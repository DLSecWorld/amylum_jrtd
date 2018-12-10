package com.wangku.miaodan.core.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.wangku.miaodan.core.model.Token;
import com.wangku.miaodan.core.service.ITokenService;
import com.wangku.miaodan.utils.Strings;
import com.wangku.miaodan.utils.message.aliyun.MessageUtils;

public class LoginInterceptor implements HandlerInterceptor {
	
	private static final Map<String, String> tickets = new HashMap<String, String>();
	
	private ITokenService tokenService;
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String ticket = getTicket(request);// 未登录， 重定向到登录页面
		boolean flag;
		if (ticket == null) {
			flag = false;
		} else {
			Token token = tokenService.getDetailByCondition(new Token(null, ticket, null, 0));
			if (token == null || Strings.isNullOrEmpty(token.getMobile())) {
				flag = false;
			} else {
				tickets.put(ticket, token.getMobile());
				flag = true;
			}
		}
		
		if (!flag) {
			response.sendRedirect(request.getContextPath()+"/login/index");
		}
		return flag;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}
	
	public static String getTicket(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("ticket")) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	
	public static void sendCode(String mobile, String verifyCode) {
		System.out.println(mobile + "------" + verifyCode);
		MessageUtils.sendSms(mobile, verifyCode);
	}
	
	public static String getMobile(HttpServletRequest request) {
		String mobile = null;
		String ticket = getTicket(request);
		if (ticket != null) {
			mobile = tickets.get(ticket);
		}
		return mobile;
	}

	public void setTokenService(ITokenService tokenService) {
		this.tokenService = tokenService;
	}

}
