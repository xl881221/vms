package fmss.common.util;


/**
* <p>��Ȩ����:(C)2003-2010 </p>
* @����: xindengquan
* @����: 2009-7-14 ����07:16:23
* @����: [DateManager]��Ҫ�Ƕ����ڵĲ���
*/
public class DateManager{
	
	/**
	* <p>��������: getYYYYMMDD|����: </p>
	* @param currentDate ��ǰ����
	* @return yyyy-mm-dd
	*/
	public java.sql.Date getDay(java.util.Date currentDate){
		return new java.sql.Date(currentDate.getTime());
	}
	
	/**
	* <p>��������: getWeekDay|����: </p>
	* @param currentDate ��ǰ����
	* @return �ַ���
	*/
	public String getWeekDay(java.util.Date currentDate){
		String[] week = {"������","����һ","���ڶ�","������","������","������","������"};
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		
		return week[calendar.get(java.util.Calendar.DAY_OF_WEEK)-1];
	}
}
