package fmss.common.ui.controller;


/**
 * ��˵��: ����������ӿ�<br>
 * ����ʱ��: 2009-2-9 ����10:06:52<br>
 * 
 * @author �����<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public interface IFunction {
	/**
	 * ���ݵ�ǰ����Ĳ��� KEY��KEY����Ϊ��ֵ�С� ���ص�ǰ ������ ���ж��� �ж�Ӧ��ֵ ����
	 * 
	 * @author yangxufei ����ʱ��: 2009-2-6 ����05:15:04<br>
	 * @throws DataTypeException 
	 */
	public abstract double getNRow(String key) throws FormulaCalcException, DataTypeException;
	/**
	 * ���ݵ�ǰ����Ĳ��� KEY��KEY����Ϊ�ַ��С� ���ص�ǰ ������ ���ж��� �ж�Ӧ��ֵ
	 * 
	 * @author yangxufei ����ʱ��: 2009-2-6 ����05:15:04<br>
	 * @throws DataTypeException 
	 */
	public abstract String getSRow(String key)throws FormulaCalcException, DataTypeException ;
}
