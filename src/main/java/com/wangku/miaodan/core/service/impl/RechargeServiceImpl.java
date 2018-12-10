package com.wangku.miaodan.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangku.miaodan.core.dao.RechargeMapper;
import com.wangku.miaodan.core.model.Recharge;
import com.wangku.miaodan.core.service.IRechargeService;
import com.wangku.miaodan.utils.Strings;

@Service
public class RechargeServiceImpl implements IRechargeService {
	
	@Autowired
	private RechargeMapper rechargeMapper;

	@Override
	public Recharge getDetailByIdAndMobile(Long id, String mobile) {
		return rechargeMapper.getDetailByIdAndMobile(id, mobile);
	}

	@Override
	public void notifySuccessByNumber(String number) {
		rechargeMapper.updateStatusByNumber(number, 2);
	}

	@Override
	public void notifyFailByNumber(String number) {
		rechargeMapper.updateStatusByNumber(number, 3);
	}

	@Override
	public Recharge getdetailByNumber(String number) {
		return rechargeMapper.getdetailByNumber(number);
	}

	@Override
	public List<Recharge> list(String addTime, String name, int start, int size) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("size", size);
		map.put("addTime", Strings.isNullOrEmpty(addTime)? null:addTime);
		map.put("name", Strings.isNullOrEmpty(name)? null:name);
		return rechargeMapper.list(map);
	}

	@Override
	public long count(String addTime, String name) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("addTime", Strings.isNullOrEmpty(addTime)? null:addTime);
		map.put("name", Strings.isNullOrEmpty(name)? null:name);
		return rechargeMapper.count(map);
	}

}
