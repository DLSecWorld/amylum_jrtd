package com.wangku.miaodan.core.service;

import com.wangku.miaodan.core.model.AuthUser;

public interface IAuthUserService {
	
	public AuthUser getDetailByUserAndPass(String username, String password);

	public AuthUser getDetailByTicket(String ticket);

	public void addTicket(Long id, String ticket);

	public void removeTicket(String userName);

}
