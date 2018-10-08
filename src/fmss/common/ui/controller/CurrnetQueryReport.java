package fmss.common.ui.controller;


/**
 * ��˵��: ������ǰSESSION �еı�����Ϣ<br>
 * ����ʱ��: 2008-10-29 ����10:52:36<br>
 * 
 * @author �����<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class CurrnetQueryReport implements Cloneable{
	
	private static final long serialVersionUID = 7991552226614088258L; 
	public CurrnetQueryReport(){
		
	}
	
	public CurrnetQueryReport(FreeQueryForm freeQueryForm,int pageSize,int curpage){
		this.freeQueryForm = freeQueryForm;
		this.pageSize = pageSize;
		this.curpage = curpage;
	}
	
	private int pageSize;
	private int curpage;
	/**
	 * ��ǰ����FORM
	 */
	private FreeQueryForm freeQueryForm;

	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getCurpage() {
		return curpage;
	}
	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}

	

	
	public  Object clone() throws CloneNotSupportedException {
		return new CurrnetQueryReport((FreeQueryForm)this.freeQueryForm.clone(),this.pageSize,this.curpage);
	}

	public FreeQueryForm getFreeQueryForm() {
		return freeQueryForm;
	}

	public void setFreeQueryForm(FreeQueryForm freeQueryForm) {
		this.freeQueryForm = freeQueryForm;
	}
	

}
