package com.course.common.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.course.common.security.Digests;

public class CheckAPISecurity {

	public static String API_KEY = "shanyu-tech!@#$%^&*()";

	/**
	 * 使用 Map按key进行排序
	 * 
	 * @param map
	 * @return
	 */
	public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}

		Map<String, Object> sortMap = new TreeMap<String, Object>(new MapKeyComparator());

		sortMap.putAll(map);

		return sortMap;
	}

	/**
	 * 必须包含“token ”签名
	 * 
	 * @param postdata
	 * @return
	 */
	public static boolean CheckSecurity(Map<String, Object> postdata) {
		if (!postdata.containsKey("signature"))
			return false;

		Map<String, Object> resultMap = sortMapByKey(postdata);
		StringBuilder params = new StringBuilder();
		for (Map.Entry<String, Object> map : resultMap.entrySet()) {
			if (map.getKey().equals("signature"))
				continue;
			params.append(map.getKey() + "=" + (map.getValue() == null ? "" : map.getValue().toString()) + "&");
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHH");
		params.append("time=" + sf.format(new Date()) + "&");
		params.append("key=" + API_KEY);
		InputStream data;
		String signinfo = "";
		try {
			data = new ByteArrayInputStream(params.toString().getBytes("UTF-8"));
			signinfo = Encodes.encodeHex(Digests.md5(data)).toLowerCase();
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		if (signinfo.equals(resultMap.get("signature").toString().toLowerCase()))
			return true;

		return false;
	}
}

class MapKeyComparator implements Comparator<String> {

	@Override
	public int compare(String str1, String str2) {

		return str1.compareTo(str2);
	}
}