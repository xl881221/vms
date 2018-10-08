package fmss.common.ui.controller;


/**
 * 类说明:<br>
 * 创建时间: 2009-2-18 下午04:23:12<br>
 * 
 * @author 杨旭飞<br>
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
