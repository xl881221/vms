package fmss.common.ui.controller;

import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;


/**
 * 系统统一 字符串处理工具类,extends org.apache.commons.lang.StringUtils
 */
public final class StringUtil extends StringUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(StringUtil.class);
	
	/**
	 * 功能说明：改变String的首字母大小写<br>
	 * 创建者：李涛<br>
	 * 创建时间：2006-3-16<br>
	 * 
	 * @param str
	 * @param capitalize
	 *            true: 变大写 false:变小写
	 * @return
	 */
	public static String changeFirstCharacterCase(String str, boolean capitalize) {
		if (str == null || str.length() == 0) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str.length());
		if (capitalize) {
			buf.append(Character.toUpperCase(str.charAt(0)));
		} else {
			buf.append(Character.toLowerCase(str.charAt(0)));
		}
		buf.append(str.substring(1));
		return buf.toString();
	}
	
	public static boolean checkNumber(String s) {
		if (s == null)
			return false;
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException numberformatexception) {
			return false;
		}
	}

	/**
	 * x<=y 时能正常显示
	 */
	public static String getPercent(String x, String y) {
		int round = 2;
		double a = Double.valueOf(x).doubleValue();
		double b = Double.valueOf(y).doubleValue();
		double z = (a / b) * 100;
		String temp = String.valueOf(z);
		if (temp.endsWith("0")) {
			round--;
			temp = temp.substring(0, temp.length() - 1);
		}
		if (temp.endsWith(".")) {
			round--;
		}
		String result = StringUtil.doubleToString(z, round) + "%";
		return result;
	}

	public static String checkStr(String s) {
		if (IsEmptyStr(s)) {
			return null;
		} else {
			return "'" + s + "'";
		}
	}

	public static String clearSpace(String s) {
		String s1 = "";
		if (IsEmptyStr(s))
			return s;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c != ' ' && c != '\t' && c != '\n')
				s1 = s1 + c;
		}

		return s1;
	}

	public static int count_char(String s, char c) {
		int j = 0;
		if (s == null)
			return 0;
		for (int i = 0; i < s.length(); i++)
			if (s.charAt(i) == c)
				j++;
		return j;
	}

	public static int darkFindField(String s, String as[]) {
		s = s.toUpperCase();
		String as1[] = new String[as.length];
		for (int i = 0; i < as.length; i++)
			as1[i] = as[i].toUpperCase();

		return findField(s, as1);
	}

	public static String doubleToString(double d, int i) {

		BigDecimal bd = new BigDecimal(Double.toString(d));
		return bd.setScale(i, BigDecimal.ROUND_HALF_UP).toString();
	}

	public static String doubleToStringNotEndWithZero(double d, int i) {
		String s = doubleToString(d, i);
		if (i > 0) {
			s = s.replaceAll("0+$", "");
			if (s.charAt(s.length() - 1) == '.') {
				s = s.substring(0, s.length() - 1);
			}
		}
		return s;
	}

	public static String doubleToStringWithTh(double d, int i) {
		String ss = doubleToString(d, i);
		int indexOfPoint = ss.indexOf(".");
		String s = "#,##0";
		DecimalFormat decimalformat = (DecimalFormat) NumberFormat
				.getInstance();
		decimalformat.applyPattern(s);
		if (indexOfPoint == -1) { // 是整数
			return decimalformat.format(d);
		} else {
			String intString = ss.substring(0, indexOfPoint); // 整数部分
			String smallString = ss.substring(indexOfPoint + 1); // 小数部分
			return decimalformat.format(Long.parseLong(intString)) + "."
					+ smallString;
		}
	}

	public static String[] explainTagStr(String s, char c) {
		if (IsEmptyStr(s))
			return null;
		if (s.charAt(0) == c)
			s = s.substring(1);
		int i = s.length();
		if (s.charAt(i - 1) == c)
			s = s.substring(0, i - 1);
		String as[] = null;
		String s1 = "";
		int k = count_char(s, c);
		if (k == 0) {
			as = new String[1];
			as[0] = s.trim();
			return as;
		}
		k++;
		as = new String[k];
		k = 0;
		s = s + c;
		for (int j = 0; j < s.length(); j++)
			if (s.charAt(j) == c) {
				s1 = s1.trim();
				as[k] = s1;
				s1 = "";
				k++;
			} else {
				s1 = s1 + s.charAt(j);
			}

		return as;
	}

	public static String[][] explainTagStr(String s, char c, char c1) {
		if (s.charAt(0) == c)
			s = s.substring(1);
		int i = s.length();
		if (s.charAt(i - 1) == c)
			s = s.substring(0, i - 1);
		String as[] = explainTagStr(s, c);
		String as1[][] = new String[as.length][2];
		for (int j = 0; j < as.length; j++) {
			String as2[] = explainTagStr(as[j], c1);
			if (as2 == null)
				as1[j] = new String[2];
			else
				as1[j] = as2;
		}
		return as1;
	}

	static public String[][] explainTagStr(String str, String sp1, char sp2) {
		int fromIndex = 0;
		ArrayList a1 = new ArrayList();
		while (true) {
			int pos = str.indexOf(" " + sp1 + " ", fromIndex);
			if (pos == -1)
				break;
			a1.add(replaceStr(str.substring(fromIndex, pos), " ", ""));
			fromIndex = pos + sp1.length() + 2;
		}
		String last = replaceStr(str.substring(fromIndex), " ", "");
		if (!IsEmptyStr(last))
			a1.add(last);
		String result[][] = new String[a1.size()][2];
		for (int i = 0; i < a1.size(); i++) {
			String stmp = (String) a1.get(i);
			int pos = stmp.indexOf(sp2);
			result[i][0] = stmp.substring(0, pos);
			result[i][1] = stmp.substring(pos + 1);
		}
		return result;
	}

	public static int findField(String s, String as[]) {
		if (as == null)
			return -1;
		for (int i = 0; i < as.length; i++)
			if (as[i].equals(s))
				return i;

		return -1;
	}

	public static String getCurrentDate() {
		return getCurrentDate(0);
	}

	public static String getCurrentDate(int i) {
		Calendar calendar = Calendar.getInstance();
		int j = calendar.get(1);
		int k = calendar.get(2) + 1;
		int l = calendar.get(5);
		String s = "" + j;
		String s1 = "" + k;
		String s2 = "" + l;
		if (k < 10)
			s1 = "0" + k;
		if (l < 10)
			s2 = "0" + l;
		switch (i) {
		case 0: // '\0'
		default:
			return s + "-" + s1 + "-" + s2;

		case 1: // '\001'
			return s + s1 + s2;

		case 2: // '\002'
			return null;

		case 3: // '\003'
			return null;
		}
	}


	public static int getIntValue(String value) {
		int v;
		try {
			v = Integer.parseInt(value);
		} catch (Exception e) {
			v = 0;
		}
		return v;
	}

	public static String getNumericString(double d) {
		long l = new Double(d).longValue();
		if (l == d) {
			return String.valueOf(l);
		} else {
			return doubleToString(d, 2);
		}
	}

	public static String getNumericString(double d, int round) {
		long l = new Double(d).longValue();
		if (l == d) {
			return String.valueOf(l);
		} else {
			return doubleToString(d, round);
		}
	}

	/**
	 * Finds first index of a substring in the given source string with ignored
	 * case. This seems to be the fastest way doing this, with common string
	 * length and content (of course, with no use of Boyer-Mayer type of
	 * algorithms). Other implementations are slower: getting char array frist,
	 * lowercasing the source string, using String.regionMatch etc.
	 * 
	 * @param src
	 *            source string for examination
	 * @param subS
	 *            substring to find
	 * @param startIndex
	 *            starting index from where search begins
	 * @return index of founded substring or -1 if substring is not found
	 */
	public static int indexOfIgnoreCase(String src, String subS, int startIndex) {
		String sub = subS.toLowerCase();
		int sublen = sub.length();
		int total = src.length() - sublen + 1;

		for (int i = startIndex; i < total; i++) {
			int j = 0;

			while (j < sublen) {
				char source = Character.toLowerCase(src.charAt(i + j));

				if (sub.charAt(j) != source) {
					break;
				}

				j++;
			}

			if (j == sublen) {
				return i;
			}
		}

		return -1;
	}

	public static boolean IsEmptyStr(String s) {
		int i = 0;
		if (s == null)
			return true;
		if (s.length() == 0)
			return true;
		i = s.length();
		for (int j = 0; j < i; j++) {
			char c = s.charAt(j);
			if (c != '\t' && c != '\n' && c != '\r' && c != ' ')
				return false;
		}
		return true;
	}

	public static double java_truncate(double dd, int posi) {
		return Double.parseDouble(StringUtil.runTruncat(dd + "", posi));
	}

	public static String LeftTrim(String s) {
		int i = 0;
		if (s == null)
			return null;
		for (i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c != '\t' && c != '\n' && c != '\r' && c != ' ')
				break;
		}

		return s.substring(i);
	}

	public static String newDate(String s, int i, int j, int k)
			throws Exception {
		// if(!checkDate(s))
		// throw new Exception("[" + s + "] is not correct date format,please
		// use 'YYYY-MM-DD' format");
		String s1 = null;
		s1 = s.substring(0, 4);
		int l = Integer.parseInt(s1);
		s1 = s.substring(5, 7);
		int i1 = Integer.parseInt(s1);
		s1 = s.substring(8);
		int j1 = Integer.parseInt(s1);
		Calendar calendar = Calendar.getInstance();
		calendar.set(l, i1 - 1, j1);
		calendar.add(1, i);
		calendar.add(2, j);
		calendar.add(5, k);
		i = calendar.get(1);
		j = calendar.get(2) + 1;
		k = calendar.get(5);
		String s2 = "" + i;
		String s3 = "" + j;
		String s4 = "" + k;
		if (j < 10)
			s3 = "0" + j;
		if (k < 10)
			s4 = "0" + k;
		return s2 + "-" + s3 + "-" + s4;
	}

	public static String replaceStr(String s, int i, char c) {
		return s.substring(0, i) + c + s.substring(i + 1);
	}

	public static String replaceStr(String s, String s1, String s2) {
		if (IsEmptyStr(s))
			return s;
		int i = s1.length();
		StringBuffer stringbuffer = new StringBuffer();
		String s3 = s;
		do {
			int j = s3.indexOf(s1);
			if (j >= 0) {
				stringbuffer = stringbuffer.append(s3.substring(0, j)).append(
						s2);
				s3 = s3.substring(j + i);
			} else {
				return stringbuffer.append(s3).toString();
			}
		} while (true);
	}

	public static String RightTrim(String s) {
		int i = 0;
		if (s == null)
			return "";
		for (i = s.length() - 1; i >= 0; i--) {
			char c = s.charAt(i);
			if (c != '\t' && c != '\n' && c != '\r' && c != ' ')
				break;
		}
		return s.substring(0, i + 1);
	}

	public static String runTruncat(String s, int i) {
		int j = s.indexOf(46);
		if (j < 0)
			return s;
		String s1 = s.substring(j + 1);
		if (s1.length() <= i)
			return s;
		else
			return s.substring(0, j + i + 1);
	}

	/**
	 * 
	 * 从一个表变量中获取以关键字开头的数据 参数 传递过来的表变量 要取值的表关键字 返回 返回执行后得到的数据
	 */

	public static String splitTableVar(String as_tablevar, String as_keyword) {
		String pattern = as_keyword + "\\[([^\\[]*)\\]";
		Pattern patt = Pattern.compile(pattern);
		Matcher matcher = patt.matcher(as_tablevar);
		if (matcher.find()) {
			String tag = matcher.group(1);
			return tag;
		}
		return "";
	}

	public static double stringToDouble(String s) {
		if (!checkNumber(s))
			return 0.0D;
		else
			return Double.parseDouble(s);
	}

	public static String strToDate(String s) {
		String s1;
		String s2;
		s1 = "yyyymmdd";
		s2 = "yymmdd";
		if (s.length() == s1.length())
			return s.substring(0, 4) + "-" + s.substring(4, 6) + "-"
					+ s.substring(6);
		if (s.length() == s2.length())
			return s.substring(0, 2) + "-" + s.substring(2, 4) + "-"
					+ s.substring(4);
		return s;
	}

	public static String trim(String str) {
		if (null == str) {
			return null;
		} else {
			return str.trim();
		}

	}

	public static double truncate(double d, int i) {
		String pattern = "\\d*.\\d{X}";
		pattern = pattern.replaceAll("X", String.valueOf(i));
		Pattern patt = Pattern.compile(pattern);
		Matcher matcher = patt.matcher(String.valueOf(d));
		if (matcher.find()) {
			String value = matcher.group(0);
			return Double.parseDouble(value);
		}
		return d;
	}


	public static String getErrorStackString(Throwable e) {
		java.io.CharArrayWriter cw = new java.io.CharArrayWriter();
		java.io.PrintWriter pw = new java.io.PrintWriter(cw, true);
		e.printStackTrace(pw);
		return cw.toString();
	}

	/**
	 * 功能说明: 填充动态SQL语句的参数，参数以#号标示<br>
	 * 创建者: 刘岩松<br>
	 * 创建时间: 2007-6-29 上午11:30:45<br>
	 * 
	 * @param sqlStr
	 *            带有#号变量的sql语句
	 * @param map
	 *            变量值的map
	 * @return
	 */
	public static String fillSqlVariable(String sqlStr, Map map) {

		Iterator iter = map.keySet().iterator();

		while (iter.hasNext()) {

			String key = iter.next().toString();
			String patternStr = "#" + key + "#";
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(sqlStr);

			String value = null;
			try {
				value = map.get(key).toString();
			} catch (NullPointerException e) {
				value = "";
			}
			sqlStr = matcher.replaceAll(value);
		}
		return sqlStr;
	}

	public static String stringList2Query(List stringList) {
		String result = "";
		for (int i = 0; i < stringList.size(); i++) {
			result += "\'" + (String) stringList.get(i) + "\',";
		}
		return result.endsWith(",") ? result.substring(0, result.length() - 1)
				: result;
	}

	public static String string2Query(String param) {
		return "\'" + param.replaceAll("'", "''") + "\'";
	}

	public static String stringArray2StringJoinWithUserDefine(
			String[] stringArray, String joinString) {
		String result = "";
		for (int i = 0; i < stringArray.length; i++) {
			result += stringArray[i] + ",";
		}
		return result.endsWith(",") ? result.substring(0, result.length() - 1)
				: result;
	}

	public static String[] generateCode(String bank_id, int i) {
		String[] result = new String[2];
		String GbankGroupCode = "G_" + bank_id;
		String GSbankGroupCode = "GS_" + bank_id;
		result[0] = GbankGroupCode;
		result[1] = GSbankGroupCode;
		return result;
	}



	/**
	 * 通过指定列 输出html表格
	 * 
	 * @return
	 */
	public static String outPutterTableByHTML(List dataList, int cols) {
		StringBuffer sbf = new StringBuffer();
		if (dataList.size() <= 0) {
			throw new RuntimeException("输出表格样式是时出错" + dataList
					+ " 中没有可用数据!");
		} else {
			sbf.append("<table>");
			for (int i = 1; i < dataList.size() + 1; i++) {
				String str = (String) dataList.get(i - 1);

				if (i == 1) {
					sbf.append("<tr>");
				}
				sbf.append("<td>" + str + "</td>");
				if (i % cols <= 0) {
					if (i == dataList.size()) {
						sbf.append("</tr>");
					} else {
						sbf.append("</tr><tr>");
					}
				}
			}
			sbf.append("</table>");
		}
		return sbf.toString();
	}

	public static String stringArray2Query(String[] stringArray) {
		String result = "";
		for (int i = 0; i < stringArray.length; i++) {
			result += "\'" + stringArray[i] + "\',";
		}
		return result.endsWith(",") ? result.substring(0, result.length() - 1)
				: result;
	}

	/**
	 * 通过指定列 输出html表格
	 * 
	 * @return
	 */
	public static String outPutterTableByTxt(List dataList, int cols) {
		StringBuffer sbf = new StringBuffer();
		if (dataList.size() <= 0) {
			throw new RuntimeException("输出表格样式是时出错" + dataList
					+ " 中没有可用数据!");
		} else {
			for (int i = 1; i < dataList.size() + 1; i++) {
				String str = (String) dataList.get(i - 1);

				if (i == 1) {
					sbf.append("\r\n");
				}
				sbf.append(str);
				if (i % cols <= 0) {
					if (i == dataList.size()) {
					} else {
						sbf.append("\r\n");
					}
				}
			}
		}
		return sbf.toString();
	}

	public static String getDeletedStringNewLineEnterSign(String willString) {
		// only trim start and end's whitespace.
		String finalStr = willString.trim();
		if (willString.indexOf("\n") != -1) {
			finalStr = StringUtil.replace(finalStr, "\n", "");
		}
		if (willString.indexOf("\r") != -1) {
			finalStr = StringUtil.replace(finalStr, "\r", "");
		}
		if (willString.indexOf("\t") != -1) {
			finalStr = StringUtil.replace(finalStr, "\t", "");
		}
		return finalStr;
	}

	
	public static String get_percent(CellValue cv, int digit, String v) {
		try {
			return StringUtil.doubleToString(cv.getNumericValue() * 100,
					digit)
					+ "%";
		} catch (DataTypeException e) {
			return v;
		}
	}
	
	public static String get_percent(double value, int digit, String v) {
			return StringUtil.doubleToString(value * 100,
					digit)+ "%";
	}
	public static void main(String[] args) {
		CellValue cv = new NumericValue(0.78);
		
		System.out.println(StringUtil.get_percent(cv, 3,"0.00%"));

	}

}
