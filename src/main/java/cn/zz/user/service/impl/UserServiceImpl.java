package cn.zz.user.service.impl;

import cn.zz.user.common.util.lang.Strings;
import cn.zz.user.common.util.page.PageQuery;
import cn.zz.user.common.util.page.PageResult;
import cn.zz.user.entity.User;
import cn.zz.user.mapper.UserMapper;
import cn.zz.user.mapper.base.BaseMapper;
import cn.zz.user.service.UserService;
import cn.zz.user.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

	@Override
	public BaseMapper<User> getBaseMapper() {
		return userMapper;
	}

	@Resource
	private UserMapper userMapper;

	@Override
	public User getUserByUsername(String username) {
		username = Strings.transactSQLInjection(username);
		return userMapper.getUserByUsername(username);
	}

	@Override
	public User getUserByUsernameAndPassword(String username, String password) {
		username = Strings.transactSQLInjection(username);
		password = Strings.transactSQLInjection(username);
		return userMapper.getUserByUsernameAndPassword(username, password);
	}

	@Override
	public PageResult<User> getUsersByPage(PageQuery pageQuery) {
		PageResult<User> result = new PageResult();
		result.setPageQuery(pageQuery);
		int totalCount = userMapper.getCountByConditions();
		result.setCount(totalCount);
		if (totalCount == 0){
			return result;
		}
		List<User> list = userMapper.getListByConditions(pageQuery);
		result.setItems(list);
		return result;
	}
}
