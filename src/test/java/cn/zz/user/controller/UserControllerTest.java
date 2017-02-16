package cn.zz.user.controller;

import cn.zz.user.BaseSpringBootTest;
import cn.zz.user.entity.User;
import cn.zz.user.mapper.UserMapper;
import cn.zz.user.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;

/**
 * Created by zhour on 2017/2/15.
 */
public class UserControllerTest extends BaseSpringBootTest {

    @Mock
    private UserMapper userMapper = null;

    @InjectMocks
    @Resource
    private UserService userService;

    private int userId;
    private String USER_NAME = "zhou";

    @Before
    public void before(){
        host = "http://localhost:" + port;
        restTemplate = new TestRestTemplate();

        //1、Mock data
        userId = 19900220;
        User user = new User();
        user.setId(userId);
        user.setUsername(USER_NAME);
        //2、set return value
        when(userMapper.selectByPrimaryKey(userId)).thenReturn(user);
    }

    @Test
    public void getAllUsersTest() throws Exception {
        ResponseEntity<String> forEntity = restTemplate.getForEntity(
                host + "/user/"+userId, String.class);
        JSONObject jsonObject = JSONObject.parseObject(forEntity.getBody());
        assertEquals(1,jsonObject.getInteger("status").intValue());
        assertEquals(USER_NAME,jsonObject.getJSONObject("data").getString("username"));
    }
}
