package fmss.action.entity;

import fmss.action.base.AuditBase;
import fmss.action.base.SubSystemAdminAuditBase;

public class SubSystemAdminChangeDO extends SubSystemAdminAuditEntityDO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8012502520235707102L;
	private transient AuditBase subSystemAuditEntity = new SubSystemAdminAuditBase();

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public AuditBase getSubSystemAuditEntity() {
		return subSystemAuditEntity;
	}

	public void setSubSystemAuditEntity(AuditBase subSystemAuditEntity) {
		this.subSystemAuditEntity = subSystemAuditEntity;
	}
	
}
