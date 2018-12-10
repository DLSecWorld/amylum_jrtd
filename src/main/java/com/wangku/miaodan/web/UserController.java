package com.wangku.miaodan.web;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wangku.miaodan.constant.ProductTypeEnums;
import com.wangku.miaodan.core.interceptor.LoginInterceptor;
import com.wangku.miaodan.core.model.Recharge;
import com.wangku.miaodan.core.model.User;
import com.wangku.miaodan.core.service.IRechargeService;
import com.wangku.miaodan.core.service.IUserService;
import com.wangku.miaodan.utils.PayUtil;


@Controller
@RequestMapping("/user")
public class UserController {
	
	private static final Logger LOG = Logger.getLogger(UserController.class);
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IRechargeService rechargeService;
	
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String detail(HttpServletRequest request, ModelMap model) {
		model.put("user", userService.getDetailByMobile(LoginInterceptor.getMobile(request)));
		return "/user/home";
	}
	
	/**
	 * 完善和修改用户信息
	 * @param user
	 * @param request
	 * @return
	 * @author lulu@99114.com
	 * @time 2018年11月10日 下午4:31:01
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> update(User user, HttpServletRequest request) {
		String mobile = LoginInterceptor.getMobile(request);
		user.setMobile(mobile);
		user.setStatus(1);
		userService.update(user);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 200);
		result.put("msg", "修改成功");
		return result;
	}
	
	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> auth(Integer status, HttpServletRequest request) {
		User user = new User();
		user.setMobile(LoginInterceptor.getMobile(request));
		user.setStatus(status);
		userService.update(user);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 200);
		result.put("msg", "用户认证状态修改成功");
		return result;		
	}
	
	@RequestMapping(value = "/recharge", method = RequestMethod.GET)
	public String rechargeHome(HttpServletRequest request, ModelMap model) {
		String orderId = request.getParameter("orderId");
		if (orderId != null) {
			model.put("recharge", rechargeService.getDetailByIdAndMobile(Long.parseLong(orderId), LoginInterceptor.getMobile(request)));
		}
		model.put("user", userService.getDetailByMobile(LoginInterceptor.getMobile(request)));
		return "/user/recharge";
	}
	
	/**
	 * 购买页面
	 * 
	 * @param type    商品标识
	 * @param request
	 * @return
	 * @author lulu@99114.com
	 * @time 2018年11月14日 下午6:28:43
	 */
	@RequestMapping(value = "/recharge", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> recharge(long type,HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		ProductTypeEnums byType = ProductTypeEnums.getByType(type);
		if (byType == null) {
			result.put("code", 601);
			result.put("msg", "商品不存在");
			return result;			
		}
		
		String mobile = LoginInterceptor.getMobile(request);
		if (Objects.isNull(mobile) || mobile.isEmpty()) {
			result.put("code", 602);
			result.put("msg", "用户未登录");
			return result;	
		}
		
		BigDecimal price = byType.getPrice();
		BigDecimal sum =  new BigDecimal(price.doubleValue() * 1);
		String number = PayUtil.getNonceStr();
		Recharge recharge = new Recharge(number, mobile, byType.getType(), 1, price, sum);
		Long id = userService.addRecharge(recharge);
		result.put("code", 200);
		result.put("msg", "订单创建成功");
		result.put("data", id);
		return result;	
	}
	
	@RequestMapping("/recharge/pay")
	public String rechargeDetail(long id, HttpServletRequest request, ModelMap mp) {
		Recharge recharge = rechargeService.getDetailByIdAndMobile(id, LoginInterceptor.getMobile(request));
		mp.put("recharge", recharge);
		return "/user/recharge-detail";
	}
	
	@RequestMapping(value = "/certify", method = RequestMethod.GET)
	public String certifyGet(HttpServletRequest request, ModelMap model) {
		model.put("user", userService.getDetailByMobile(LoginInterceptor.getMobile(request)));
		return "/user/certify";
	}
	
	@RequestMapping(value ="/paylog", method = RequestMethod.GET)
	public String consumerLog(HttpServletRequest request, ModelMap model) {
		return "/user/log";
	}

}
