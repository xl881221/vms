/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-23 ����04:00:33
 * @����:[UBaseDictionaryDO]ϵͳ�ֵ��
 */
public class UBaseDictionaryDO extends BaseDO implements Serializable{

	private String dicType;// �ֵ���� DIC_TYPE
	private String dicValue;// �ֵ��ֵ DIC_VALUE
	private String dicName;// �ֵ������ DIC_NAME
	private String description;// ���� DESCRIPTION
	private Integer orderNum;// ����

	
	/**
	 * @return orderNum
	 */
	public Integer getOrderNum(){
		return orderNum;
	}

	
	/**
	 * @param orderNum Ҫ���õ� orderNum
	 */
	public void setOrderNum(Integer orderNum){
		this.orderNum = orderNum;
	}

	/**
	 * <p>���캯������: |����:�޲ι��캯�� </p>
	 */
	public UBaseDictionaryDO(){
	}

	/**
	 * <p>���캯������: |����:�����ֵ���Ϣ </p>
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
	 * <p>��������: getDicType|����:ȡ���ֵ������Ϣ </p>
	 * @return
	 */
	public String getDicType(){
		return dicType;
	}

	/**
	 * <p>��������: setDicType|����:�����ֵ������Ϣ </p>
	 * @param dicType
	 */
	public void setDicType(String dicType){
		this.dicType = dicType;
	}

	/**
	 * <p>��������: getDicValue|����:ȡ���ֵ��ֵ </p>
	 * @return
	 */
	public String getDicValue(){
		return dicValue;
	}

	/**
	 * <p>��������: setDicValue|����:�����ֵ��ֵ </p>
	 * @param dicValue
	 */
	public void setDicValue(String dicValue){
		this.dicValue = dicValue;
	}

	/**
	 * <p>��������: getDicName|����:ȡ���ֵ������ </p>
	 * @return
	 */
	public String getDicName(){
		return dicName;
	}

	/**
	 * <p>��������: setDicName|����:�����ֵ������ </p>
	 * @param dicName
	 */
	public void setDicName(String dicName){
		this.dicName = dicName;
	}

	/**
	 * <p>��������: getDescription|����:ȡ��������Ϣ </p>
	 * @return
	 */
	public String getDescription(){
		return description;
	}

	/**
	 * <p>��������: setDescription|����:����������Ϣ </p>
	 * @param description
	 */
	public void setDescription(String description){
		this.description = description;
	}
}
