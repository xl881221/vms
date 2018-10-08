package fmss.common.ui.controller;

/**
 * description: calc data 不符合现实业务意义.
 */
public class CalcDataErrorException extends ReportEngineCheckException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 617276138503366608L;

	public CalcDataErrorException(String sceneInfo) {
		super("计算数据有错误:" + sceneInfo);
	}
}
