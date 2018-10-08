package fmss.action.entity;

import java.sql.Timestamp;

import fmss.dao.entity.UBaseHolidayDO;


public class HolidayAuditEntityDO extends UBaseHolidayDO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4469782891539477525L;

	protected String auditUser;
	protected Timestamp auditTime;
	protected String changeUser;
	protected Timestamp changeTime;
	protected Long auditStatus;
	protected Long id;
	protected Long changeStatus = new Long(0);

	protected String changeRemark;
	
	public String getChangeRemark() {
		return changeRemark;
	}

	public void setChangeRemark(String changeRemark) {
		this.changeRemark = changeRemark;
	}

	public String getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}

	public java.sql.Timestamp getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Timestamp auditTime) {
		this.auditTime = auditTime;
	}

	public String getChangeUser() {
		return changeUser;
	}

	public void setChangeUser(String changeUser) {
		this.changeUser = changeUser;
	}

	public Timestamp getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(Timestamp changeTime) {
		this.changeTime = changeTime;
	}

	public Long getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Long auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getChangeStatus() {
		return changeStatus;
	}

	public void setChangeStatus(Long changeStatus) {
		this.changeStatus = changeStatus;
	}
}
