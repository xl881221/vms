package fmss.common.ui.controller;


/**
 * ��˵��:<br>
 * ����ʱ��: 2009-2-18 ����04:23:12<br>
 * 
 * @author �����<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public abstract class Rule {
	private String img ;

	public String getImg() {
		return "user-image/"+img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	protected abstract boolean isEq(CellValue value);
}
