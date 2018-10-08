package fmss.action.entity;

import fmss.action.base.AuditBase;
import fmss.action.base.SubSystemChangeAuditBase;

public class UBaseConfigChangeDO extends SubSystemChangeAuditEntityDO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private transient AuditBase configAuditEntity = new SubSystemChangeAuditBase();

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public AuditBase getConfigAuditEntity() {
		return configAuditEntity;
	}

	public void setConfigAuditEntity(AuditBase configAuditEntity) {
		this.configAuditEntity = configAuditEntity;
	}
   

}
