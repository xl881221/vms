package fmss.common.ui.controller;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * ��˵��: ����ϵͳ�ı��ʽ������<br>
 * ����ʱ��: 2009-2-6 ����05:43:15<br>
 * 
 * @author �����<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public abstract class Expression {

	/**
	 * ���Ĺ�ʽ
	 */
	protected String javaFml;

	/**
	 * ���������� ���к��ļ��� ����˵��:<br>
	 * ����ʱ��: 2009-2-6 ����05:50:49<br>
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
			String errorInfo = "��ǰ��ʽ����û�еõ��κη���ֵ��  ���鵱ǰ��ʽ����ȷ��" + this.javaFml;
			throw new Exception(errorInfo);
		}

		setCalcResultContext(ctx, resultObj);
	}

	protected abstract void setCalcResultContext(AnalyseContext ctx,
			Object resultObj);

	/**
	 * ���������� ����ʽ���� ����˵��:<br>
	 * ����ʱ��: 2009-2-6 ����05:51:17<br>
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
