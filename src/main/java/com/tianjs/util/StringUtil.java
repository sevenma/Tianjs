package com.tianjs.util;

public class StringUtil {
	/**
	 * 是否为空
	 * @param str
	 * @return
	 */
	public static boolean isEmptyString(String str) {
		return str == null || str.trim().length() == 0;
	}

}
