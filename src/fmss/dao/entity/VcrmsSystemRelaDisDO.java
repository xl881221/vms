package fmss.dao.entity;

import java.io.Serializable;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: jiazhimin
 * @����: 2011-04-25
 * @����: [VcrmsSystemRelaDisDO]������
 */
public class VcrmsSystemRelaDisDO extends BaseDO implements Serializable{

	private static final long serialVersionUID = 1L;
	private String systemId;
	private String systemCname;
	private String crmsSubjectId;
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public String getSystemCname() {
		return systemCname;
	}
	public void setSystemCname(String systemCname) {
		this.systemCname = systemCname;
	}
	public String getCrmsSubjectId() {
		return crmsSubjectId;
	}
	public void setCrmsSubjectId(String crmsSubjectId) {
		this.crmsSubjectId = crmsSubjectId;
	}
	
}
