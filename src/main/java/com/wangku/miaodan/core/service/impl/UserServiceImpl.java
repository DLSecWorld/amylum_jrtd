package com.wangku.miaodan.core.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wangku.miaodan.constant.ProductTypeEnums;
import com.wangku.miaodan.core.dao.OrderMapper;
import com.wangku.miaodan.core.dao.RechargeMapper;
import com.wangku.miaodan.core.dao.StoredOrderMapper;
import com.wangku.miaodan.core.dao.UserMapper;
import com.wangku.miaodan.core.model.Recharge;
import com.wangku.miaodan.core.model.User;
import com.wangku.miaodan.core.service.IUserService;
import com.wangku.miaodan.utils.Strings;
import com.wangku.miaodan.web.PayMentController;

@Service
public class UserServiceImpl implements IUserService {
	
	private static final Logger LOG = Logger.getLogger(IUserService.class);
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private RechargeMapper rechargeMapper;
	
	@Autowired
	private StoredOrderMapper storedOrderMapper;
	
	@Autowired
	private OrderMapper orderMapper;

	@Override
	public boolean checkCanStore(String mobile, boolean idTD) {
		return userMapper.getTimesOfUser(mobile, idTD) > 0;
	}

	@Override
	public long storeOrder(Long orderId, String mobile, boolean isTD) {
		int counsumeOrder = orderMapper.counsumeOrder(orderId);
		if (counsumeOrder > 0) {
			storedOrderMapper.insert(orderId, mobile, isTD? 0:1);
			userMapper.reduceTimesByMobile(mobile, isTD);
			return 1;
		} else {
			return 0;
		}		
	}	

	@Override
	public void addUser(String mobile) {
		userMapper.addUser(mobile);
	}
	
	@Override
	public User getDetailByMobile(String mobile) {
		return userMapper.selectByMobile(mobile);
	}

	@Override
	public void incrTimesByMobile(String mobile, int times) {
		
	}

	@Override
	public void update(User user) {
		user.setUpdateTime(new Date());
		userMapper.updateByMobile(user);
	}

	@Override
	public Long addRecharge(Recharge recharge) {
		rechargeMapper.insert(recharge);
		return recharge.getId();
	}

	@Override
	public void recharge(Map<String, String> restmap) {
		ProductTypeEnums byType = ProductTypeEnums.getByType(Long.parseLong(restmap.get("attach")));
		LOG.info("用户充值成功，更新抢单机会:" + byType.getJpTimes() + "---------" + byType.getTdTimes());
		userMapper.increTimesByRechargeNumber(restmap.get("out_trade_no"), byType.getJpTimes(), byType.getTdTimes());
	}

	@Override
	public void addOpenId(String mobile, String openId) {
		userMapper.addOpenId(mobile, openId);
	}

	@Override
	public boolean isAuth(String mobile) {
		User user = userMapper.selectByMobile(mobile);
		return user.getStatus() == 2;
	}

	
	@Override
	public List<User> list(String city, String status, String addTime, int start, int size) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("city", Strings.isNullOrEmpty(city)? null: city.replace("市", ""));
		try {
			param.put("status", Integer.parseInt(status));
		} catch (NumberFormatException e) {}
		param.put("addTime", Strings.isNullOrEmpty(addTime)?null:addTime);
		param.put("start", start);
		param.put("size", size);
		
		return userMapper.list(param);
	}	

	@Override
	public void checkAuth(Long userId, int status) {
		User record = new User();
		record.setId(userId);
		record.setStatus(status);
		userMapper.updateByPrimaryKey(record);
	}

	@Override
	public long count(String city, String status, String addTime) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("city", Strings.isNullOrEmpty(city)? null: city.replace("市", ""));
		try {
			param.put("status", Integer.parseInt(status));
		} catch (NumberFormatException e) {}
		param.put("addTime", Strings.isNullOrEmpty(addTime)?null:addTime);
		return userMapper.count(param);
	}

	@Override
	public User getDetailById(Long id) {
		return userMapper.selectByPrimaryKey(id);
	}

}
