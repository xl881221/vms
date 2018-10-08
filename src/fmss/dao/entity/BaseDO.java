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
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-27 下午12:03:15
 * @描述: [BaseDO]基类
 */
public class BaseDO implements Serializable{

	private static final long serialVersionUID = 741231858441822688L;// 序列号
	protected static Date theStartDateBK = getStartDate(1970, 0, 1);// 开始日期
	protected static Date theEndDateBK = getStartDate(2099, 11, 31);// 结束日期
	protected Date theStartDate = theStartDateBK;// 开始日期
	protected Date theEndDate = theEndDateBK;// 结束日期

	private static Date getStartDate(int year, int month, int day){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		return cal.getTime();
	}

	/* （非 Javadoc）
	* <p>重写方法: toString|描述: </p>
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
