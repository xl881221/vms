package fmss.common.ui.controller;
/**
 * ��˵��:<br>
 * ����ʱ��: 2008-8-5 ����01:00:31<br>
 * 
 * @author �����<br>
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
	 * ����˵����ҳ���ţ���1��ʼ<br>
	 * ����ʱ�䣺2006-1-18 14:57:09<br>
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
	 * ����˵�����ܼ�¼��<br>
	 * ����ʱ�䣺2006-1-18 14:57:45<br>
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
	 * ����˵����ҳ���С<br>
	 * ����ʱ�䣺2006-1-18 14:58:11<br>
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
	 * ����˵����ҳ��ʼ���<br>
	 * ����ʱ�䣺2006-1-18 14:58:15<br>
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
	 * ����˵����ҳ�������<br>
	 * ����ʱ�䣺2006-1-18 14:58:19<br>
	 * 
	 * @return
	 */
	public int getEndNum() {
		return endNum;
	}

	/**
	 * ����˵�����Ƿ��һҳ<br>
	 * ����ʱ�䣺2006-1-18 14:58:32<br>
	 * 
	 * @return
	 */
	public boolean isFirstPage() {
		return pageIndex == 1;
	}

	/**
	 * ����˵�����Ƿ����һҳ<br>
	 * ����ʱ�䣺2006-1-18 14:58:38<br>
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
