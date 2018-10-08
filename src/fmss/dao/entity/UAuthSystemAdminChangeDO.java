/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;


public class UAuthSystemAdminChangeDO extends BaseDO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;// 用户编号 U_BASE_USER.USER_ID
	private String systemId;// 系统编号 U_BASE_CONFIG.SYSTEM_ID
	private String changeUser;
	private String changeTime;
	private String auditUser;
	private String auditTime;
	private String changeStatus;
	private String auditStatus ;
	private long id ;
	private String systemCname;

	public String getSystemCname() {
		return systemCname;
	}

	public void setSystemCname(String systemCname) {
		this.systemCname = systemCname;
	}

	public String getChangeUser() {
		return changeUser;
	}

	public void setChangeUser(String changeUser) {
		this.changeUser = changeUser;
	}

	public String getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(String changeTime) {
		this.changeTime = changeTime;
	}

	public String getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}

	public String getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	public String getChangeStatus() {
		return changeStatus;
	}

	public void setChangeStatus(String changeStatus) {
		this.changeStatus = changeStatus;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * <p>方法名称: getUserId|描述:取得用户编号 </p>
	 * @return
	 */
	public String getUserId(){
		return userId;
	}

	/**
	 * <p>方法名称: setUserId|描述:设置用户编号 </p>
	 * @param userId
	 */
	public void setUserId(String userId){
		this.userId = userId;
	}

	/**
	 * <p>方法名称: getSystemId|描述:取得系统编号 </p>
	 * @return
	 */
	public String getSystemId(){
		return systemId;
	}

	/**
	 * <p>方法名称: setSystemId|描述:设置系统编号 </p>
	 * @param systemId
	 */
	public void setSystemId(String systemId){
		this.systemId = systemId;
	}
}
