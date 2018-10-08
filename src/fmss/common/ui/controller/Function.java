package fmss.common.ui.controller;

import org.apache.commons.lang.StringUtils;


/**
 * ��˵��: ����ϵͳ���ļ��㺯��<br>
 * ����ʱ��: 2009-2-6 ����12:31:10<br>
 * 
 * @author �����<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class Function implements IFunction{

	private AnalyseContext ctx;

	private QueryData queryData; // ��ǰ����ģ��

	private String effeciveDataType; // ��ǰ������ col\row\cell

	private Row row; // ��ǰ�� ������������Ϊ row ��ʱ����õ�

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
	 * ���ݵ�ǰ����Ĳ��� KEY��KEY����Ϊ��ֵ�С� ���ص�ǰ ������ ���ж��� �ж�Ӧ��ֵ ����
	 * 
	 * @author yangxufei ����ʱ��: 2009-2-6 ����05:15:04<br>
	 * @throws DataTypeException 
	 */
	public final double getNRow(String key)
			throws FormulaCalcException, DataTypeException {
		if (StringUtils.isBlank(key)) {
			throw new FormulaCalcException("��ʽȡֵ���� ��ǰ����ļ�ֵ[key]Ϊ�գ�");
		}
		CellValue cv = row.getCvByTarget(key);
		
		if(cv instanceof NumericValue){
			return cv.getNumericValue();
		}else{
			throw new FormulaCalcException("��ʽȡֵ���� ����ǰ��ȡֵ�� [Key="+key+"] ��Ӧ���������Ͳ��� NumericValue!");
		}
	}
	
	/**
	 * ���ݵ�ǰ����Ĳ��� KEY��KEY����Ϊ�ַ��С� ���ص�ǰ ������ ���ж��� �ж�Ӧ��ֵ
	 * 
	 * @author yangxufei ����ʱ��: 2009-2-6 ����05:15:04<br>
	 * @throws DataTypeException 
	 */
	public final String getSRow(String key)
			throws FormulaCalcException, DataTypeException {
		if (StringUtils.isBlank(key)) {
			throw new FormulaCalcException("��ʽȡֵ���� ��ǰ����ļ�ֵ[key]Ϊ�գ�");
		}
		CellValue cv = row.getCvByTarget(key);
		
		if(cv instanceof StringValue){
			return cv.getStringValue();
		}else{
			throw new FormulaCalcException("��ʽȡֵ���� ����ǰ��ȡֵ�� [Key="+key+"] ��Ӧ���������Ͳ��� StringValue!");
		}
	}
}
