/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;

/**
 * <p>
 * 版权所有:(C)2003-2010 
 * </p>
 * 
 * @作者: zhangshoufeng
 * @日期: 2009-6-23 下午03:48:36
 * @描述:[UAuthResMapDO]资源对照表
 */
public class UAuthResMapDO extends BaseDO implements Serializable {

	private String resId;// 资源对照编号 RES_ID
	private String resName;// 资源名称 RES_NAME
	private String systemId;// 系统编号 U_BASE_CONFIG.SYSTEM_ID
	private String resType;// 类型 RES_TYPE
	private String srcTable;// 源表名 SRC_TABLE
	private String srcKeyField;// 源主键字段
	private String srcIdField;// 源编号字段
	private String srcNameField;// 源名称字段
	private java.lang.Integer orderNum;// 序号 ORDER_NUM
	private String description;// 描述 DESCRIPTION
	private UBaseConfigDO ubaseConfig;// 子系统配置信息
	private UBaseDictionaryDO uBaseDictionary;// 字典表信息

	/**
	 * <p>
	 * 方法名称: getResId|描述:取得资源对照编号
	 * </p>
	 * 
	 * @return
	 */
	public String getResId() {
		return resId;
	}

	/**
	 * <p>
	 * 方法名称: setResId|描述:设置资源对照编号
	 * </p>
	 * 
	 * @param resId
	 */
	public void setResId(String resId) {
		this.resId = resId;
	}

	/**
	 * <p>
	 * 方法名称: getResName|描述:取得资源名称
	 * </p>
	 * 
	 * @return
	 */
	public String getResName() {
		return resName;
	}

	/**
	 * <p>
	 * 方法名称: setResName|描述:设置资源名称
	 * </p>
	 * 
	 * @param resName
	 */
	public void setResName(String resName) {
		this.resName = resName;
	}

	/**
	 * <p>
	 * 方法名称: getSystemId|描述:取得系统编号
	 * </p>
	 * 
	 * @return
	 */
	public String getSystemId() {
		return systemId;
	}

	/**
	 * <p>
	 * 方法名称: setSystemId|描述:设置系统编号
	 * </p>
	 * 
	 * @param systemId
	 */
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	/**
	 * <p>
	 * 方法名称: getResType|描述:取得资源类型
	 * </p>
	 * 
	 * @return
	 */
	public String getResType() {
		return resType;
	}

	/**
	 * <p>
	 * 方法名称: setResType|描述: 设置资源类型
	 * </p>
	 * 
	 * @param resType
	 */
	public void setResType(String resType) {
		this.resType = resType;
	}

	/**
	 * <p>
	 * 方法名称: getSrcTable|描述: 取得系统源表名
	 * </p>
	 * 
	 * @return
	 */
	public String getSrcTable() {
		return srcTable;
	}

	/**
	 * <p>
	 * 方法名称: setSrcTable|描述:设置系统源表名
	 * </p>
	 * 
	 * @param srcTable
	 */
	public void setSrcTable(String srcTable) {
		this.srcTable = srcTable;
	}

	/**
	 * @return srcKeyField
	 */
	public String getSrcKeyField() {
		return srcKeyField;
	}

	/**
	 * @param srcKeyField
	 *            要设置的 srcKeyField
	 */
	public void setSrcKeyField(String srcKeyField) {
		this.srcKeyField = srcKeyField;
	}

	/**
	 * @return srcIdField
	 */
	public String getSrcIdField() {
		return srcIdField;
	}

	/**
	 * @param srcIdField
	 *            要设置的 srcIdField
	 */
	public void setSrcIdField(String srcIdField) {
		this.srcIdField = srcIdField;
	}

	/**
	 * @return srcNameField
	 */
	public String getSrcNameField() {
		return srcNameField;
	}

	/**
	 * @param srcNameField
	 *            要设置的 srcNameField
	 */
	public void setSrcNameField(String srcNameField) {
		this.srcNameField = srcNameField;
	}

	/**
	 * <p>
	 * 方法名称: getOrderNum|描述:取得系统序号
	 * </p>
	 * 
	 * @return
	 */
	public java.lang.Integer getOrderNum() {
		return orderNum;
	}

	/**
	 * <p>
	 * 方法名称: setOrderNum|描述:设置系统序号
	 * </p>
	 * 
	 * @param orderNum
	 */
	public void setOrderNum(java.lang.Integer orderNum) {
		this.orderNum = orderNum;
	}

	/**
	 * <p>
	 * 方法名称: getDescription|描述: 取得系统描述信息
	 * </p>
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <p>
	 * 方法名称: setDescription|描述:设置系统描述信息
	 * </p>
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * <p>
	 * 方法名称: getUbaseConfig|描述:取得子系统信息
	 * </p>
	 * 
	 * @return
	 */
	public UBaseConfigDO getUbaseConfig() {
		return ubaseConfig;
	}

	/**
	 * <p>
	 * 方法名称: setUbaseConfig|描述: 设置子系统信息
	 * </p>
	 * 
	 * @param ubaseConfig
	 */
	public void setUbaseConfig(UBaseConfigDO ubaseConfig) {
		this.ubaseConfig = ubaseConfig;
	}

	/**
	 * <p>
	 * 方法名称: getUBaseDictionary|描述: 取得字典表信息
	 * </p>
	 * 
	 * @return
	 */
	public UBaseDictionaryDO getUBaseDictionary() {
		return uBaseDictionary;
	}

	/**
	 * <p>
	 * 方法名称: setUBaseDictionary|描述: 设置字典表信息
	 * </p>
	 * 
	 * @param baseDictionary
	 */
	public void setUBaseDictionary(UBaseDictionaryDO baseDictionary) {
		uBaseDictionary = baseDictionary;
	}

	/*
	 * （非 Javadoc） <p>重写方法: equals|描述: </p>
	 * 
	 * @param other
	 * 
	 * @return
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof UAuthResMapDO))
			return false;
		final UAuthResMapDO uarm = (UAuthResMapDO) other;
		if (!uarm.getResId().equals(getResId()))
			return false;
		return true;
	}

	/*
	 * （非 Javadoc） <p>重写方法: hashCode|描述: </p>
	 * 
	 * @return
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int result;
		result = getResId().hashCode();
		result = 30 * result;
		return result;
	}

	public boolean isVisitSelfResource() {
		return "PUB".equalsIgnoreCase(this.resType);
	}
}
