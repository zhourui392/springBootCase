package cn.zz.user.common.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.zz.user.common.cache.TokenCache;
import cn.zz.user.common.cache.UserToken;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author : zhourui
 * @version: 2019-08-09 16:30
 **/

@Component
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    @Resource
    TokenCache tokenCache;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)
        throws Exception {

        String token = httpServletRequest.getHeader("token");
        String url = httpServletRequest.getRequestURI();


        if (token == null){
            return false;
        }
        UserToken cache = tokenCache.getCache(token);
        if (cache == null){
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
        ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
        Object o, Exception e) throws Exception {

    }
}
