<%@ page language="java"  import="fmss.dao.entity.LoginDO,com.jes.core.api.servlet.ServletHelper"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="fmss.common.util.*,java.util.*"%>
<%@page import="fmss.common.config.UIConfig"%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
%>

<%@include file="common/include-null.jsp"%>

<%String language=ServletHelper.getCookieValue(request,"USYS_USER_LANGUAGE");%>

	<%
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");

		String[] week = null;
		if("e".equals(language)){
		  week=new String[]{ "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
		}else{
		  week=new String[]{ "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		}
		
		
		LoginDO login = (LoginDO) request.getSession().getAttribute(
				"LOGIN_USER");
		String userCname = login.getUserCname();
		DateManager dm = new DateManager();
		java.sql.Date currentDay = dm.getDay(new Date());
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		String weekDay = week[calendar.get(java.util.Calendar.DAY_OF_WEEK) - 1];
	%>

				
					<ul class="nav">
					<s:iterator value="top10MenuList" status="rowstatus" id="row">
							<s:if test="menuRes == 1 || systemId == '00010'">
								<li  class="nav-1" id="firstId"   >
									<a id="firstUrl" target="mainFramePage"  href="<s:property value='unitLoginUrl'/>"
										 
										name="mainpage" target="mainFramePage">
									<s:if test='menu_language=="e"'>
										<s:property value="systemNickEname" />
									</s:if>
									<s:else>
										<s:property value="systemCname" />
									</s:else>
									</a>
								</li>
							</s:if>
							<s:else>
								<li class="nav-1" sysid="<s:property value="systemId" />" sysname="<s:property value="systemEname" />">
									<a target="mainFramePage"
										  U="menu.action?subSysId=<s:property value="systemId" />&subSysName=<s:property value="systemEname" />" > 
										<s:if test='menu_language=="e"'>
											<s:property value="systemNickEname" />
										</s:if>
										<s:else>
											<s:property value="systemCname" />
										</s:else>
									</a>
								</li>
							</s:else>

						</s:iterator>
						 
				
				 
					</ul>
				


