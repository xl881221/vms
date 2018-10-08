package fmss.action.entity;

import org.apache.commons.lang.builder.HashCodeBuilder;

import fmss.action.base.AuditBase;
import fmss.action.base.UserRoleAuditBase;
import fmss.dao.entity.UAuthRoleUserDO;

public class UserRoleChangeDO extends UAuthRoleUserDO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2765466065448809518L;
	private transient AuditBase auditEntity = new UserRoleAuditBase();
	private String auditUser;
	private java.sql.Timestamp auditTime;
	private String changeUser;
	private java.sql.Timestamp changeTime;
	private Long auditStatus;
	private Long id;

	private Long changeStatus = new Long(0);

	private String roleName;

	private String systemName;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AuditBase getAuditEntity() {
		return auditEntity;
	}

	public void setAuditEntity(AuditBase auditEntity) {
		this.auditEntity = auditEntity;
	}

	public Long getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Long auditStatus) {
		this.auditStatus = auditStatus;
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
		UAuthRoleUserDO o = (UAuthRoleUserDO) obj;
		if (this.getRoleId().equals(o.getRoleId())
				&& this.getUserId().equals(o.getUserId()))
			return true;
		return false;
	}
	
	public int hashCode(){
		return HashCodeBuilder.reflectionHashCode(this);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("ÓÃ»§id=" + this.getUserId() + ",");
		sb.append("½ÇÉ«id=" + this.getRoleId());
		return sb.toString();
	}

	public String getShowStatus() {
		return (String) AuditBase.USER_ROLE_TYPE_DESC_MAP.get(this
				.getChangeStatus());
	}

	public String getOwnShowStatus() {
		if (this.getAuditStatus() != null
				&& this.getAuditStatus().intValue() == AuditBase.AUDIT_STATUS_APPROVED)
			return (String) AuditBase.OWN_USER_ROLE_TYPE_DESC_MAP.get(this
					.getChangeStatus());
		if (this.getChangeUser() == null) {
			return (String) AuditBase.OWN_USER_ROLE_TYPE_DESC_MAP.get(this
					.getChangeStatus());
		}
		return (String) AuditBase.USER_ROLE_TYPE_DESC_MAP.get(this
				.getChangeStatus());
	}

	public String getAuditInfo() {
		return (String) AuditBase.AUDIT_STATUS_MAP.get(this.getAuditStatus());
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
}
