package cn.zz.user.common.restful;

import cn.zz.user.common.util.HostnameNoVerifier;
import cn.zz.user.common.util.TrustAllStrategy;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HttpUtil implements HttpConstant{

    private static CloseableHttpClient httpClient = null;

    private final static Object syncLock = new Object();

    public static CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (syncLock) {
                if (httpClient == null) {
                    try {
                        httpClient = createHttpClient();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (KeyStoreException e) {
                        e.printStackTrace();
                    } catch (KeyManagementException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return httpClient;
    }

    public static String parseParameterOfUrl(String url, Map<String, String> params) {
        StringBuilder tmp = new StringBuilder(url);
        if (params != null && params.size() > 0) {
            tmp.append("?");
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Iterator<Map.Entry<String, String>> iterator = entries.iterator(); iterator.hasNext(); ) {
                Map.Entry<String, String> entry = iterator.next();
                tmp.append(entry.getKey());
                tmp.append("=");
                try {
                    tmp.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if (iterator.hasNext()) {
                    tmp.append("&");
                }
            }
        }
        return tmp.toString();
    }

    private static CloseableHttpClient createHttpClient()
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception,
                                        int executionCount, HttpContext context) {
                if (executionCount >= 5) {// 如果已经重试了5次，就放弃
                    return false;
                } else if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                } else if (exception instanceof SSLHandshakeException // 不要重试SSL握手异常
                        || exception instanceof InterruptedIOException
                        || exception instanceof UnknownHostException
                        || exception instanceof ConnectTimeoutException
                        || exception instanceof SSLException) {
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(SOCKET_TIME_OUT)
                .setConnectTimeout(CONNECT_TIME_OUT).build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(getIgnoreVerifySSLConnManager())
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(httpRequestRetryHandler).build();

        return httpClient;
    }

    private static PoolingHttpClientConnectionManager getIgnoreVerifySSLConnManager()
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, new TrustAllStrategy());

        SSLContext sslcontext = builder.build();
        sslcontext.init(null, new TrustManager[] { new EasyX509TrustManager() }, null);
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,new HostnameNoVerifier());

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory())
                .register("https", sslsf)
                .build();

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry);
        // 将最大连接数增加
        connManager.setMaxTotal(CM_MAX_TOTAL);
        // 将每个路由基础的连接增加
        connManager.setDefaultMaxPerRoute(CM_MAX_PER_ROUTER);
        // 将目标主机的最大连接数增加
        //HttpHost httpHost = new HttpHost(hostname, port);
        //connManager.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);
        return connManager;
    }
}
