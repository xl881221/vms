package fmss.common.util;

import java.util.ArrayList;
import java.util.List;

public class PaginationList {

	private List recordList = new ArrayList(); 
	private long recordCount = 0; 
	private int pageSize = 15; 
	private int currentPage = 1; 

	public PaginationList() {

	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public String getCurrentPageStr() {
		return Integer.toString(currentPage);
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(long recordCount) {
		this.recordCount = recordCount;
	}

	public String getRecordCountStr() {
		return Long.toString(recordCount);
	}

	public List getRecordList() {
		return recordList;
	}

	public void setRecordList(List recordList) {
		this.recordList = recordList;
	}

	public int getPageStart() {
		return (this.currentPage - 1) * this.pageSize + 1;
	}

}
