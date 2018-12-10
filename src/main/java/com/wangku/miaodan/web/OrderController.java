package com.wangku.miaodan.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wangku.miaodan.core.interceptor.LoginInterceptor;
import com.wangku.miaodan.core.model.Order;
import com.wangku.miaodan.core.model.Requit;
import com.wangku.miaodan.core.model.TempOrder;
import com.wangku.miaodan.core.model.User;
import com.wangku.miaodan.core.service.IOrderService;
import com.wangku.miaodan.core.service.IRequitService;
import com.wangku.miaodan.core.service.IUserService;
import com.wangku.miaodan.utils.Strings;

@Controller
public class OrderController {
	
	@Autowired
	private IOrderService orderService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IRequitService resquitService;
	
	@RequestMapping("/order/search")
	@ResponseBody
	public Map<String, Object> search(SearchBean condition, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (condition.getType() != null && condition.getType() == 3) {
			map.put("orders", orderService.getStoredByUser(LoginInterceptor.getMobile(request)));
		} else {
			map.put("orders", orderService.selectByCondition(condition));
		}
		return map;
	}
	
	@RequestMapping("/order/detail/{id}")
	public String detail(@PathVariable("id") String id, String from, ModelMap mp, HttpServletRequest request) {
		String mobile = LoginInterceptor.getMobile(request);
		Order order = orderService.getOrderById(Long.parseLong(id));
		mp.put("isMine", orderService.checkIsMine(mobile, order.getId()));
		mp.put("from", from);
		mp.put("order", order);
		mp.put("isTD", (new Date().getTime() - order.getApplyTime().getTime())/(24 * 60 * 60 * 1000) >= 1? true:false);
		mp.put("requit", resquitService.detail(Long.parseLong(id), mobile));
		return "/order/order-detail";
	}
	
	@RequestMapping("/order/store")
	@ResponseBody
	public Map<String, Object> store(Long id, HttpServletRequest request) {
		
		String mobile = LoginInterceptor.getMobile(request);
		Map<String, Object> result = new HashMap<String, Object>();
		Order order = orderService.getOrderById(id);
		if (order == null) {
			result.put("code", 601);
		} else {
			boolean isTD = new Date().getTime() - order.getApplyTime().getTime() >= 24 * 60 * 60 * 1000;
			 //验证用户是否通过认证
			 Integer status = userService.getDetailByMobile(mobile).getStatus();
			 
			if (status == 0) {
				result.put("code", 601);
				result.put("msg", "未进行认证");
			} else if (status == 1) {
				result.put("code", 602);
				result.put("msg", "认证中");
			} else if (status == 3){
				result.put("code", 603);
				result.put("msg", "认证未通过");
			} else if (!userService.checkCanStore(mobile, isTD)) {
				result.put("code", 300);
				result.put("msg", "抢单机会用尽，请尽快充值");
			} else {
				if (userService.storeOrder(id, mobile, isTD) == 1) {
					result.put("code", 200);
					result.put("mobile", order.getMobile());
				} else {
					result.put("code", 600);
				}
				
			}
		}
		return result;
	}
	
	@RequestMapping(value = "/order/requit/{id}", method = RequestMethod.GET)
	public String requit(@PathVariable("id")Long id, ModelMap model, String from) {
		model.put("orderId", id);
		model.put("from", from);
		return "order/requit";
	}
	
	@RequestMapping(value = "/order/requit/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> requitSave(@PathVariable("id")Long id, HttpServletRequest request, Requit requit) {
		String mobile = LoginInterceptor.getMobile(request);
		User user = userService.getDetailByMobile(mobile);
		requit.setMobile(mobile);
		requit.setUserId(user.getId());
		requit.setOrderId(id);
		String requitReason = requit.getRequitReason();
		if ("0".equals(requitReason)) {
			requit.setRequitReason("客户不需要");
		} else if ("1".equals(requitReason)) {
			requit.setRequitReason("联系方式异常");
		} else if ("2".equals(requitReason)) {
			requit.setRequitReason("异地");
		}
		resquitService.save(requit);
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("code", 200);
		result.put("msg", "退单操作完成");
		return result;
	}
	
	@RequestMapping(value = "/loan", method = RequestMethod.POST)
	@ResponseBody
	public int save(@RequestBody TempOrder order) {
		
		if (order == null) {
			return 0;
		}
		
		if (Strings.isNullOrEmpty(order.getName())) {
			return 1;
		}
		
		if (Strings.isNullOrEmpty(order.getMobile())) {
			return 2;
		}
		
		if (Strings.isNullOrEmpty(order.getCity()) || order.getCity().contains("市")) {
			return 3;
		}
		
		if (order.getSum() == null || order.getSum() <= 0) {
			return 4;
		}
		
		if (Strings.isNullOrEmpty(order.getIdentyNumber())) {
			return 5;
		}
		
		if (Objects.isNull(order.getApplyTime())) {
			return 6;
		}
		
		List<Order> list = new ArrayList<Order>();
		list.add(order.translateOrder());
		orderService.saveBatch(list);
		return 200;
	}

}
