package com.wangku.miaodan.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangku.miaodan.core.dao.TokenMapper;
import com.wangku.miaodan.core.model.Token;
import com.wangku.miaodan.core.service.ITokenService;
import com.wangku.miaodan.utils.Strings;


@Service("tokenService")
public class TokenServiceImpl implements ITokenService {
	
	@Autowired
	private TokenMapper tokenMapper;

	@Override
	public boolean addLoginInfo(Token token) {
		if (Strings.isNullOrEmpty(token.getVerifyCode()) && token.getVerifyInvalidTime() <= 0) {
			return false;
		}
		tokenMapper.insert(token);
		return true;
	}

	@Override
	public boolean updateLoginInfo(Token token) {
		tokenMapper.update(token);
		return true;
	}

	@Override
	public Token getDetailByCondition(Token token) {
		return tokenMapper.getDetailByToken(token);
	}

}
