package fmss.common.ui.controller;


/**
 * 类说明:<br>
 * 创建时间: 2009-2-9 上午11:56:54<br>
 * 
 * @author 杨旭飞<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class FormulaUtil {

	
	/**
	 * 
	 * 方法说明:根据数据类型和key构造 beanshell 的取值函数<br>
	 * 创建时间: 2009-2-9 下午03:23:01<br>
	 */
	public static String parseBsGetNRow(String key){
		return AnalyseCst.BSH_SPACE+AnalyseCst.DOT+"getNRow(\""+key+"\")";
	}
	/**
	 * 
	 * 方法说明:根据数据类型和key构造 beanshell 的取值函数<br>
	 * 创建时间: 2009-2-9 下午03:23:01<br>
	 */
	public static String parseBsGetSRow(String key){
		return AnalyseCst.BSH_SPACE+AnalyseCst.DOT+"getSRow(\""+key+"\")";
	}
}
