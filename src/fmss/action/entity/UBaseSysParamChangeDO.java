package fmss.action.entity;

import fmss.action.base.AuditBase;
import fmss.action.base.SysParamAuditBase;

public class UBaseSysParamChangeDO extends SysParamAuditEntityDO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8012502520235707102L;
	private transient AuditBase sysParamAuditEntity = new SysParamAuditBase();

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public AuditBase getSysParamAuditEntity() {
		return sysParamAuditEntity;
	}

	public void setSysParamAuditEntity(AuditBase sysParamAuditEntity) {
		this.sysParamAuditEntity = sysParamAuditEntity;
	}




}
