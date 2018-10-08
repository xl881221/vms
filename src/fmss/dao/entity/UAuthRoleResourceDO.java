/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-23 ����03:51:26
 * @����: [UAuthRoleResourceDO]Ȩ������
 */
public class UAuthRoleResourceDO extends BaseDO implements Serializable{

	private String objectId;// Ȩ�������� OBJECT_ID
	private String resId;// ��Դ���ձ�� RES_ID
	private String resDetailValue;// ������Դ����ֵ
	private String resDetailName;// ������Դ��������
	private String systemId;// ��ϵͳ���
	private UAuthResMapDO resMap; //��Դ���� 2009-7-8 xindengquan add
	private UBaseConfigDO ubaseConfig;// ��ϵͳ������Ϣ
	
	/**
	 * @return ubaseConfig
	 */
	public UBaseConfigDO getUbaseConfig(){
		return ubaseConfig;
	}

	
	/**
	 * @param ubaseConfig Ҫ���õ� ubaseConfig
	 */
	public void setUbaseConfig(UBaseConfigDO ubaseConfig){
		this.ubaseConfig = ubaseConfig;
	}

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
	 * <p>��������: getResId|����:ȡ����Դ���ձ�� </p>
	 * @return
	 */
	public String getResId(){
		return resId;
	}

	/**
	 * <p>��������: setResId|����:������Դ���ձ�� </p>
	 * @param resId
	 */
	public void setResId(String resId){
		this.resId = resId;
	}

	
	/**
	 * @return resDetailValue
	 */
	public String getResDetailValue(){
		return resDetailValue;
	}

	
	/**
	 * @param resDetailValue Ҫ���õ� resDetailValue
	 */
	public void setResDetailValue(String resDetailValue){
		this.resDetailValue = resDetailValue;
	}

	
	/**
	 * @return resDetailName
	 */
	public String getResDetailName(){
		return resDetailName;
	}

	
	/**
	 * @param resDetailName Ҫ���õ� resDetailName
	 */
	public void setResDetailName(String resDetailName){
		this.resDetailName = resDetailName;
	}

	
	public UAuthResMapDO getResMap(){
		return resMap;
	}

	
	public void setResMap(UAuthResMapDO resMap){
		this.resMap = resMap;
	}

	
	/**
	 * @return systemId
	 */
	public String getSystemId(){
		return systemId;
	}

	
	/**
	 * @param systemId Ҫ���õ� systemId
	 */
	public void setSystemId(String systemId){
		this.systemId = systemId;
	}

}
