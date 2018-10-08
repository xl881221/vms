<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="java.util.*"%>
<%@page import="org.springframework.jdbc.core.JdbcTemplate"%>
<%@page import="java.text.SimpleDateFormat"%>
<% 
WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletConfig().getServletContext());
DataSource ds=(DataSource)wac.getBean("dataSource");
JdbcTemplate jt=new JdbcTemplate(ds);
if(request.getParameter("sms")==null||request.getParameter("sms").equals("")){
	return ;
}
String p=request.getParameter("sms");
String t=request.getParameter("gentype");
String download=request.getParameter("download");

if(download!=null&&download.equals("1")){
	response.setContentType("APPLICATION/OCTET-STREAM; charset=gbk");
	response.setHeader("Content-Disposition", "inline;filename=\"menu.sql\"");

}





System.out.println(t);
p="'"+p.replaceAll(",","','")+"'";
//System.out.println(p);
SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
out.println("prompt PL/SQL Developer import file");
out.println("prompt Created on "+sdf.format(new  Date())+" by menu Builder");
out.println("set feedback off");
out.println("set define off");
out.println("prompt Loading Menu..");
out.println("-----------------------------------------");	
if(t.equals("1")){
	out.println("DELETE FROM  U_BASE_MENU ;");
}
else{
	List menus=jt.queryForList("select 'DELETE FROM  U_BASE_MENU  WHERE SYSTEM_ID||''_''||MENU_ID='''||SYSTEM_ID||'_'||MENU_ID||''';' as del from u_Base_Menu WHERE  SYSTEM_ID||'_'||MENU_ID IN ("+p+")  order by  ORDER_NUM");
	Iterator it=menus.iterator();
	while(it.hasNext()){
		out.println(((Map)it.next()).get("del").toString());
	}
}
out.println("-----------------------------------------");		
System.out.println("select 'INSERT INTO U_BASE_MENU (SYSTEM_ID, MENU_ID, MENU_NAME, TARGET, URL, IMG_SRC, ORDER_NUM, DISPLAY, ENABLED) VALUES ('''||SYSTEM_ID||''', '''||MENU_ID||''', '''||MENU_NAME||''',  '''||TARGET||''',  '''||URL||''', '''||IMG_SRC||''', '||ORDER_NUM||', ''YES'',''YES'');' as inst from u_Base_Menu WHERE  SYSTEM_ID||'_'||MENU_ID IN ("+p+") ");
List menus2=jt.queryForList("select 'INSERT INTO U_BASE_MENU (SYSTEM_ID, MENU_ID, MENU_NAME, TARGET, URL, IMG_SRC, ORDER_NUM, DISPLAY, ENABLED) VALUES ('''||SYSTEM_ID||''', '''||MENU_ID||''', '''||MENU_NAME||''',  '''||TARGET||''',  '''||URL||''', '''||IMG_SRC||''', '||ORDER_NUM||', ''YES'',''YES'');' as inst from u_Base_Menu WHERE  SYSTEM_ID||'_'||MENU_ID IN ("+p+") order by  length(MENU_ID),ORDER_NUM,MENU_ID");
Iterator it2=menus2.iterator();
while(it2.hasNext()){
	out.println(((Map)it2.next()).get("inst").toString());
}
out.println("-----------------------------------------");	
out.println("COMMIT;");

out.println("set feedback on");
out.println("set define on");
out.println("prompt Done.");

if(download!=null&&download.equals("1")){
	out.flush();

}

%>