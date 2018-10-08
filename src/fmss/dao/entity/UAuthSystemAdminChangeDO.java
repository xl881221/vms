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
	private String userId;// �û���� U_BASE_USER.USER_ID
	private String systemId;// ϵͳ��� U_BASE_CONFIG.SYSTEM_ID
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
	 * <p>��������: getUserId|����:ȡ���û���� </p>
	 * @return
	 */
	public String getUserId(){
		return userId;
	}

	/**
	 * <p>��������: setUserId|����:�����û���� </p>
	 * @param userId
	 */
	public void setUserId(String userId){
		this.userId = userId;
	}

	/**
	 * <p>��������: getSystemId|����:ȡ��ϵͳ��� </p>
	 * @return
	 */
	public String getSystemId(){
		return systemId;
	}

	/**
	 * <p>��������: setSystemId|����:����ϵͳ��� </p>
	 * @param systemId
	 */
	public void setSystemId(String systemId){
		this.systemId = systemId;
	}
}
