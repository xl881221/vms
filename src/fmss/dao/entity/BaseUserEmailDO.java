package fmss.dao.entity;

import java.io.Serializable;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: jiazhimin
 * @日期: 2011-04-25
 * @描述: [VcrmsSystemRelaDO]机构表
 */
public class BaseUserEmailDO extends BaseDO implements Serializable{

	private static final long serialVersionUID = 1L;
	private String userId;
	private String bankId;
	private String bankName;
	private String systemId;
	private String systemName;
	private String crmsSubjectId;
	private String userCname;
	
	
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getCrmsSubjectId() {
		return crmsSubjectId;
	}
	public void setCrmsSubjectId(String crmsSubjectId) {
		this.crmsSubjectId = crmsSubjectId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public String getUserCname() {
		return userCname;
	}
	public void setUserCname(String userCname) {
		this.userCname = userCname;
	}	
}
