package com.wangku.miaodan.core.service;

import com.wangku.miaodan.core.model.PaymentOrder;

public interface IPaymentOrderService {
	
	public int add(PaymentOrder order);

	public void notifyResult(PaymentOrder paymentOrder);

}
