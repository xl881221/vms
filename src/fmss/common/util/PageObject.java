package fmss.common.util;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-27 ����02:37:53
 * @����: [PageObject]��ҳ���õ����ݶ���
 */
public class PageObject{

	private int pageIndex;// ҳ��
	private int itemAmount;// ������
	private int pageSize = 1;// ҳ���С
	private int startNum;// ��ʼ��¼��
	private int endNum;// ������¼��

	/**
	 * <p>��������: getPageAmount|����:ȡ�������� </p>
	 * @return
	 */
	public int getPageAmount(){
		int remainder = itemAmount % pageSize;
		if(remainder == 0)
			return itemAmount / pageSize;
		else
			return itemAmount / pageSize + 1;
	}

	/**
	 * <p>��������: getPageIndex|����:ҳ���ţ���1��ʼ</p>
	 * @return
	 */
	public int getPageIndex(){
		return pageIndex;
	}

	/**
	 * <p>��������: setPageIndex|����:����ҳ������ </p>
	 * @param pageIndex
	 */
	public void setPageIndex(int pageIndex){
		this.pageIndex = pageIndex;
	}

	/**
	 * <p>��������: getItemAmount|����:ȡ���ܼ�¼�� </p>
	 * @return
	 */
	public int getItemAmount(){
		return itemAmount;
	}

	/**
	 * <p>��������: setItemAmount|����: �����ܼ�¼��</p>
	 * @param itemAmount
	 */
	public void setItemAmount(int itemAmount){
		this.itemAmount = itemAmount;
	}

	/**
	 * <p>��������: getPageSize|����: ȡ��ҳ���С</p>
	 * @return
	 */
	public int getPageSize(){
		return pageSize;
	}

	/**
	 * <p>��������: setPageSize|����:����ҳ���С </p>
	 * @param pageSize
	 */
	public void setPageSize(int pageSize){
		this.pageSize = pageSize;
	}

	/**
	 * <p>��������: getStartNum|����:ȡ��ҳ��ʼ��� </p>
	 * @return
	 */
	public int getStartNum(){
		return startNum;
	}

	/**
	 * <p>��������: setStartNum|����:����ҳ��ʼ��� </p>
	 * @param startNum
	 */
	public void setStartNum(int startNum){
		this.startNum = startNum;
	}

	/**
	 * <p>��������: getEndNum|����:ȡ��ҳ������� </p>
	 * @return
	 */
	public int getEndNum(){
		return endNum;
	}

	/**
	 * <p>��������: isFirstPage|����:�Ƿ��һҳ </p>
	 * @return
	 */
	public boolean isFirstPage(){
		return pageIndex == 1;
	}

	/**
	 * <p>��������: isLastPage|����:�Ƿ����һҳ </p>
	 * @return
	 */
	public boolean isLastPage(){
		return getPageAmount() > pageIndex;
	}

	/**
	 * <p>��������: getBeginIndex|����:ȡ�ÿ�ʼ������ </p>
	 * @return
	 */
	public int getBeginIndex(){
		return (pageIndex - 1) * pageSize + 1;
	}
}
