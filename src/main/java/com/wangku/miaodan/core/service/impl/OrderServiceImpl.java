package com.wangku.miaodan.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangku.miaodan.core.dao.OrderMapper;
import com.wangku.miaodan.core.model.Order;
import com.wangku.miaodan.core.service.IOrderService;
import com.wangku.miaodan.utils.Strings;
import com.wangku.miaodan.web.SearchBean;

@Service
public class OrderServiceImpl implements IOrderService {
	
	@Autowired
	private OrderMapper orderMapper;

	@Override
	public List<Order> selectByCondition(SearchBean condition) {
		Integer page = condition.getPage();
		page = page == null? 1:page <= 0? 1 : page;
		condition.setPage((condition.getPage() - 1) * condition.getSize());
		return orderMapper.selectByCondition(condition);
	}

	@Override
	public Order getOrderBySourceIdAndType(String sourceId, int type) {
		return orderMapper.selectBySourceIdAndType(sourceId, type);
	}

	@Override
	public List<Order> getStoredByUser(String mobile) {
		return orderMapper.getStoredByUser(mobile);
	}

	@Override
	public boolean checkIsMine(String mobile, Long id) {
		return orderMapper.selectByMobileAndId(mobile, id) > 0;
	}

	@Override
	public void saveBatch(List<Order> list) {
		if (list.size() > 0) {
			orderMapper.saveBatch(list);
		}
	}

	@Override
	public Order getOrderById(Long id) {
		return orderMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Order> list(String applyTime, String source, String status, String city, int start, int size) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("size", size);
		map.put("applyTime", Strings.isNullOrEmpty(applyTime)? null:applyTime);
		map.put("source", Strings.isNullOrEmpty(source)? null:source);
		map.put("city", Strings.isNullOrEmpty(city)? null:city.replace("市", ""));
		try {
			map.put("status", Integer.parseInt(status));
		} catch (NumberFormatException e) {}
		
		
		return orderMapper.list(map);
	}

	@Override
	public void updateStatus(Long productId, int status) {
		Order order = new Order();
		order.setId(productId);
		order.setStatus(status);
		orderMapper.updateByPrimaryKeySelective(order);
	}

	@Override
	public long count(String applyTime, String source, String status, String city) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("applyTime", Strings.isNullOrEmpty(applyTime)? null:applyTime);
		map.put("source", Strings.isNullOrEmpty(source)? null:source);
		map.put("city", Strings.isNullOrEmpty(city)? null:city.replace("市", ""));
		try {
			map.put("status", Integer.parseInt(status));
		} catch (NumberFormatException e) {}	
		
		return orderMapper.count(map);
	}

}
