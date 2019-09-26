package cn.zz.user.client.retry;

import javax.annotation.Resource;

import cn.zz.user.BaseSpringBootTest;
import cn.zz.user.entity.User;
import cn.zz.user.mapper.UserMapper;
import cn.zz.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class RetryTest extends BaseSpringBootTest {

    @InjectMocks
    @Resource
    private UserService userService;

    @Mock
    UserMapper userMapper;

    String userName = "test";
    Integer userId = 1;

    @Before
    public void before(){
        User user = new User();
        user.setId(userId);
        user.setUsername(userName);
        //2、set return value
        when(userMapper.getUserByUsername(userName)).thenReturn(user);
    }

    @Test
    public void testRetry() throws Exception {
        User test = Retry.get(userService)
            .setFailAction(e -> {
                e.printStackTrace();
            })
            .catchAll(Exception.class)
            .register(UserService.class)
            .getUserByUsername("test");

        assertNotNull(test);
        assertEquals(userId, test.getId());
    }

}