package fmss.common.ui.controller;

/**
 * description: calc data ��������ʵҵ������.
 */
public class CalcDataErrorException extends ReportEngineCheckException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 617276138503366608L;

	public CalcDataErrorException(String sceneInfo) {
		super("���������д���:" + sceneInfo);
	}
}
