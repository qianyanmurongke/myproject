package com.course.common.freemarker;

import java.util.List;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class NumberToChineseMethod implements TemplateMethodModelEx {
	@SuppressWarnings("rawtypes")
	public Object exec(List args) throws TemplateModelException {
		String text;
		if (args.size() > 0) {
			TemplateModel arg0 = (TemplateModel) args.get(0);
			text = Freemarkers.getString(arg0, "arg0");
		} else {
			throw new TemplateModelException("arg0 is missing!");
		}

		String result = "";

		String[] s1 = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
		String[] s2 = { "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千" };

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
