package fmss.common.ui.controller;


/**
 * 类说明: 自由查询的表达式计算类<br>
 * 创建时间: 2009-2-6 下午05:55:43<br>
 * 
 * @author 杨旭飞<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class FreeQueryExpression extends Expression {
	
	public FreeQueryExpression(String formula) {
		super();
		this.javaFml = formula;
	}

	protected void setCalcResultContext(AnalyseContext ctx, Object resultObj) {
		ctx.setContext(AnalyseContext.REALTIME_CALC_RESULT, resultObj);
	}
}
