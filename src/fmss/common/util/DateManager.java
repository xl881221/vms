package fmss.common.util;


/**
* <p>版权所有:(C)2003-2010 </p>
* @作者: xindengquan
* @日期: 2009-7-14 下午07:16:23
* @描述: [DateManager]主要是对日期的操作
*/
public class DateManager{
	
	/**
	* <p>方法名称: getYYYYMMDD|描述: </p>
	* @param currentDate 当前日期
	* @return yyyy-mm-dd
	*/
	public java.sql.Date getDay(java.util.Date currentDate){
		return new java.sql.Date(currentDate.getTime());
	}
	
	/**
	* <p>方法名称: getWeekDay|描述: </p>
	* @param currentDate 当前日期
	* @return 字符串
	*/
	public String getWeekDay(java.util.Date currentDate){
		String[] week = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		
		return week[calendar.get(java.util.Calendar.DAY_OF_WEEK)-1];
	}
}
