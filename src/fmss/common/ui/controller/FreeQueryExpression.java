package fmss.common.ui.controller;


/**
 * ��˵��: ���ɲ�ѯ�ı��ʽ������<br>
 * ����ʱ��: 2009-2-6 ����05:55:43<br>
 * 
 * @author �����<br>
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
