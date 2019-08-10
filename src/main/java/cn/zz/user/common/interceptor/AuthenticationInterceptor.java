package cn.zz.user.common.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import cn.zz.user.common.cache.TokenCache;
import cn.zz.user.common.cache.UserToken;
import cn.zz.user.common.util.Root;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author : zhourui
 * @version: 2019-08-09 16:30
 **/

@Component
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    @Resource
    TokenCache tokenCache;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)
        throws Exception {
        String token = httpServletRequest.getHeader("token");

        if (token == null){
            writeNoAuthenticationResponse(httpServletResponse);
            return false;
        }
        UserToken cache = tokenCache.getCache(token);
        if (cache == null){
            writeNoAuthenticationResponse(httpServletResponse);
            return false;
        }
        return true;
    }

    private void writeNoAuthenticationResponse(HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try{
            response.sendError(401);
        }
        catch (IOException e){
            e.printStackTrace();
            try {
                response.sendError(500);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
        ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
        Object o, Exception e) throws Exception {

    }
}
