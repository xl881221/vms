package fmss.common.ui.controller;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;


/**
 * 类说明:<br>
 * 创建时间: 2008-9-3 上午11:04:02<br>
 * 
 * @author 杨旭飞<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class AnalyzingUtil {
	public static String getDialect(String tableName, String Type)
			throws AnalyzingException {
		if(tableName.length()>3)
			return Type + tableName.substring(0, 3);
		else
			return Type + tableName+"01";
		
//		if (tableName.length() < 3)
//			throw new AnalyzingException("数据库表名定义不能小于4个英文字符");
//		return Type + tableName.substring(0, 3);
	}

	public static boolean isRefreshForm(HttpSession session, String schemeCode,
			String formName) throws AnalyzingException {
		if (StringUtils.isNotBlank(schemeCode)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean getOddOrEven(int i) {
		return i % 2 == 0 ? true : false;
	}

	public static List buildRule(String str, String type) {
		String[] temp = str.split(";");
		List list = new LinkedList();
		if (AnalyseCst.NUMBER.equals(type)) {
			for (int i = 0; i < temp.length; i++) {
				String s = temp[i];
				String[] temp1 = s.split(",");
				NumericRule nr = new NumericRule();
				String sv = temp1[0];
				String ev = temp1[1];
				if(StringUtils.isNotBlank(sv)){
					nr.setStartValue((Double.valueOf(sv)).doubleValue());
				}
				if(StringUtils.isNotBlank(ev)){
					nr.setEndValue((Double.valueOf(ev)).doubleValue());
				}

				nr.setImg(temp1[2]);
				list.add(nr);
			}
		}
		return list;
	}
}
