package fmss.common.ui.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;



/**
 * 类说明:<br>
 * 创建时间: 2008-11-27 上午09:55:45<br>
 * 
 * @author 杨旭飞<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class FreeQueryUtil {
	
	public static String processStr(String str, FreeQueryClmForm freeQueryClmForm) {
		String[] array = str.split(",");
		StringBuffer resSbf = new StringBuffer();
		String tableName = freeQueryClmForm.getTableDialectName() + "."
				+ freeQueryClmForm.getTableClmName();
		String clmType = freeQueryClmForm.getTableClmType();
		List list = new LinkedList();
		for (int i = 0; i < array.length; i++) {
			String temp = array[i];
			temp = temp.replaceAll("[\\(|\\)]", "");
			String regex = "and|or";
			List tempL = new ArrayList();
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(temp);
			while (matcher.find()) {
				String tempStr = matcher.group();
				tempL.add(tempStr);
			}
			Iterator iterator = tempL.iterator();
			String[] arr1 = temp.split(regex);
			StringBuffer sbf = new StringBuffer();
			for (int j = 0; j < arr1.length; j++) {
				String res = buildByValue(arr1[j], tableName, clmType);
				if (iterator.hasNext()) {
					sbf.append(res).append(" ")
							.append((String) iterator.next()).append(" ");
				} else {
					sbf.append(res);
				}

			}
			list.add("(" + sbf.toString() + ")");
		}

		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			String temp = (String) iterator.next();
			resSbf.append(temp);
			if (iterator.hasNext())
				resSbf.append(" ").append("or").append(" ");
		}
		return resSbf.toString();
	}

	private static String buildByValue(String str, String tableName, String type) {
		String res = "";
		if (!StringUtils.containsNone(str, new char[] { '>', '=', '<' })) {
			res = tableName + " " + buildByClmType(str.trim(), type);
		} else {
			res = tableName + " " + "like" + "  "
					+ buildByClmType(str.trim(), type).trim() ;
		}
		return res;
	}
	/**
	 * 
	 * 方法说明:根据所制定的CELLVALUE List 转化成以 | 分隔的 key <br>
	 * 创建时间: 2009-1-16 下午02:36:18<br>
	 */
	public static String bulidKeyByThisList(List list){
		StringBuffer sbf = new StringBuffer();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			String itemKey = ((CellValue)iterator.next()).toValueString();
			sbf.append(itemKey);
			if(iterator.hasNext()){
				sbf.append("|");
			}
		}
		return sbf.toString();
	}
	
	private static String buildByClmType(String str, String type) {
		String regex = "<|>|=";
		String value = str.replaceAll(regex, "").trim();
		String mark = str.replaceAll(value, "").trim();
		if ("string".equals(type) || "date".equals(type)) {
			if( "".equals(mark) ){
				value = "'%" + value + "%'";
			}
			else {
				value = "'" + value + "'";
			}
		}
		return mark.trim() + " " + value.trim();
	}

}
