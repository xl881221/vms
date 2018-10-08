/**
 * Created on 2006-3-23
 * Created by Sunteya
 */
package fmss.common.ui.controller;

import org.apache.commons.lang.math.NumberUtils;

/**
 * description: crms 数据处理工具类.extends apache.commons.lang.math.NumberUtils;
 */
public class NumberUtil extends NumberUtils {
	public static int intValue(Integer i) {
		return (i == null) ? 0 : i.intValue();
	}

	public static long longValue(Long l) {
		return (l == null) ? 0 : l.longValue();
	}

	public static double round(double v, int num) {
		return Double.parseDouble(StringUtil.doubleToString(v, num));
		//return org.apache.commons.lang.math.NumberUtils
	}
}
