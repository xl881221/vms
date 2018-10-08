/*
 * Copyright (c) 2009 All Rights Reserved.
 */
package fmss.dao.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-27 ����12:03:15
 * @����: [BaseDO]����
 */
public class BaseDO implements Serializable{

	private static final long serialVersionUID = 741231858441822688L;// ���к�
	protected static Date theStartDateBK = getStartDate(1970, 0, 1);// ��ʼ����
	protected static Date theEndDateBK = getStartDate(2099, 11, 31);// ��������
	protected Date theStartDate = theStartDateBK;// ��ʼ����
	protected Date theEndDate = theEndDateBK;// ��������

	private static Date getStartDate(int year, int month, int day){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		return cal.getTime();
	}

	/* ���� Javadoc��
	* <p>��д����: toString|����: </p>
	* @return
	* @see java.lang.Object#toString()
	*/
	public String toString(){
		try{
			Map props = BeanUtils.describe(this);
			Iterator iProps = props.keySet().iterator();
			StringBuffer retBuffer = new StringBuffer();
			while(iProps.hasNext()){
				String key = (String) iProps.next();
				// skip false property "class"
				if("class".equals(key)){
					continue;
				}
				// FindBug: Inefficient use of keySet iterator instead of
				// entrySet iterator
				retBuffer.append(key).append("=[").append(props.get(key))
						.append("]");
				if(iProps.hasNext()){
					retBuffer.append(", ");
				}
			}
			return retBuffer.toString();
		}catch (Exception e){
			return super.toString();
		}
	}
}
