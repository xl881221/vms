package fmss.action.entity;

import fmss.action.base.AuditBase;
import fmss.action.base.HolidayTypeBaseAuditBase;

public class HolidayTypeChangeDO extends HolidayTypeAuditEntityDO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8012512520235707102L;
	private transient AuditBase holidayTypeAuditEntity = new HolidayTypeBaseAuditBase();

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public AuditBase getHolidayTypeAuditEntity() {
		return holidayTypeAuditEntity;
	}

	public void setHolidayTypeAuditEntity(AuditBase holidayTypeAuditEntity) {
		this.holidayTypeAuditEntity = holidayTypeAuditEntity;
	}
}
