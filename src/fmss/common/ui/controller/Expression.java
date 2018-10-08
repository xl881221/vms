package fmss.common.ui.controller;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * 类说明: 分析系统的表达式计算类<br>
 * 创建时间: 2009-2-6 下午05:43:15<br>
 * 
 * @author 杨旭飞<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public abstract class Expression {

	/**
	 * 核心公式
	 */
	protected String javaFml;

	/**
	 * 传入上下文 运行核心计算 方法说明:<br>
	 * 创建时间: 2009-2-6 下午05:50:49<br>
	 * @throws Exception 
	 */
	public void calc(AnalyseContext ctx) throws Exception {

		Object resultObj = null;
		try {
			resultObj = initEngineAndExecute(ctx);
		} catch (EvalError e) {
			e.printStackTrace();
		}

		if (this.javaFml != null && resultObj == null) {
			String errorInfo = "当前公式计算没有得到任何返回值！  请检查当前公式的正确性" + this.javaFml;
			throw new Exception(errorInfo);
		}

		setCalcResultContext(ctx, resultObj);
	}

	protected abstract void setCalcResultContext(AnalyseContext ctx,
			Object resultObj);

	/**
	 * 根据上下文 处理公式运算 方法说明:<br>
	 * 创建时间: 2009-2-6 下午05:51:17<br>
	 */
	private Object initEngineAndExecute(AnalyseContext ctx) throws EvalError {

		IFunction runtimeFunc = new Function(ctx);

		Interpreter i = new Interpreter();

		i.set("func", runtimeFunc);

		Object evalObject = null;

		evalObject = i.eval(this.javaFml);

		return evalObject;
	}
}
