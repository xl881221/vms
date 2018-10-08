package fmss.common.ui.controller;

import java.util.List;


/**
 * 类说明:<br>
 * 创建时间: 2008-12-29 上午11:44:23<br>
 * 
 * @author 杨旭飞<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class ReportViewObject {
	private String schemeCode;
	private String reportHtml;
	private String graphJson;
	private String sql;
	private PageObject pageObject;
	private List queryMetaList;
	public ReportViewObject(String schemeCode,String reportHtml,String graphJson,PageObject pageObject,String sql,List queryMetaList){
		this.schemeCode = schemeCode;
		this.reportHtml = reportHtml;
		this.pageObject = pageObject;
		this.graphJson = graphJson;
		this.sql = sql;
		this.queryMetaList = queryMetaList;
	}
	public String getSchemeCode() {
		return schemeCode;
	}
	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}
	public String getReportHtml() {
		return reportHtml;
	}
	public void setReportHtml(String reportHtml) {
		this.reportHtml = reportHtml;
	}
	public String getGraphJson() {
		return graphJson;
	}
	public void setGraphJson(String graphJson) {
		this.graphJson = graphJson;
	}
	public PageObject getPageObject() {
		return pageObject;
	}
	public void setPageObject(PageObject pageObject) {
		this.pageObject = pageObject;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public List getQueryMetaList() {
		return queryMetaList;
	}
	public void setQueryMetaList(List queryMetaList) {
		this.queryMetaList = queryMetaList;
	}

}
