package com.wangku.miaodan.core.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.wangku.miaodan.core.model.Order;
import com.wangku.miaodan.web.SearchBean;

@Repository
public interface OrderMapper {
	
    int deleteByPrimaryKey(Long id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
    
  	List<Order> selectByCondition(SearchBean condition);

  	Order selectBySourceIdAndType(@Param("sourceId")String sourceId, @Param("type")int type);

	int counsumeOrder(@Param("id")Long orderId);

	List<Order> getStoredByUser(@Param("mobile")String mobile);

	int selectByMobileAndId(@Param("mobile")String mobile, @Param("id")Long id);

	void saveBatch(List<Order> list);

	List<Order> list(Map<String, Object> map);    

	long count(Map<String, Object> map);

    
}