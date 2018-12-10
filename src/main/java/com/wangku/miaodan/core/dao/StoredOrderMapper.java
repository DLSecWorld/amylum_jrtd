package com.wangku.miaodan.core.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.wangku.miaodan.core.model.StoredOrder;

@Repository
public interface StoredOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(@Param("orderId")Long orderId, @Param("mobile")String mobile, @Param("isTd")int isTd);

    int insertSelective(StoredOrder record);

    StoredOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StoredOrder record);

    int updateByPrimaryKey(StoredOrder record);

	StoredOrder detail(@Param("userId")Long userId, @Param("orderId")Long orderId);
}