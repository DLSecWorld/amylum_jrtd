package com.wangku.miaodan.utils;

import java.util.Random;


public class VerifyCodeUtil {
	
	private static final Random ran = new Random(System.currentTimeMillis());
	
	public static final String getVerifyCode() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 6; i++) {
			sb.append(ran.nextInt(10));
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
