package com.wangku.miaodan.utils;

import java.util.UUID;

public class IDUtils {
	
	public static final String getNumber() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
