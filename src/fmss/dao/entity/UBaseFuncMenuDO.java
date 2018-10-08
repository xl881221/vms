/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;

public class UBaseFuncMenuDO extends BaseDO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String menuId;
	private String systemId;
	private int funcId;
	private UBaseFuncDO func;
	public int getFuncId() {
		return funcId;
	}
	public void setFuncId(int funcId) {
		this.funcId = funcId;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public UBaseFuncDO getFunc() {
		return func;
	}
	public void setFunc(UBaseFuncDO func) {
		this.func = func;
	}

}
