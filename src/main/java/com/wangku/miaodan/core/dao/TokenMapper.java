package com.wangku.miaodan.core.dao;

import org.springframework.stereotype.Repository;

import com.wangku.miaodan.core.model.Token;

@Repository
public interface TokenMapper {

	int insert(Token token);
	
	int update(Token token);
	
	Token getDetailByToken(Token token);
	
}