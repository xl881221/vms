package fmss.action.entity;

import org.apache.commons.lang.builder.HashCodeBuilder;

import fmss.action.base.AuditBase;
import fmss.action.base.RoleResourceAuditBase;
import fmss.dao.entity.UAuthRoleResourceDO;

public class RoleResourceChangeDO extends UAuthRoleResourceDO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7449088982254110281L;
	private transient AuditBase auditEntity = new RoleResourceAuditBase();
	private String auditUser;
	private java.sql.Timestamp auditTime;
	private String changeUser;
	private java.sql.Timestamp changeTime;
	private Long auditStatus;
	private Long id;

	private Long changeStatus = new Long(0);

	public String getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}

	public java.sql.Timestamp getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(java.sql.Timestamp auditTime) {
		this.auditTime = auditTime;
	}

	public String getChangeUser() {
		return changeUser;
	}

	public void setChangeUser(String changeUser) {
		this.changeUser = changeUser;
	}

	public java.sql.Timestamp getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(java.sql.Timestamp changeTime) {
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

	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		UAuthRoleResourceDO o = (UAuthRoleResourceDO) obj;
		if (this.getObjectId().equals(o.getObjectId())
				&& this.getResDetailValue().equals(o.getResDetailValue())
				&& this.getResId().equals(o.getResId())
				&& this.getSystemId().equals(o.getSystemId()))
			return true;
		return false;
	}
	
	public int hashCode(){
		return HashCodeBuilder.reflectionHashCode(this);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("角色id=" + this.getObjectId() + ",");
		sb.append("资源id=" + this.getResId() + ",");
		sb.append("资源名=" + this.getResDetailName() + ",");
		sb.append("资源值=" + this.getResDetailValue());
		return sb.toString();
	}

	public AuditBase getAuditEntity() {
		return auditEntity;
	}

	public void setAuditEntity(AuditBase auditEntity) {
		this.auditEntity = auditEntity;
	}

	public String getAuditInfo() {
		return (String) AuditBase.AUDIT_STATUS_MAP.get(this.getAuditStatus());
	}

	public String getShowStatus() {
		return (String) AuditBase.ROLE_RES_TYPE_DESC_MAP.get(this
				.getChangeStatus());
	}

	public String getOwnShowStatus() {
		if (this.getAuditStatus() != null
				&& this.getAuditStatus().intValue() == AuditBase.AUDIT_STATUS_APPROVED)
			return (String) AuditBase.OWN_ROLE_RES_TYPE_DESC_MAP.get(this
					.getChangeStatus());
		if (this.getChangeUser() == null) {
			return (String) AuditBase.OWN_ROLE_RES_TYPE_DESC_MAP.get(this
					.getChangeStatus());
		}
		return (String) AuditBase.ROLE_RES_TYPE_DESC_MAP.get(this
				.getChangeStatus());
	}

}
