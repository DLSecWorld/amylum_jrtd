package com.wangku.miaodan.utils;

import java.util.Objects;

public class Strings {
	
	public static boolean isNullOrEmpty(Object str) {
		return Objects.isNull(str) || String.valueOf(str).isEmpty();
	}

}
