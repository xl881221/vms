/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-27 ����10:25:30
 * @����: [UAuthObjectDO]Ȩ�������
 */
public class UAuthObjectDO extends BaseDO implements Serializable{

	private String objectId; // Ȩ�������� OBJECT_ID
	private String objectName; // Ȩ���������� OBJECT_NAME
	private String objectType; // Ȩ���������� OBJECT_TYPE

	/**
	 * <p>��������: getObjectId|����:ȡ��Ȩ�������� </p>
	 * @return
	 */
	public String getObjectId(){
		return objectId;
	}

	/**
	 * <p>��������: setObjectId|����:����Ȩ�������� </p>
	 * @param objectId
	 */
	public void setObjectId(String objectId){
		this.objectId = objectId;
	}

	/**
	 * <p>��������: getObjectName|����:ȡ��Ȩ���������� </p>
	 * @return
	 */
	public String getObjectName(){
		return objectName;
	}

	/**
	 * <p>��������: setObjectName|����:����Ȩ���������� </p>
	 * @param objectName
	 */
	public void setObjectName(String objectName){
		this.objectName = objectName;
	}

	/**
	 * <p>��������: getObjectType|����:ȡ��Ȩ���������� </p>
	 * @return
	 */
	public String getObjectType(){
		return objectType;
	}

	/**
	 * <p>��������: setObjectType|����:����Ȩ���������� </p>
	 * @param objectType
	 */
	public void setObjectType(String objectType){
		this.objectType = objectType;
	}
}
