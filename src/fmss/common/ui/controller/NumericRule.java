package fmss.common.ui.controller;


/**
 * ��˵��:<br>
 * ����ʱ��: 2009-2-18 ����04:29:51<br>
 * 
 * @author �����<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class NumericRule extends Rule{

	private double startValue;
	
	private double endValue;
	
	protected boolean isEq(CellValue value) {

		double v = ((NumericValue)value).getNumericValue();
		
		if(startValue == 0 && endValue!=0){
			if(v >= endValue){
				return true;
			}else{
				return false;
			}
		}else if(endValue ==0 && startValue!=0){
			if(v<=startValue){
				return true;
			}else{
				return false;
			}
		}else{
			if(startValue <= v &&  endValue >= v){
				return true;
			}else{
				return false;
			}
		}

	}

	public double getStartValue() {
		return startValue;
	}

	public void setStartValue(double startValue) {
		this.startValue = startValue;
	}

	public double getEndValue() {
		return endValue;
	}

	public void setEndValue(double endValue) {
		this.endValue = endValue;
	}

}
