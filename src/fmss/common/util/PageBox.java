package fmss.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-27 ����02:36:13
 * @����: [PageBox]��ҳ����
 */
public class PageBox{

	private List pageList;// �����б�
	private PageObject pageObject;// ���ݶ���

	/**
	 * <p>��������: getPageList|����:ȡ�������б� </p>
	 * @return
	 */
	public List getPageList(){
		return pageList == null ? new ArrayList() : pageList;
	}

	/**
	 * <p>��������: setPageList|����:���������б� </p>
	 * @param pageList
	 */
	public void setPageList(List pageList){
		this.pageList = pageList;
	}

	/**
	 * <p>��������: getPageObject|����: ȡ�����ݶ���</p>
	 * @return
	 */
	public PageObject getPageObject(){
		return pageObject;
	}

	/**
	 * <p>��������: setPageObject|����:�������ݶ��� </p>
	 * @param pageObject
	 */
	public void setPageObject(PageObject pageObject){
		this.pageObject = pageObject;
	}
}
