package com.wangku.miaodan.core.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.wangku.miaodan.core.model.AuthUser;
import com.wangku.miaodan.core.service.IAuthUserService;
import com.wangku.miaodan.utils.Strings;

public class AdminLoginInterceptor implements HandlerInterceptor {
	
	private static final Map<String, String> users = new HashMap<String, String>();
	
	private IAuthUserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		boolean flag = false;
		String ticket = getTicket(request);// 未登录， 重定向到登录页面
		if (!Strings.isNullOrEmpty(ticket)) {
			AuthUser user = userService.getDetailByTicket(getTicket(request));
			if (user != null) {
				users.put(ticket, user.getUsername());
				flag = true;
			}
		}
		
		if (!flag) {
			response.sendRedirect(request.getContextPath()+"/admin/login");
		}
		return flag;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}
	
	public static final String getTicket(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("adminticket".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	
	public static final String getUserName(HttpServletRequest request) {
		return users.get(getTicket(request));
	}
	
	public static final void setUserName(String ticket, String username) {
		users.put(ticket, username);
	}

	public void setUserService(IAuthUserService userService) {
		this.userService = userService;
	}

	public static final void clearTicket(HttpServletRequest request) {
		String ticket = getTicket(request);
		String username = users.get(ticket);
		users.remove(ticket);
	}
	
}
