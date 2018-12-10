package com.wangku.miaodan.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangku.miaodan.core.dao.PaymentOrderMapper;
import com.wangku.miaodan.core.model.PaymentOrder;
import com.wangku.miaodan.core.service.IPaymentOrderService;
import com.wangku.miaodan.core.service.IRechargeService;
import com.wangku.miaodan.core.service.IUserService;

@Service
public class PaymentOrderServiceImpl implements IPaymentOrderService {
	
	@Autowired
	private PaymentOrderMapper orderMapper;
	
	@Autowired
	private IRechargeService rechargeService;
	
	@Autowired
	private IUserService userService;

	@Override
	public int add(PaymentOrder order) {
		return 0;
	}

	@Override
	public void notifyResult(PaymentOrder paymentOrder) {
		orderMapper.notifyResult(paymentOrder);
	}

}
