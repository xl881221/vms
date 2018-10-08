package fmss.dao.entity;

import java.io.Serializable;

public class UBaseHolidayTypeDO extends BaseDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8457021127584193367L;
	/**
	 * 
	 */
	private String holidayType;
	private String holidayName;
	private String enable;
	private String remark;
	public String getHolidayType() {
		return holidayType;
	}
	public void setHolidayType(String holidayType) {
		this.holidayType = holidayType;
	}
	public String getHolidayName() {
		return holidayName;
	}
	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
