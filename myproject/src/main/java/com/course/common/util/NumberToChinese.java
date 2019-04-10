package com.course.common.util;

public abstract class NumberToChinese {

	public static String getChinese(Integer number) {
		if(number == null)
			return "";
		String result = "";

		String[] s1 = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
		String[] s2 = { "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千" };
		String text = Integer.toString(number);
		int n = text.length();
		for (int i = 0; i < n; i++) {

			int num = text.charAt(i) - '0';

			if (i != n - 1 && num != 0) {
				result += s1[num] + s2[n - 2 - i];
			} else {
				result += s1[num];
			}
		}

		return result;
	}
}
