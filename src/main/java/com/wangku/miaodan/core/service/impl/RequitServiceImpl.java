package com.wangku.miaodan.core.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wangku.miaodan.core.dao.OrderMapper;
import com.wangku.miaodan.core.dao.RequitMapper;
import com.wangku.miaodan.core.dao.StoredOrderMapper;
import com.wangku.miaodan.core.dao.UserMapper;
import com.wangku.miaodan.core.model.Order;
import com.wangku.miaodan.core.model.Requit;
import com.wangku.miaodan.core.model.StoredOrder;
import com.wangku.miaodan.core.service.IRequitService;
import com.wangku.miaodan.utils.Strings;

@Service
public class RequitServiceImpl implements IRequitService {
	
	@Autowired
	private RequitMapper requitMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private StoredOrderMapper storeOrderMapper; 
	
	@Autowired
	private OrderMapper orderMapper;

	@Override
	public List<Requit> list(String name, String status, int start, int size) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("size", size);
		map.put("name", Strings.isNullOrEmpty(name)? null:name);
		try {
			map.put("status", Integer.parseInt(status));
		} catch (NumberFormatException e) {}
		
		
		return requitMapper.list(map);
	}

	@Override
	public void save(Requit requit) {
		requit.setUpdateTime(new Date());
		requitMapper.insert(requit);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateStatus(Long requitId, int status) {
		Requit requit = requitMapper.selectByPrimaryKey(requitId);
		requit.setStatus((byte)status);
		requitMapper.updateByPrimaryKey(requit);
		if (status == 1) {
			StoredOrder storeOrder = storeOrderMapper.detail(requit.getUserId(), requit.getOrderId());
			userMapper.increTimesByMobile(storeOrder.getMobile(), storeOrder.getIsTd());
			Order order = orderMapper.selectByPrimaryKey(storeOrder.getOrderId());
			order.setStatus(0);
			orderMapper.updateByPrimaryKey(order);
		}
	}

	@Override
	public Requit detail(long orderId, String mobile) {
		return requitMapper.selectByOrderId(orderId, mobile);
	}

	@Override
	public long count(String name, String status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", Strings.isNullOrEmpty(name)? null:name);
		try {
			map.put("status", Integer.parseInt(status));
		} catch (NumberFormatException e) {}		
		return requitMapper.count(map);
	}

}
