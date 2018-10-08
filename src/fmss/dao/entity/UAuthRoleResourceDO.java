/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-23 下午03:51:26
 * @描述: [UAuthRoleResourceDO]权限主体
 */
public class UAuthRoleResourceDO extends BaseDO implements Serializable{

	private String objectId;// 权限主体编号 OBJECT_ID
	private String resId;// 资源对照编号 RES_ID
	private String resDetailValue;// 具体资源对象值
	private String resDetailName;// 具体资源对象名称
	private String systemId;// 子系统编号
	private UAuthResMapDO resMap; //资源对照 2009-7-8 xindengquan add
	private UBaseConfigDO ubaseConfig;// 子系统配置信息
	
	/**
	 * @return ubaseConfig
	 */
	public UBaseConfigDO getUbaseConfig(){
		return ubaseConfig;
	}

	
	/**
	 * @param ubaseConfig 要设置的 ubaseConfig
	 */
	public void setUbaseConfig(UBaseConfigDO ubaseConfig){
		this.ubaseConfig = ubaseConfig;
	}

	/**
	 * <p>方法名称: getObjectId|描述:取得权限主体编号 </p>
	 * @return
	 */
	public String getObjectId(){
		return objectId;
	}

	/**
	 * <p>方法名称: setObjectId|描述:设置权限主体编号 </p>
	 * @param objectId
	 */
	public void setObjectId(String objectId){
		this.objectId = objectId;
	}

	/**
	 * <p>方法名称: getResId|描述:取得资源对照编号 </p>
	 * @return
	 */
	public String getResId(){
		return resId;
	}

	/**
	 * <p>方法名称: setResId|描述:设置资源对照编号 </p>
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
	 * @param resDetailValue 要设置的 resDetailValue
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
	 * @param resDetailName 要设置的 resDetailName
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
	 * @param systemId 要设置的 systemId
	 */
	public void setSystemId(String systemId){
		this.systemId = systemId;
	}

}
