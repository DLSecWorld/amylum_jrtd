package com.wangku.miaodan.web;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wangku.miaodan.core.interceptor.LoginInterceptor;
import com.wangku.miaodan.core.model.User;
import com.wangku.miaodan.core.service.IUserService;

@Controller
public class HomeController {
	
	@Autowired
	private IUserService userService;
	
	@RequestMapping(value = "/home", produces = "text/html;charset=UTF-8")
	public String home(ModelMap model, SearchBean condition, HttpServletRequest request) {
		String mobile = LoginInterceptor.getMobile(request);
		User user = userService.getDetailByMobile(mobile);
		if (user.getOpenId() == null) {
			String openId = request.getParameter("openid");
			if(!Objects.isNull(openId) && !openId.isEmpty()) {
				userService.addOpenId(mobile, openId);
				model.put("openId", openId);
			}
		} else {
			model.put("openId", user.getOpenId());
		}
		model.put("condition", condition);
		return "home";
	}
	
	@RequestMapping(value = "/jpdd", produces = "text/html;charset=UTF-8")
	public String jpdd(ModelMap model, SearchBean condition) {
		model.put("condition", condition);
		return "/order/jpdd";
	}
	
	@RequestMapping(value = "/td", produces = "text/html;charset=UTF-8")
	public String td(ModelMap model, SearchBean condition) {
		model.put("condition", condition);
		return "/order/td";
	}
	
	@RequestMapping("/mine")
	public String mine() {
		return "/order/mine";
	}
	
	@RequestMapping("/option/{path}")
	public String about(@PathVariable("path")String path) {
		return "/option/" + path;
	}

}
