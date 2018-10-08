package fmss.common.ui.controller;

import java.util.HashMap;
import java.util.Map;

/**
 * 类说明: 操作类<br>
 * 创建时间: 2009-1-9 上午11:47:18<br>
 * 
 * @author 杨旭飞<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class Operation {
	
	public Operation(String operationKey,Map operationParamMap){
		this.operationKey = operationKey;
		this.operationParamMap = operationParamMap;
	}
	/**
	 * 操作KEY
	 */
	private String operationKey;
	/**
	 * 操作的参数 map
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
