package cn.zz.user.controller;

import cn.zz.user.BaseSpringBootTest;
import cn.zz.user.entity.User;
import cn.zz.user.mapper.UserMapper;
import cn.zz.user.service.UserService;
import cn.zz.user.service.impl.UserServiceImpl;
import com.alibaba.fastjson.JSONObject;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

/**
 * Created by zhour on 2017/2/15.
 */
public class UserControllerTest extends BaseSpringBootTest {
    private UserMapper userMapper = null;

    @Resource
    private UserService userService;

    private int userId;
    private String USER_NAME = "zhou";

    @Before
    public void before(){
        host = "http://localhost:" + port;
        restTemplate = new TestRestTemplate();

        //1、Mock HospitalMapper
        userMapper = EasyMock.createMock(UserMapper.class);
        userId = 19900220;
        User user = new User();
        user.setId(userId);
        user.setUsername(USER_NAME);
        //2、set return value
        EasyMock.expect(userMapper.selectByPrimaryKey(userId)).andReturn(user);
        EasyMock.replay(userMapper);

        //3、set mapper for service
        //spring的单例Service这样测试，多例的还不清楚怎么
        try{
            Field field = UserServiceImpl.class.getDeclaredField("userMapper");
            if (field != null){
                field.setAccessible(true);
                field.set(userService,userMapper);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void getAllUsersTest() throws Exception {
        ResponseEntity<String> forEntity = restTemplate.getForEntity(
                host + "//user/"+userId, String.class);
        JSONObject jsonObject = JSONObject.parseObject(forEntity.getBody());
        assertEquals(1,jsonObject.getInteger("status").intValue());
        assertEquals(USER_NAME,jsonObject.getJSONObject("data").getString("username"));
    }
}
