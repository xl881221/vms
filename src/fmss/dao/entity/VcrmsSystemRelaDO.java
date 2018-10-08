package fmss.dao.entity;

import java.io.Serializable;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: jiazhimin
 * @����: 2011-04-25
 * @����: [VcrmsSystemRelaDO]������
 */
public class VcrmsSystemRelaDO extends BaseDO implements Serializable{

	private static final long serialVersionUID = 1L;
	private String systemId;
	private String systemCname;
	private String crmsSubjectId;
	private String ResId;
	private String ResName;
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
	public String getResId() {
		return ResId;
	}
	public void setResId(String resId) {
		ResId = resId;
	}
	public String getResName() {
		return ResName;
	}
	public void setResName(String resName) {
		ResName = resName;
	}
}
