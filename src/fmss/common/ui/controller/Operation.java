package fmss.common.ui.controller;

import java.util.HashMap;
import java.util.Map;

/**
 * ��˵��: ������<br>
 * ����ʱ��: 2009-1-9 ����11:47:18<br>
 * 
 * @author �����<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class Operation {
	
	public Operation(String operationKey,Map operationParamMap){
		this.operationKey = operationKey;
		this.operationParamMap = operationParamMap;
	}
	/**
	 * ����KEY
	 */
	private String operationKey;
	/**
	 * �����Ĳ��� map
	 */
	private Map operationParamMap = new HashMap();

	public String getOperationKey() {
		return operationKey;
	}

	public void setOperationKey(String operationKey) {
		this.operationKey = operationKey;
	}

	public Map getOperationParamMap() {
		return operationParamMap;
	}

	public void setOperationParamMap(Map operationParamMap) {
		this.operationParamMap = operationParamMap;
	}
}
