/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-23 下午04:00:33
 * @描述:[UBaseDictionaryDO]系统字典表
 */
public class UBaseDictionaryDO extends BaseDO implements Serializable{

	private String dicType;// 字典分类 DIC_TYPE
	private String dicValue;// 字典键值 DIC_VALUE
	private String dicName;// 字典键名称 DIC_NAME
	private String description;// 描述 DESCRIPTION
	private Integer orderNum;// 排序

	
	/**
	 * @return orderNum
	 */
	public Integer getOrderNum(){
		return orderNum;
	}

	
	/**
	 * @param orderNum 要设置的 orderNum
	 */
	public void setOrderNum(Integer orderNum){
		this.orderNum = orderNum;
	}

	/**
	 * <p>构造函数名称: |描述:无参构造函数 </p>
	 */
	public UBaseDictionaryDO(){
	}

	/**
	 * <p>构造函数名称: |描述:设置字典信息 </p>
	 * @param dicType
	 * @param dicValue
	 * @param dicName
	 * @param description
	 */
	public UBaseDictionaryDO(String dicType, String dicValue, String dicName,
			String description){
		this.dicType = dicType;
		this.dicValue = dicValue;
		this.dicName = dicName;
		this.description = description;
	}

	/**
	 * <p>方法名称: getDicType|描述:取得字典分类信息 </p>
	 * @return
	 */
	public String getDicType(){
		return dicType;
	}

	/**
	 * <p>方法名称: setDicType|描述:设置字典分类信息 </p>
	 * @param dicType
	 */
	public void setDicType(String dicType){
		this.dicType = dicType;
	}

	/**
	 * <p>方法名称: getDicValue|描述:取得字典键值 </p>
	 * @return
	 */
	public String getDicValue(){
		return dicValue;
	}

	/**
	 * <p>方法名称: setDicValue|描述:设置字典键值 </p>
	 * @param dicValue
	 */
	public void setDicValue(String dicValue){
		this.dicValue = dicValue;
	}

	/**
	 * <p>方法名称: getDicName|描述:取得字典键名称 </p>
	 * @return
	 */
	public String getDicName(){
		return dicName;
	}

	/**
	 * <p>方法名称: setDicName|描述:设置字典键名称 </p>
	 * @param dicName
	 */
	public void setDicName(String dicName){
		this.dicName = dicName;
	}

	/**
	 * <p>方法名称: getDescription|描述:取得描述信息 </p>
	 * @return
	 */
	public String getDescription(){
		return description;
	}

	/**
	 * <p>方法名称: setDescription|描述:设置描述信息 </p>
	 * @param description
	 */
	public void setDescription(String description){
		this.description = description;
	}
}
