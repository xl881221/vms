package fmss.common.ui.controller;

import java.util.ArrayList;
import java.util.List;


/**
 * ��˵��:<br>
 * ����ʱ��: 2008-8-5 ����01:00:04<br>
 * 
 * @author �����<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class PageBox {
	private List pageList;
	
	private QueryData queryData;
	
	private PageObject pageObject;

	public List getPageList() {
		return pageList==null?new ArrayList():pageList;
	}

	public void setPageList(List pageList) {
		this.pageList = pageList;
	}

	public PageObject getPageObject() {
		return pageObject;
	}

	public void setPageObject(PageObject pageObject) {
		this.pageObject = pageObject;
	}

	public QueryData getQueryData() {
		return queryData;
	}

	public void setQueryData(QueryData queryData) {
		this.queryData = queryData;
	}
	
	/*
	 * ����һ����PagBox;
	 * 
	 * */
	public static PageBox getEmptyPageBox(int pageNum, int pageSize){
		PageBox pageBox = new PageBox();
		PageObject pageObject = new PageObject();
		pageObject.setPageSize(pageSize);
		pageObject.setPageIndex(pageNum);
		pageObject.setItemAmount(0);
		pageBox.setPageObject(pageObject);
		return pageBox;
	}



}
