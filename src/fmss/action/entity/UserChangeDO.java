package fmss.action.entity;

import fmss.action.base.AuditBase;
import fmss.action.base.UserBaseAuditBase;

public class UserChangeDO extends AuditEntityDO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8012502520235707102L;
	private transient AuditBase auditEntity = new UserBaseAuditBase();

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public AuditBase getAuditEntity() {
		return auditEntity;
	}

	public void setAuditEntity(AuditBase auditEntity) {
		this.auditEntity = auditEntity;
	}

}
