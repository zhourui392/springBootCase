package cn.zz.user.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.zz.user.entity.User;

public abstract class BaseController implements HandlerInterceptor {
	public static User user = null;
	private HttpServletRequest myRequest;
	
	public BaseController(){
	}
	
	@Override
	public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler )
		throws Exception {
		this.myRequest = request;
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		User user = (User)request.getSession().getAttribute("user");
		/*if(user != null && UserCache.deleteMap.containsKey(user.getId())){//用户已被删除
			//clear session
			request.getSession().removeAttribute("user");
			request.getSession().invalidate();
			//处理Ajax请求
			if (request.getHeader("x-requested-with") != null
					&& request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
				response.setHeader("sessionstatus", "isdelete");// 在响应头设置session状态
			}
			return false;
		}*/
		return true;
	}


	@Override
	public void postHandle( HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView ) throws Exception {
		this.myRequest = request;
		if ( isLegalView(modelAndView) ) {
			modelAndView.addObject("newdate", new Date());
		}
	}


	@Override
	public void afterCompletion( HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex )
		throws Exception {
	}


	/**
	 * 判断是否为合法的视图地址
	 * <p>
	 * 
	 * @param modelAndView
	 *            spring 视图对象
	 * @return boolean
	 */
	protected boolean isLegalView( ModelAndView modelAndView ) {
		boolean legal = false;
		if ( modelAndView != null ) {
			String viewUrl = modelAndView.getViewName();
			if ( viewUrl != null && viewUrl.contains("redirect:") ) {
				legal = false;
			} else {
				legal = true;
			}
		}
		return legal;
	}
	
	/**
	 * get userid by request session
	 * @return
	 */
	protected Integer getUserIdBySession(){
		/*User user;
		if (myRequest.getSession() != null && myRequest.getSession().getAttribute("user") != null){
			user = (User)myRequest.getSession().getAttribute("user");
			Integer userId = user == null ? null: user.getId();
			return userId;
		}*/
		return null;
	}
}
