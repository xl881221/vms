package fmss.common.ui.controller;


public class DataTypeException extends CalcDataErrorException {

	/**
	 * 
	 * @param dt1
	 *            期望类型
	 * @param dt2
	 *            实际类型
	 */
	public DataTypeException(String dt1, String dt2) {
		super("报表系统错误:请联系相关技术人员 [当前计算数据的数据类型使用错误，不是期望的数据类型: 期望类型=" + dt1
				+ " 实际类型=" + dt2 + "]");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7119869264421677919L;

}
