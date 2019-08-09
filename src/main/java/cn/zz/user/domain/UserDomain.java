package cn.zz.user.domain;

import cn.zz.user.entity.User;
import cn.zz.user.mapper.UserMapper;

import javax.annotation.Resource;

/**
 * @author : zhourui
 * @version: 2019-07-24 11:30
 **/
public class UserDomain {

    @Resource
    private UserMapper userMapper;

    public void save(String userName, String password){
        User user = new User();
        user.setUsername(userName);
        userMapper.insert(user);
    }

    public void deleteUser(Integer userId){
        userMapper.deleteByPrimaryKey(userId);
    }
}
