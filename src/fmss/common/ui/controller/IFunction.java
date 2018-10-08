package fmss.common.ui.controller;


/**
 * 类说明: 公共函数类接口<br>
 * 创建时间: 2009-2-9 上午10:06:52<br>
 * 
 * @author 杨旭飞<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public interface IFunction {
	/**
	 * 根据当前传入的参数 KEY【KEY必须为数值列】 返回当前 上下文 中行对象 中对应的值 该列
	 * 
	 * @author yangxufei 创建时间: 2009-2-6 下午05:15:04<br>
	 * @throws DataTypeException 
	 */
	public abstract double getNRow(String key) throws FormulaCalcException, DataTypeException;
	/**
	 * 根据当前传入的参数 KEY【KEY必须为字符列】 返回当前 上下文 中行对象 中对应的值
	 * 
	 * @author yangxufei 创建时间: 2009-2-6 下午05:15:04<br>
	 * @throws DataTypeException 
	 */
	public abstract String getSRow(String key)throws FormulaCalcException, DataTypeException ;
}
