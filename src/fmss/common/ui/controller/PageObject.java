package fmss.common.ui.controller;
/**
 * 类说明:<br>
 * 创建时间: 2008-8-5 下午01:00:31<br>
 * 
 * @author 杨旭飞<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class PageObject {

	private int pageIndex;

	private int itemAmount;

	private int pageSize = 1;

	private int startNum;

	private int endNum;
	
	private boolean hasNextPage = false;

	public boolean getHasNextPage() {
		return this.hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	public int getPageAmount() {
		int remainder = itemAmount % pageSize;
		if (remainder == 0)
			return itemAmount / pageSize;
		else
			return itemAmount / pageSize + 1;
	}

	/**
	 * 功能说明：页面编号，从1开始<br>
	 * 创建时间：2006-1-18 14:57:09<br>
	 * 
	 * @return
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	/**
	 * 功能说明：总记录数<br>
	 * 创建时间：2006-1-18 14:57:45<br>
	 * 
	 * @return
	 */
	public int getItemAmount() {
		return itemAmount;
	}

	public void setItemAmount(int itemAmount) {
		this.itemAmount = itemAmount;
	}

	/**
	 * 功能说明：页面大小<br>
	 * 创建时间：2006-1-18 14:58:11<br>
	 * 
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 功能说明：页开始编号<br>
	 * 创建时间：2006-1-18 14:58:15<br>
	 * 
	 * @return
	 */
	public int getStartNum() {
		return startNum;
	}

	public void setStartNum(int startNum) {
		this.startNum = startNum;
	}

	/**
	 * 功能说明：页结束编号<br>
	 * 创建时间：2006-1-18 14:58:19<br>
	 * 
	 * @return
	 */
	public int getEndNum() {
		return endNum;
	}

	/**
	 * 功能说明：是否第一页<br>
	 * 创建时间：2006-1-18 14:58:32<br>
	 * 
	 * @return
	 */
	public boolean isFirstPage() {
		return pageIndex == 1;
	}

	/**
	 * 功能说明：是否最后一页<br>
	 * 创建时间：2006-1-18 14:58:38<br>
	 * 
	 * @return
	 */
	public boolean isLastPage() {
		return getPageAmount() > pageIndex;
	}

	public int getBeginIndex() {
		return (pageIndex - 1) * pageSize + 1;
	}
	
	public int getEndIndex() {
		return pageIndex * pageSize + 1;
	}
}
