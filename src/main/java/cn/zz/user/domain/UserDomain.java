package cn.zz.user.domain;

import cn.zz.user.common.tool.MD5Util;
import cn.zz.user.common.util.lang.Times;
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

    public boolean save(String showName, String userName, String password){
        User user = new User();
        user.setShowName(showName);
        user.setUsername(userName);
        //password加密
        user.setPassword(MD5Util.md5(password));
        user.setStatus(User.STAUS_NORMAL);
        user.setCreatedtime(Times.now());
        user.setUpdatedtime(Times.now());
        return userMapper.insert(user) > 0;
    }

    public void deleteUser(Integer userId){
        userMapper.deleteByPrimaryKey(userId);
    }
}
