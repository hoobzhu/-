package com.hoob.rs.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cglib.core.Converter;

/**
 * 该控制器用于扩展BeanCopier 的实现对bean的copy
 * 
 * @author faker
 *
 */
public class ConvertExtend implements Converter {
	/**
	 * 在此集合内的字段，将不会被复制，用于排除复制过程中某些不想复制的字段
	 * @param context 目标对象set方法
	 * 
	 */
	private String[] eliminateField;

	@Override
	public Object convert(Object value, Class target, Object context) {
		// 没有需要过滤的字段，默认全部复制
		if (null == eliminateField || eliminateField.length == 0) {
			return value;
		}

		for (int i = 0; i < eliminateField.length; i++) {
			if (String.valueOf(context).equals(eliminateField[i])) {
				return null;
			}
		}
		return value;
	}

	private ConvertExtend(String... eliminate) {
		this.eliminateField = new String[eliminate.length];
		for (int i = 0; i < eliminate.length; i++) {
			this.eliminateField[i] = "set"
					+ eliminate[i].substring(0, 1).toUpperCase()
					+ eliminate[i].substring(1);
		}
	}

	private static Map<String, ConvertExtend> map = new HashMap<String, ConvertExtend>();

	public static synchronized ConvertExtend getIntance(String eliminate) {
		ConvertExtend ce = map.get(eliminate);
		if (ce == null) {
			if (eliminate != null) {
				ce = new ConvertExtend(eliminate.split(","));
				map.put(eliminate, ce);
			}
		}
		return ce;
	}

	public ConvertExtend() {
		this.eliminateField = null;
	}
}
