package fmss.action.entity;

import fmss.action.base.AuditBase;
import fmss.action.base.HolidayBaseAuditBase;
import fmss.action.base.HolidayTypeBaseAuditBase;

public class HolidayChangeDO extends HolidayAuditEntityDO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8012512520235707102L;
	private transient AuditBase holidayAuditEntity = new HolidayBaseAuditBase();

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public AuditBase getHolidayAuditEntity() {
		return holidayAuditEntity;
	}

	public void setHolidayAuditEntity(AuditBase holidayAuditEntity) {
		this.holidayAuditEntity = holidayAuditEntity;
	}
}
