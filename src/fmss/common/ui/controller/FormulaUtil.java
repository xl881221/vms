package fmss.common.ui.controller;


/**
 * ��˵��:<br>
 * ����ʱ��: 2009-2-9 ����11:56:54<br>
 * 
 * @author �����<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class FormulaUtil {

	
	/**
	 * 
	 * ����˵��:�����������ͺ�key���� beanshell ��ȡֵ����<br>
	 * ����ʱ��: 2009-2-9 ����03:23:01<br>
	 */
	public static String parseBsGetNRow(String key){
		return AnalyseCst.BSH_SPACE+AnalyseCst.DOT+"getNRow(\""+key+"\")";
	}
	/**
	 * 
	 * ����˵��:�����������ͺ�key���� beanshell ��ȡֵ����<br>
	 * ����ʱ��: 2009-2-9 ����03:23:01<br>
	 */
	public static String parseBsGetSRow(String key){
		return AnalyseCst.BSH_SPACE+AnalyseCst.DOT+"getSRow(\""+key+"\")";
	}
}
