/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-23 下午04:03:04
 * @描述: [UBaseInstRelaDO]
 */
public class UBaseInstRelaDO extends BaseDO implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -7797903417945095104L;

	private String instId;// 机构编号 INST_ID varchar2(20)

	private String instIdLevel1;
	private String instIdLevel2;
	private String instIdLevel3;
	private String instIdLevel4;
	private String instIdLevel5;
	private String instIdLevel6;
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public String getInstIdLevel1() {
		return instIdLevel1;
	}
	public void setInstIdLevel1(String instIdLevel1) {
		this.instIdLevel1 = instIdLevel1;
	}
	public String getInstIdLevel2() {
		return instIdLevel2;
	}
	public void setInstIdLevel2(String instIdLevel2) {
		this.instIdLevel2 = instIdLevel2;
	}
	public String getInstIdLevel3() {
		return instIdLevel3;
	}
	public void setInstIdLevel3(String instIdLevel3) {
		this.instIdLevel3 = instIdLevel3;
	}
	public String getInstIdLevel4() {
		return instIdLevel4;
	}
	public void setInstIdLevel4(String instIdLevel4) {
		this.instIdLevel4 = instIdLevel4;
	}
	public String getInstIdLevel5() {
		return instIdLevel5;
	}
	public void setInstIdLevel5(String instIdLevel5) {
		this.instIdLevel5 = instIdLevel5;
	}
	public String getInstIdLevel6() {
		return instIdLevel6;
	}
	public void setInstIdLevel6(String instIdLevel6) {
		this.instIdLevel6 = instIdLevel6;
	}
}
