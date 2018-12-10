package com.wangku.miaodan.core.service;

import com.wangku.miaodan.core.model.Token;

public interface ITokenService {
	
	public boolean addLoginInfo(Token token);
	
	public boolean updateLoginInfo(Token token);
	
	public Token getDetailByCondition(Token token);

}
