
<%@page import="com.jes.core.api.holiday.HolidayManager"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="fmss.common.util.TestHoliday"%>
<%@page import="java.util.HashSet"%>
<%
	String web = request.getScheme() + "://" + request.getServerName() + ":"+ request.getServerPort() + request.getContextPath();
	HolidayManager.getInstance().getHolidayTypes(web, HolidayManager.TYPE_ALL);
	HolidayManager.getInstance().getHolidayTypes(web, HolidayManager.TYPE_ENABLED);
	HolidayManager.getInstance().getHolidayTypes(web, HolidayManager.TYPE_DISABLED);
	HolidayManager.getInstance().checkHoliday(web,new java.util.Date(),"1");
	int result = HolidayManager.getInstance().checkHoliday(web,"2010-10-02","top");
	if(result == HolidayManager.CHECK_CODE_IS_HOLIDAY)
		;
	for (Iterator iterator = HolidayManager.getInstance().getHolidayTypes(web, HolidayManager.TYPE_ALL).iterator(); iterator.hasNext();) {
		Map o = (Map) iterator.next();
		System.out.println(o.get(HolidayManager.HOLIDAY_NAME));
		System.out.println(o.get(HolidayManager.HOLIDAY_TYPE));
		System.out.println(o.get(HolidayManager.HOLIDAY_ENABLE));
	}
	//HolidayManager.getInstance().addHolidays(web,"dyz_21331212212121",TestHoliday.getOneYearDays());
	HolidayManager.getInstance().addHolidays(web,"dyz_21331212212121",new HashSet());
%>