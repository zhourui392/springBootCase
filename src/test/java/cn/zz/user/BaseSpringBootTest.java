package cn.zz.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by zhour on 2017/2/9.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceApplication.class,webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseSpringBootTest {
    @Value("${local.server.port}")// 注入端口号
    protected int port;
    protected String host;
    protected TestRestTemplate restTemplate;

    @Before
    public void before() {
        host = "http://localhost:" + port;
        restTemplate = new TestRestTemplate();
    }

    @Test
    public void test(){

    }
}
