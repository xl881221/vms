package fmss.action.entity;

import fmss.action.base.AuditBase;
import fmss.action.base.InstBaseAuditBase;

public class UBaseInstChangeDO extends InstAuditEntityDO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8012502520235707102L;
	private transient AuditBase instAuditEntity = new InstBaseAuditBase();

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public AuditBase getInstAuditEntity() {
		return instAuditEntity;
	}

	public void setInstAuditEntity(AuditBase instAuditEntity) {
		this.instAuditEntity = instAuditEntity;
	}


}
