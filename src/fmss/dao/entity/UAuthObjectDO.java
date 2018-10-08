/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-27 上午10:25:30
 * @描述: [UAuthObjectDO]权限主体表
 */
public class UAuthObjectDO extends BaseDO implements Serializable{

	private String objectId; // 权限主体编号 OBJECT_ID
	private String objectName; // 权限主体名称 OBJECT_NAME
	private String objectType; // 权限主体类型 OBJECT_TYPE

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
	 * <p>方法名称: getObjectName|描述:取得权限主体名称 </p>
	 * @return
	 */
	public String getObjectName(){
		return objectName;
	}

	/**
	 * <p>方法名称: setObjectName|描述:设置权限主体名称 </p>
	 * @param objectName
	 */
	public void setObjectName(String objectName){
		this.objectName = objectName;
	}

	/**
	 * <p>方法名称: getObjectType|描述:取得权限主体类型 </p>
	 * @return
	 */
	public String getObjectType(){
		return objectType;
	}

	/**
	 * <p>方法名称: setObjectType|描述:设置权限主体类型 </p>
	 * @param objectType
	 */
	public void setObjectType(String objectType){
		this.objectType = objectType;
	}
}
