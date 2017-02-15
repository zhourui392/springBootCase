package cn.zz.user.controller;

import cn.zz.user.entity.User;
import cn.zz.user.service.UserService;
import cn.zz.user.common.tool.MD5Util;
import cn.zz.user.common.util.Root;
import cn.zz.user.common.util.lang.Times;
import cn.zz.user.common.util.page.PageQuery;
import cn.zz.user.common.util.page.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController extends BaseController{
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;


	@RequestMapping(value = "/user" ,method = RequestMethod.GET)
	public String login(@RequestParam(value = "username") String username) throws Exception {
		logger.debug("RequestParam.username is {}.", username);
		Root root = Root.getRootOKAndSimpleMsg();
		User user = userService.getUserByUsername(username);
		root.setData(user);
		return root.toJsonString();
	}

	@RequestMapping(value = "/user/name" ,method = RequestMethod.GET)
	public String loginNameIsExist(@RequestParam(value = "username") String username) throws Exception {
		logger.debug("RequestParam.username is {}.", username);
		Root root = Root.getRootOKAndSimpleMsg();
		User user = userService.getUserByUsername(username);
		if (user != null){
			root.setData(1);
		}else{
			root.setData(0);
		}
		return root.toJsonString();
	}

	@RequestMapping(value = "/tus_user/test" ,method = RequestMethod.POST)
	public void testController(@RequestParam(value = "username") String username,
							   @RequestParam(value = "password") String password) {
		logger.debug("test controller");
	}

	@ResponseBody
	@RequestMapping(value="/user/{id}", method= RequestMethod.GET)
	public String getUserById(@PathVariable("id") int id) {
		User user = userService.getById(id);
		user.setPassword(null);
		return Root.getRootOKAndSimpleMsg().setData(user).toJsonString();
	}

	@ResponseBody
	@RequestMapping(value="/user/{id}", method= RequestMethod.DELETE)
	public String deleteUserById(@PathVariable("id") int id) {
		boolean resultBoolean = userService.deleteById(id);
		if (resultBoolean) return Root.getRootOKAndSimpleMsg().toJsonString();
		return Root.getRootFailAndSimpleMsg().toJsonString();
	}

	@ResponseBody
	@RequestMapping(value="/user", method= RequestMethod.POST)
	public String addUser(@RequestParam(value = "showName") String showName, @RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
		User user = new User();
		//TODO 验证非空
		user.setShowName(showName);
		user.setUsername(username);
		//password加密
		user.setPassword(MD5Util.md5(password));
		user.setStatus(User.STAUS_NORMAL);
		user.setCreatedtime(Times.now());
		user.setUpdatedtime(Times.now());
		boolean resultBoolean = userService.add(user);
		if (resultBoolean) return Root.getRootOKAndSimpleMsg().toJsonString();
		return Root.getRootFailAndSimpleMsg().toJsonString();
	}

	@ResponseBody
	@RequestMapping(value="/user/{id}", method= RequestMethod.POST)
	public String updateUser(@PathVariable("id") int id, @RequestParam(value = "showName") String showName, @RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
		User user = new User();
		user.setId(id);
		if (showName != null){
			user.setShowName(showName);
		}
		if (username != null){
			user.setUsername(username);
		}
		if (password != null){
			user.setPassword(MD5Util.md5(password));
		}
//		if (status != null){
//			user.setStatus(status);
//		}
		user.setUpdatedtime(Times.now());
		boolean resultBoolean = userService.update(user);
		if (resultBoolean) return Root.getRootOKAndSimpleMsg().toJsonString();
		return Root.getRootFailAndSimpleMsg().toJsonString();
	}

	@RequestMapping(value = "/user/login" ,method = RequestMethod.GET)
	public String login(@RequestParam(value = "username") String username,
						@RequestParam(value = "password") String password, HttpServletRequest request) throws Exception {
		logger.debug("RequestParam.username is {}, password is {}.", username, password);
		User user = userService.getUserByUsernameAndPassword(username,password);
		if (user != null){
			return Root.getRootOKAndSimpleMsg().setData(user).toJsonString();
		}
		return Root.getRootFailAndSimpleMsg().toJsonString();
	}

	@ResponseBody
	@RequestMapping(value="/users", method= RequestMethod.GET)
	public String getUsersByPage(@RequestParam(value = "pageIndex", required=false) Integer pageIndex) {
		PageQuery pageQuery = new PageQuery();
		if (pageIndex != null){
			pageQuery.setPageIndex(pageIndex);
		}
		PageResult<User> pageResult = userService.getUsersByPage(pageQuery);
		return Root.getRootOKAndSimpleMsg().setData(pageResult).toJsonString();
	}
}
