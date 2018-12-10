package com.wangku.miaodan.utils;

public class VerifyCode {
	
	private String code;
	
	private Long passedTime;
	
	private static final Long TIME_5_MINUTE = 300000L;
	
	public VerifyCode(String code) {
		this.code = code;
		this.passedTime = System.currentTimeMillis() + TIME_5_MINUTE;
	}
	
	public boolean isValid(String code) {
		if (this.code.equals(code) && System.currentTimeMillis() <= passedTime) {
			return true;
		} else {
			return false;
		}
	}
	
}
