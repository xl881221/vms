/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;

public class UBaseFuncAuthDO extends BaseDO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int funcId;
	private String objectId;
	private String objectType;
	private UBaseFuncDO func;
	public UBaseFuncDO getFunc() {
		return func;
	}
	public void setFunc(UBaseFuncDO func) {
		this.func = func;
	}
	public int getFuncId() {
		return funcId;
	}
	public void setFuncId(int funcId) {
		this.funcId = funcId;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

}
