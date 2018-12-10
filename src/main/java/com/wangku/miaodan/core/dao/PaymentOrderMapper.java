package com.wangku.miaodan.core.dao;

import org.springframework.stereotype.Repository;

import com.wangku.miaodan.core.model.PaymentOrder;

@Repository
public interface PaymentOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PaymentOrder record);

    int insertSelective(PaymentOrder record);

    PaymentOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PaymentOrder record);

    int updateByPrimaryKey(PaymentOrder record);

	void notifyResult(PaymentOrder paymentOrder);
}