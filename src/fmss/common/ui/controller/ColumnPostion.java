package fmss.common.ui.controller;


/**
 * ��˵��: ������������ ��һ�� QUERY DATA �� �ж���������λ��
 *        ��Ҫ �������� �����ݷ���ʱ�Զ����е� ��ʱλ��<br>
 * ����ʱ��: 2009-2-3 ����09:27:30<br>
 * 
 * @author �����<br>
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
