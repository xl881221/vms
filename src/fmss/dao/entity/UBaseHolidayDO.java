package fmss.dao.entity;

import java.io.Serializable;

public class UBaseHolidayDO extends BaseDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 966122239197822475L;
	/**
	 * 
	 */
	private String holidayType;
	private String holidayValue;
	private String description;
	public String getHolidayType() {
		return holidayType;
	}
	public void setHolidayType(String holidayType) {
		this.holidayType = holidayType;
	}
	public String getHolidayValue() {
		return holidayValue;
	}
	public void setHolidayValue(String holidayValue) {
		this.holidayValue = holidayValue;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
