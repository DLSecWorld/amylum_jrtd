package com.wangku.miaodan.core.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.wangku.miaodan.core.model.AuthUser;

@Repository
public interface AuthUserMapper {
	
	public AuthUser getDetailByNameAndPass(@Param("username")String username, @Param("password")String password);

	public AuthUser getDetailByTicket(@Param("ticket")String ticket);

	public void addTicket(@Param("id")Long id, @Param("ticket")String ticket);

	public void removeTicket(@Param("username")String userName);
    
}