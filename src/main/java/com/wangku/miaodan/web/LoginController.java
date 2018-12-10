package com.wangku.miaodan.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wangku.miaodan.core.interceptor.LoginInterceptor;
import com.wangku.miaodan.core.model.Token;
import com.wangku.miaodan.core.service.ITokenService;
import com.wangku.miaodan.core.service.IUserService;
import com.wangku.miaodan.utils.Strings;
import com.wangku.miaodan.utils.VerifyCodeUtil;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ITokenService tokenService;
	
	@RequestMapping("/index")
	public ModelAndView login(ModelAndView mav) {
		mav.setViewName("/login/login");
		return mav;
	}
	
	@RequestMapping("/out")
	public String logout(HttpServletRequest request) {
		String ticket = getTicket(request);
		Token token = tokenService.getDetailByCondition(new Token(null, ticket, null, 0));
		if (token != null) {
			tokenService.updateLoginInfo(new Token(token.getMobile(), null, null, 0));
		}
		return "redirect:/login/index";
	}
	
	@RequestMapping("/checkVerifyCode")
	@ResponseBody
	public Map<String, Object> checkUser(String mobile, String msgCode) {
		Map<String, Object> result = new HashMap<String, Object>(4);
		long nowTime = System.currentTimeMillis();
		Token token = tokenService.getDetailByCondition(new Token(mobile, null, msgCode, 0));
		
		if (Objects.isNull(token)) {// 验证验证码是否无效
			result.put("code", 601);
			result.put("msg", "无效验证码");			
		} else if (token.getVerifyInvalidTime() < nowTime) {// 验证验证码有效期
			result.put("code", 602);
			result.put("msg", "验证码过期");
		} else {
			String ticket = UUID.randomUUID().toString();
			tokenService.updateLoginInfo(new Token(mobile, ticket, null, 0));
			userService.addUser(mobile);// 直接进行新用户创建
			result.put("code", 200);
			result.put("msg", "验证通过");
			result.put("ticket", ticket);
		}
		
		return result;
	}
	
	@RequestMapping("/sendVerifyCode")
	@ResponseBody
	public Map<String, Object> sendVerifyCode(String mobile) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 验证手机号是否有效
		if (mobile == null || mobile.isEmpty() || !mobile.matches("^1(?:3\\d|4[4-9]|5[0-35-9]|6[67]|7[013-8]|8\\d|9\\d)\\d{8}")) {
			result.put("code", 601);
			result.put("msg", "无效手机号格式");
			return result;
		} else {
			String verifyCode = VerifyCodeUtil.getVerifyCode();
			tokenService.addLoginInfo(new Token(mobile, null, verifyCode, System.currentTimeMillis() + 5 * 60 * 1000));
			LoginInterceptor.sendCode(mobile, verifyCode);
			result.put("code", 200);
			result.put("msg", "验证码发送成功");			
		}
		return result;
	}
	
	public static String getTicket(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("ticket")) {
				return cookie.getValue();
			}
		}
		return null;
	}	

}
