package fmss.common.ui.controller;


public class DataTypeException extends CalcDataErrorException {

	/**
	 * 
	 * @param dt1
	 *            ��������
	 * @param dt2
	 *            ʵ������
	 */
	public DataTypeException(String dt1, String dt2) {
		super("����ϵͳ����:����ϵ��ؼ�����Ա [��ǰ�������ݵ���������ʹ�ô��󣬲�����������������: ��������=" + dt1
				+ " ʵ������=" + dt2 + "]");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7119869264421677919L;

}
