package fmss.common.ui.controller;

import org.apache.commons.lang.StringUtils;


/**
 * 类说明: 分析系统核心计算函数<br>
 * 创建时间: 2009-2-6 下午12:31:10<br>
 * 
 * @author 杨旭飞<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class Function implements IFunction{

	private AnalyseContext ctx;

	private QueryData queryData; // 当前数据模型

	private String effeciveDataType; // 当前作用域 col\row\cell

	private Row row; // 当前行 仅仅当作用域为 row 的时候采用到

	private CellValue cv;

	public Function(AnalyseContext ctx) {
		this.ctx = ctx;
		this.queryData = (QueryData) ctx.getContext(AnalyseContext.CURR_QUERY_DATA);

		this.effeciveDataType = (String) ctx
				.getContext(AnalyseContext.CURR_EFFECTIVE_DATA_TYPE);

		Object currDataRange = (Object) ctx
				.getContext(AnalyseContext.CURR_EFFECTIVE_DATA);
		if (AnalyseCst.ROW.equals(effeciveDataType)) {
			this.row = (Row) currDataRange;
		} else if (AnalyseCst.CELL.equals(effeciveDataType)) {
			this.cv = (CellValue) currDataRange;
		}
	}

	/**
	 * 根据当前传入的参数 KEY【KEY必须为数值列】 返回当前 上下文 中行对象 中对应的值 该列
	 * 
	 * @author yangxufei 创建时间: 2009-2-6 下午05:15:04<br>
	 * @throws DataTypeException 
	 */
	public final double getNRow(String key)
			throws FormulaCalcException, DataTypeException {
		if (StringUtils.isBlank(key)) {
			throw new FormulaCalcException("公式取值错误！ 当前传入的键值[key]为空！");
		}
		CellValue cv = row.getCvByTarget(key);
		
		if(cv instanceof NumericValue){
			return cv.getNumericValue();
		}else{
			throw new FormulaCalcException("公式取值错误！ 您当前所取值的 [Key="+key+"] 对应的数据类型不是 NumericValue!");
		}
	}
	
	/**
	 * 根据当前传入的参数 KEY【KEY必须为字符列】 返回当前 上下文 中行对象 中对应的值
	 * 
	 * @author yangxufei 创建时间: 2009-2-6 下午05:15:04<br>
	 * @throws DataTypeException 
	 */
	public final String getSRow(String key)
			throws FormulaCalcException, DataTypeException {
		if (StringUtils.isBlank(key)) {
			throw new FormulaCalcException("公式取值错误！ 当前传入的键值[key]为空！");
		}
		CellValue cv = row.getCvByTarget(key);
		
		if(cv instanceof StringValue){
			return cv.getStringValue();
		}else{
			throw new FormulaCalcException("公式取值错误！ 您当前所取值的 [Key="+key+"] 对应的数据类型不是 StringValue!");
		}
	}
}
