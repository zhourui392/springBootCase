package cn.zz.user.service;

import cn.zz.user.common.util.page.PageQuery;
import cn.zz.user.common.util.page.PageResult;
import cn.zz.user.entity.User;
import cn.zz.user.service.base.BaseService;

public interface UserService  extends BaseService<User> {

	User getUserByUsername(String username);

	User getUserByUsernameAndPassword(String username, String password);

	PageResult<User> getUsersByPage(PageQuery pageQuery);
}
