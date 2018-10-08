/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;
import java.util.Set;

public class UBaseFuncDO extends BaseDO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int funcId;
	private String funcURL;
	private String funcDesc;
	private String funcType;
	private Set funcMenus;
	private Set funcAuths;
	public Set getFuncMenus() {
		return funcMenus;
	}
	public void setFuncMenus(Set funcMenus) {
		this.funcMenus = funcMenus;
	}
	public int getFuncId() {
		return funcId;
	}
	public void setFuncId(int funcId) {
		this.funcId = funcId;
	}
	public String getFuncURL() {
		return funcURL;
	}
	public void setFuncURL(String funcURL) {
		this.funcURL = funcURL;
	}
	public String getFuncDesc() {
		return funcDesc;
	}
	public void setFuncDesc(String funcDesc) {
		this.funcDesc = funcDesc;
	}
	public String getFuncType() {
		return funcType;
	}
	public void setFuncType(String funcType) {
		this.funcType = funcType;
	}
	public Set getFuncAuths() {
		return funcAuths;
	}
	public void setFuncAuths(Set funcAuths) {
		this.funcAuths = funcAuths;
	}

}
