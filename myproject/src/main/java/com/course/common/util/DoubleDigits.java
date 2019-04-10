package com.course.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 
 * 
 * @author fang yinLang
 * 
 */
public class DoubleDigits {

	public static Double Parse2Digits(Double paramDouble) {
		// double paraDoubleformat = ((int) (paramDouble * 100));
		// return paraDoubleformat / 100;
		BigDecimal bd = new BigDecimal(paramDouble);
		return bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static Double ParseDigits(Double paramDouble) {
		// double paraDoubleformat = ((int) (paramDouble * 100));
		// return paraDoubleformat / 100;
		BigDecimal bd = new BigDecimal(paramDouble);
		return bd.setScale(0, BigDecimal.ROUND_DOWN).doubleValue();
	}

	public static String Double2String(Double paramDouble) {
		if (paramDouble == null)
			return "0.00";
		DecimalFormat df = new DecimalFormat("#,###,##0.00");
		double paraDoubleformat = ((int) (paramDouble * 100));
		String strDoubleformat = df.format(paraDoubleformat / 100);
		return strDoubleformat;

	}

	public static String Double2Digits(Double paramDouble) {
		if (paramDouble == null)
			return "0.00";
		DecimalFormat df = new DecimalFormat("######0.00");
		double paraDoubleformat = ((int) (paramDouble * 100));
		String strDoubleformat = df.format(paraDoubleformat / 100);
		return strDoubleformat;

	}

	public static String Double1Digits(Double paramDouble) {
		if (paramDouble == null)
			return "0";
		DecimalFormat df = new DecimalFormat("######0");
		double paraDoubleformat = ((int) (paramDouble * 100));
		String strDoubleformat = df.format(paraDoubleformat / 100);
		return strDoubleformat;

	}

}
