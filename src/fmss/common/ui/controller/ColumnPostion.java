package fmss.common.ui.controller;


/**
 * 类说明: 此类用于描述 在一个 QUERY DATA 中 列对象所处的位置
 *        主要 用于描述 在数据分析时自定义列的 临时位置<br>
 * 创建时间: 2009-2-3 上午09:27:30<br>
 * 
 * @author 杨旭飞<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class ColumnPostion {
	public ColumnPostion(String colType){
		this.colType = colType;
		if(AnalyseCst.ADD_POSTION_START.equals(colType)){
			this.isStart = true;
		}else if(AnalyseCst.ADD_POSTION_END.equals(colType)){
			this.isEnd = true;
		}else{
			
		}
	}
	
	private String colType;

	private boolean isEnd = false;
	
	private boolean isStart = false;
	
	public String getcolType() {
		return colType;
	}

	public void setcolType(String colType) {
		this.colType = colType;
	}

	public boolean isEnd() {
		return isEnd;
	}

	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}

	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}
	
}
