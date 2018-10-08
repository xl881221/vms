<!--file: <%=request.getRequestURI() %> -->
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="fmss.common.cache.CacheManager,org.apache.commons.lang.StringUtils"%>
<%
	String webapp = request.getScheme() + "://" + request.getServerName() + ":"+ request.getServerPort() + request.getContextPath();
	String theme = webapp + "/theme/default";
	if(StringUtils.isNotEmpty(CacheManager.getParemerCacheMapValue(fmss.common.util.Constants.PARAM_THEME_PATH_CONFIG))){
		theme = webapp + CacheManager.getParemerCacheMapValue(fmss.common.util.Constants.PARAM_THEME_PATH_CONFIG);
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML lang=UTF-8 xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="expires" content="0">
	<META content="text/html; charset=UTF-8" http-equiv=Content-Type>
	<LINK rel=stylesheet type=text/css href="<%=webapp %>/css/style_homecalendar.css" rev=stylesheet media=screen>
	<LINK rel=stylesheet type=text/css href="<%=webapp %>/css/rili_index_homecalendar.css" rev=stylesheet media=screen>
	<SCRIPT type=text/javascript src="<%=webapp %>/js/jquery/jquery.js" charset="UTF-8"></SCRIPT>
	<SCRIPT type=text/javascript>
<%
	boolean b=(request.getParameter("readOnly")!=null&&request.getParameter("readOnly").equals("true"));
	boolean isSearch=(request.getParameter("isSearch")!=null&&request.getParameter("isSearch").equals("isSearch"));
%>
	READ_ONLY=<%=b%>
<%
	System.out.println(request.getAttribute("RESULT_MESSAGE"));
	if(request.getAttribute("RESULT_MESSAGE") !=null 
		&& !"".equals(request.getAttribute("RESULT_MESSAGE"))
		&& !"true".equals(request.getAttribute("RESULT_MESSAGE"))){
%>
	alert("<%=java.net.URLDecoder.decode(request.getAttribute("RESULT_MESSAGE").toString(),"utf-8")%>");
<%
	}
%>
	</SCRIPT>
	<SCRIPT type=text/javascript src="<%=webapp %>/js/jquery/2345.js" charset="UTF-8"></SCRIPT>
	<link href="<%=theme %>/css/main.css" rel="stylesheet" type="text/css" />
</HEAD>
<BODY class=RiliIndex>
<table id="tbl_main" cellpadding="0" cellspacing="0">
<!-- 
<tr>
	<td align="center">
	<table id="tbl_tools" cellpadding="1" cellspacing="0">
	<tr>
		<td align="right" style="padding-left:5px;"></td>
		<td></td>
	</tr>
	</table>
	</td>
</tr>
 -->
</table>
<DIV id=page>
	<DIV id=bodyPart>
		<DIV style="MARGIN: 10px 15px; WIDTH: 598px" class=mt15>
			<DIV style="BACKGROUND: none transparent scroll repeat 0% 0%" id=calendar_container class="container clearfix">
				<DIV class=cldContentWrapper>
					<DIV class=cldContent><!-- 日历月视图 -->
						<DIV id=cldGridWrapper class=cldGridWrapper>
							<DIV id=mainWrapper class=mainWrapper>
								<DIV id=mainBody>
									<DIV id=mainNav class=mainNavRili>
										<DIV id=prevMonthBtn class="dateNavBtnWrapperRili month" title=上一月 onclick=dateSelection.goPrevMonth()>
											<DIV class="t2 themeBgColorRili1">&nbsp;</DIV>
											<DIV class="getBtn dateNavBtnRili themeBgColorRili1">◄</DIV>
											<DIV class="t2 themeBgColorRili1">&nbsp;</DIV>
										</DIV>
										<DIV id=nextMonthBtn class="dateNavBtnWrapperRili month" title=下一月 onclick=dateSelection.goNextMonth()>
											<DIV class="t2 themeBgColorRili1">&nbsp;</DIV>
											<DIV class="getBtn dateNavBtnRili themeBgColorRili1">►</DIV>
											<DIV class="t2 themeBgColorRili1">&nbsp;</DIV>
										</DIV>
										<DIV id=prevYearBtn class="dateNavBtnWrapperRili year" title=上一年 onclick=dateSelection.goPrevYear()>
											<DIV class="t2 themeBgColorRili1">&nbsp;</DIV>
											<DIV class="getBtn dateNavBtnRili themeBgColorRili1">◄◄</DIV>
											<DIV class="t2 themeBgColorRili1">&nbsp;</DIV>
										</DIV>
										<DIV id=nextYearBtn class="dateNavBtnWrapperRili year" title=下一年 onclick=dateSelection.goNextYear()>
											<DIV class="t2 themeBgColorRili1">&nbsp;</DIV>
											<DIV class="getBtn dateNavBtnRili themeBgColorRili1">►►</DIV>
											<DIV class="t2 themeBgColorRili1">&nbsp;</DIV>
										</DIV>
										<DIV style="WIDTH:500px; FONT-SIZE:16px" class=dateNavInfoRili>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<SPAN style="BORDER-BOTTOM: red 1px solid; COLOR: red" id=dateSelectionRili class=dateSelectionRili onclick=dateSelection.show()>
											<SPAN id=yearValue></SPAN>年<SPAN id=monthValue></SPAN>月</SPAN><SPAN class=dateSelectionBtn onclick=dateSelection.show()>▼</SPAN>
											<SPAN id=lunarValue></SPAN>
										</DIV>
									</DIV>
									<DIV id=dateSelectionDiv>
										<DIV id=dateSelectionHeader></DIV>
										<DIV id=dateSelectionBody>
											<DIV id=yearList>
												<DIV id=yearListPrev onclick=dateSelection.prevYearPage()>&lt;</DIV>
												<DIV id=yearListContent></DIV>
												<DIV id=yearListNext onclick=dateSelection.nextYearPage()>&gt;</DIV>
											</DIV>
											<DIV id=dateSeparator></DIV>
											<DIV id=monthList>
												<DIV id=monthListContent></DIV>
												<DIV style="CLEAR: both"></DIV>
											</DIV>
											<DIV id=dateSelectionBtn>
												<DIV id=dateSelectionTodayBtn onclick=dateSelection.goToday()>今天</DIV>
												<DIV id=dateSelectionOkBtn onclick=dateSelection.go()>确定</DIV>
												<DIV id=dateSelectionCancelBtn onclick=dateSelection.hide()>取消</DIV>
											</DIV>
										</DIV>
										<DIV id=dateSelectionFooter></DIV>
									</DIV>
									<DIV class="t1 themeBgColorRili"></DIV>
									<DIV class="t2 themeBgColorRili">&nbsp;</DIV>
									<DIV id=mainGrid class="mainGrid themeBgColorRili">
										<DIV id=colheadersRili></DIV>
										<DIV id=gridcontainerRili>
											<DIV id=calowner>
												<DIV id=grid class=grid>
													<DIV id=decowner></DIV>
													<DIV id=eventowner></DIV>
													<DIV id=clickowner></DIV>
													<DIV id=colowner></DIV>
													<DIV id=rowowner></DIV>
												</DIV>
											</DIV>
										</DIV>
									</DIV>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
					<DIV class="t2 ">&nbsp;</DIV>
					<DIV class="t1 ">&nbsp;</DIV>
					<DIV style="CLEAR: both"></DIV>
				</DIV>
			</DIV>
			<DIV style="DISPLAY: none; BACKGROUND: none transparent scroll repeat 0% 0%; HEIGHT: 396px" id=iframeContainer class="container left">
				<DIV class="cldContentWrapper left">
					<DIV class="t1 ">&nbsp;</DIV>
					<DIV class="t2 ">&nbsp;</DIV>
					<DIV class="cldContent ">
						<DIV style="HEIGHT: 370px" class=cldGridWrapper>
							<DIV style="BACKGROUND-OLOR: #ffffff; MARGIN-TOP: 10px; HEIGHT: 360px" class=mainWrapper>
								<DIV id=iframeContent></DIV>
							</DIV>
						</DIV>
					</DIV>
					<DIV class="t2 ">&nbsp;</DIV>
					<DIV class="t1 ">&nbsp;</DIV>
					<DIV style="CLEAR: both"></DIV>
				</DIV>
			</DIV>
			<DIV style="CLEAR: both"></DIV>
		</DIV>
		<DIV id=detailDialog class="dialogRili ts328">
			<DIV class="titleWrapperRili clearfix">
				<DIV class="title right"></DIV>
			</DIV>
			<DIV class=contentWrapper>
				<DIV class=content>
					<DIV id=detaillass=detailRili></DIV>
				</DIV>
			</DIV>
		</DIV>
	</DIV>
	<TEXTAREA id=detail_default_tpl class=tplbox> &lt;!---
		var info = arguments[0];
		if(!info.huangliY)
			info.huangliY='无';
		if(!info.huangliJ)
			info.huangliJ='无';
		--&gt;
		&lt;div class=""&gt;
		&lt;div class="left dateNumClass"&gt;+-info.dateNum-+&lt;/div&gt;
		&lt;div class="lunar left"&gt;
		&lt;div style="font-size:14px;font-weight:bold;margin-top:5px;"&gt;+-info.dateDetail-+&lt;/div&gt;
		&lt;div style="margin-top:-10px;"&gt;+-info.lunar-+&lt;/div&gt;
		&lt;/div&gt;
		&lt;/div&gt;
		&lt;div style="clear:both;padding:0px 10px;"&gt;&lt;HR class="hrClass"&gt;&lt;/HR&gt;&lt;/div&gt;
		&lt;div class="huangli"&gt;&lt;span class="yj y"&gt;&lt;/span&gt;&lt;span class="huangliSpan"&gt;+-info.huangliY-+&lt;/span&gt;&lt;/div&gt;
		&lt;div style="clear:both;"&gt;&lt;/div&gt;
		&lt;div class="huangli"&gt;&lt;span class="yj j"&gt;&lt;/span&gt;&lt;span class="huangliSpan"&gt;+-info.huangliJ-+&lt;/span&gt;&lt;/div&gt;
		&lt;!---
		if(info.schedule){
			--&gt;
			&lt;div style="clear:both;padding:0px 10px;"&gt;&lt;HR class="hrClass"&gt;&lt;/HR&gt;&lt;/div&gt;
			&lt;!---
			for(var k=0;k&lt;(info.schedule.length&lt;2?info.schedule.length:2);k++){
				--&gt;
				&lt;div style="margin-left:17px;color:#003399;line-height:20px;"&gt;+-info.schedule[k].notes-+&lt;/div&gt;
				&lt;!---
			}
		}
		--&gt;
		&lt;div style="clear:both;margin-bottom:5px;"&gt;&lt;/div&gt;
	</TEXTAREA>
	<TEXTAREA id=detail_iframe_tpl class=tplbox> &lt;!---
		var info = arguments[0];
		if(!info.huangliY)
			info.huangliY='无';
		if(!info.huangliJ)
			info.huangliJ='无';
		--&gt;
		&lt;div id="dateInfo" class="left"&gt;
		&lt;div class="left dateNumClass1" id="dateNumIframe"&gt;+-info.dateNum-+&lt;/div&gt;
		&lt;div class="lunar left" style="margin-left:5px;"&gt;
		&lt;div id="detailInfo" style="font-size:14px;font-weight:bold;margin-top:13px;"&gt;+-info.dateDetail-+&lt;/div&gt;
		&lt;div id="lunar" style="margin-top:10px;"&gt;+-info.lunar-+&lt;/div&gt;
		&lt;/div&gt;
		&lt;/div&gt;
		&lt;div style="clear:both;padding:0px 10px;"&gt;&lt;HR class="hrClass"&gt;&lt;/HR&gt;&lt;/div&gt;
	</TEXTAREA>
	<TEXTAREA id=detail_click_tpl class=tplbox> &lt;!---
		var data = arguments[0];
		var key = 'd'+global.currYear+'-'+(global.currMonth+1)+'-'+data.date;
		var title = cellImage[key]?cellImage[key].imageDesc:'设置节假日';
		--&gt;
		&lt;div title="+-title-+" onmouseover="indexMgr.dateover(+-data.index-+,+-data.date-+,event)"
		onmouseout="indexMgr.dateout(+-data.index-+,event)"
		onclick="showIframeRili(+-data.date-+)"
		class="click" style="left: +-data.left-+px;top: +-data.top-+px;width: +-data.width-+px;height: +-data.height-+px;"&gt;&lt;/div&gt;
	</TEXTAREA>
	<TEXTAREA id=iframe_tpl class=tplbox> &lt;!---
		var iframe_params = arguments[0];
		var dateInfo = arguments[1];
		var dateStr = escape(JSON.stringify(dateInfo));
		var keySrc="";
		if(Number(iframe_params.sYear)&gt;2012||Number(iframe_params.sYear)&lt;2010){
			keySrc="/dayList/rili.html?dateInfo="+dateStr;
		}
		else keySrc="/dayList/"+iframe_params.key+".html?dateInfo="+dateStr; //keySrc="/dayList/"+iframe_params.key+".html";
		--&gt;
		&lt;iframe style="width:565px;height:420px;background-color:#ffffff;" src="+-keySrc-+" scrolling="no" FrameBorder="0"&gt;
		&lt;/iframe&gt;
	</TEXTAREA>
	<TEXTAREA id=calendar_cell_in_tpl class=tplbox> &lt;!---
		var data = arguments[0];
		//alert('d'+global.currYear+'-'+(global.currMonth+1)+'-'+data.dateNum);
		var key = 'd'+global.currYear+'-'+(global.currMonth+1)+'-'+data.dateNum;
		if(cellImage[key]){
			--&gt;
			&lt;div title="+-cellImage[key].imageDesc-+" onclick="showIframeRili(+-data.dateNum-+)" onmouseover="indexMgr.dateover(+-data.index-+,+-data.dateNum-+,event)" onmouseout="indexMgr.dateout(+-data.index-+,event)" style="background:url(+-cellImage[key].imageURL-+);background-repeat:no-repeat;background-position:center center;position: absolute; left: +-data.left-+%; top: +-data.top-+%; width: +-data.width-+%; height: +-data.height-+%; z-index: 3;"&gt;
			&lt;div id="rg_rowy_h+-data.index-+" style="color:#ffffff;font-size:12px;font-weight:bold;overflow:hidden;cursor:pointer;" class="dayOfMonthRili dayOfMonthWithoutBg dayInMonth+-data.isToday ? ' currentDayRili' : ''-+" &gt;
			&nbsp;+-cellImage[key].imageDesc-+
			&lt;/div&gt;
			&lt;div class="dateNum" style="color:#ffffff;font-size:20px;"&gt;+-data.dateNum-+&lt;/div&gt;
			&lt;/div&gt;
			&lt;!---
		}else{
			--&gt;
			&lt;div title="设置节假日" style="position: absolute; left: +-data.left-+%; top: +-data.top-+%; width: +-data.width-+%; height: +-data.height-+%;cursor:pointer; z-index:1;" onclick="showIframeRili(+-data.dateNum-+)" &gt;
			&lt;div id="rg_rowy_h+-data.index-+" class="dayOfMonthRili dayInMonth+-data.isToday ? ' currentDayRili' : ''-+" style="color:+-data.lunarColor-+;cursor:pointer;"&gt;
			&nbsp;+-data.lunar-+
			&lt;/div&gt;
			&lt;div id="rg_cell_h+-data.dateNum-+" class="dateNum"&gt;+-data.dateNum-+&lt;/div&gt;
			&lt;/div&gt;
			&lt;!---
		}
		--&gt;
	</TEXTAREA>
	<TEXTAREA id=calendar_cell_out_tpl class=tplbox> &lt;!---
		var data = arguments[0];
		--&gt;
		&lt;div class="dayNotInMonthWrapper" style="position: absolute; left: +-data.left-+%; top: +-data.top-+%; width: +-data.width-+%; height: +-data.height-+%; z-index: 1;"&gt;&lt;/div&gt;
	</TEXTAREA>
	<TEXTAREA id=calendar_cell_today_tpl class=tplbox> &lt;!---
		var data = arguments[0];
		--&gt;
		&lt;div style="position: absolute; left: +-data.left-+%; top: +-data.top-+%; width: +-data.width-+%; height: +-data.height-+%; z-index: 0;"&gt;
		&lt;div class="currentDayDec"&gt;&lt;/div&gt;
		&lt;/div&gt;
	</TEXTAREA>
	<DIV id=feedbackDialog class="dialog ts428">
		<DIV class=top></DIV>
		<DIV class="titleWrapper clearfix">
			<DIV class="title left"></DIV>
			<DIV class="close right" title=关闭 onclick=dialogMgr.hide()></DIV>
		</DIV>
		<DIV class=contentWrapper>
			<DIV class=content>
				<DIV id=feedbackDialogContent class=loginDialogContent></DIV>
			</DIV>
		</DIV>
		<DIV class=bottom></DIV>
	</DIV>
	<SCRIPT type=text/javascript>
		//var FIX_COLOR = '#FB1FFC';
		var FIX_COLOR = '#8bcf1c';
		var FIX_COLORADD = '#00c';
		var FIX_COLORREMORE = '#f00';
		var SHOW_HOLIDAY_MONTH = true;
		var holidayType = '<s:property value="holidayType"/>';
		var holidayArray = '<s:property value="holidayArray"/>';
		var cellImage={};
		initRiliIndex();
	</SCRIPT>
</DIV>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;节假日&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<span id="tipsHoliday">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
	<!-- 
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;新增节假日
	<span id="tipsAddHoliday">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;取消节假日
	<span id="tipsRemoreHoliday">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
	 -->
	<%
		if(!b){
	%>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;点击设置/取消节假日
	<%
		}
	%>
	<input type="hidden" id="unAudit" name="unAudit" value="<s:property value="unAudit"/>"/>
</BODY>
</HTML>
<SCRIPT type=text/javascript>
	$.dom('tipsHoliday').style.backgroundColor = FIX_COLOR;
	//$.dom('tipsAddHoliday').style.backgroundColor = FIX_COLORADD;
	//$.dom('tipsRemoreHoliday').style.backgroundColor = FIX_COLORREMORE;
</SCRIPT>