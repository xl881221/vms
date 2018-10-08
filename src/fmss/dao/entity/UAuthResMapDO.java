/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;

/**
 * <p>
 * ��Ȩ����:(C)2003-2010 
 * </p>
 * 
 * @����: zhangshoufeng
 * @����: 2009-6-23 ����03:48:36
 * @����:[UAuthResMapDO]��Դ���ձ�
 */
public class UAuthResMapDO extends BaseDO implements Serializable {

	private String resId;// ��Դ���ձ�� RES_ID
	private String resName;// ��Դ���� RES_NAME
	private String systemId;// ϵͳ��� U_BASE_CONFIG.SYSTEM_ID
	private String resType;// ���� RES_TYPE
	private String srcTable;// Դ���� SRC_TABLE
	private String srcKeyField;// Դ�����ֶ�
	private String srcIdField;// Դ����ֶ�
	private String srcNameField;// Դ�����ֶ�
	private java.lang.Integer orderNum;// ��� ORDER_NUM
	private String description;// ���� DESCRIPTION
	private UBaseConfigDO ubaseConfig;// ��ϵͳ������Ϣ
	private UBaseDictionaryDO uBaseDictionary;// �ֵ����Ϣ

	/**
	 * <p>
	 * ��������: getResId|����:ȡ����Դ���ձ��
	 * </p>
	 * 
	 * @return
	 */
	public String getResId() {
		return resId;
	}

	/**
	 * <p>
	 * ��������: setResId|����:������Դ���ձ��
	 * </p>
	 * 
	 * @param resId
	 */
	public void setResId(String resId) {
		this.resId = resId;
	}

	/**
	 * <p>
	 * ��������: getResName|����:ȡ����Դ����
	 * </p>
	 * 
	 * @return
	 */
	public String getResName() {
		return resName;
	}

	/**
	 * <p>
	 * ��������: setResName|����:������Դ����
	 * </p>
	 * 
	 * @param resName
	 */
	public void setResName(String resName) {
		this.resName = resName;
	}

	/**
	 * <p>
	 * ��������: getSystemId|����:ȡ��ϵͳ���
	 * </p>
	 * 
	 * @return
	 */
	public String getSystemId() {
		return systemId;
	}

	/**
	 * <p>
	 * ��������: setSystemId|����:����ϵͳ���
	 * </p>
	 * 
	 * @param systemId
	 */
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	/**
	 * <p>
	 * ��������: getResType|����:ȡ����Դ����
	 * </p>
	 * 
	 * @return
	 */
	public String getResType() {
		return resType;
	}

	/**
	 * <p>
	 * ��������: setResType|����: ������Դ����
	 * </p>
	 * 
	 * @param resType
	 */
	public void setResType(String resType) {
		this.resType = resType;
	}

	/**
	 * <p>
	 * ��������: getSrcTable|����: ȡ��ϵͳԴ����
	 * </p>
	 * 
	 * @return
	 */
	public String getSrcTable() {
		return srcTable;
	}

	/**
	 * <p>
	 * ��������: setSrcTable|����:����ϵͳԴ����
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
	 *            Ҫ���õ� srcKeyField
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
	 *            Ҫ���õ� srcIdField
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
	 *            Ҫ���õ� srcNameField
	 */
	public void setSrcNameField(String srcNameField) {
		this.srcNameField = srcNameField;
	}

	/**
	 * <p>
	 * ��������: getOrderNum|����:ȡ��ϵͳ���
	 * </p>
	 * 
	 * @return
	 */
	public java.lang.Integer getOrderNum() {
		return orderNum;
	}

	/**
	 * <p>
	 * ��������: setOrderNum|����:����ϵͳ���
	 * </p>
	 * 
	 * @param orderNum
	 */
	public void setOrderNum(java.lang.Integer orderNum) {
		this.orderNum = orderNum;
	}

	/**
	 * <p>
	 * ��������: getDescription|����: ȡ��ϵͳ������Ϣ
	 * </p>
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <p>
	 * ��������: setDescription|����:����ϵͳ������Ϣ
	 * </p>
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * <p>
	 * ��������: getUbaseConfig|����:ȡ����ϵͳ��Ϣ
	 * </p>
	 * 
	 * @return
	 */
	public UBaseConfigDO getUbaseConfig() {
		return ubaseConfig;
	}

	/**
	 * <p>
	 * ��������: setUbaseConfig|����: ������ϵͳ��Ϣ
	 * </p>
	 * 
	 * @param ubaseConfig
	 */
	public void setUbaseConfig(UBaseConfigDO ubaseConfig) {
		this.ubaseConfig = ubaseConfig;
	}

	/**
	 * <p>
	 * ��������: getUBaseDictionary|����: ȡ���ֵ����Ϣ
	 * </p>
	 * 
	 * @return
	 */
	public UBaseDictionaryDO getUBaseDictionary() {
		return uBaseDictionary;
	}

	/**
	 * <p>
	 * ��������: setUBaseDictionary|����: �����ֵ����Ϣ
	 * </p>
	 * 
	 * @param baseDictionary
	 */
	public void setUBaseDictionary(UBaseDictionaryDO baseDictionary) {
		uBaseDictionary = baseDictionary;
	}

	/*
	 * ���� Javadoc�� <p>��д����: equals|����: </p>
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
	 * ���� Javadoc�� <p>��д����: hashCode|����: </p>
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
