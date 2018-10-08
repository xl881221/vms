package fmss.common.ui.controller;

import java.util.List;
import java.util.Set;


/**
 * 类说明:<br>
 * 创建时间: 2009-1-7 上午11:18:35<br>
 * 
 * @author 杨旭飞<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class FreeQueryGroupModel implements Comparable{
	public CellValue getCv() {
		return cv;
	}
	public void setCv(CellValue cv) {
		this.cv = cv;
	}
	public FreeQueryGroupModel(int startRow,int totalCount,CellValue cv){
		this.startRow = startRow;
		this.totalCount = totalCount;
		this.cv = cv;
	}
	private CellValue cv;
	private int startRow;
	private int totalCount;
	private int modStartRow;
	private int modEndRow;
	private int currentStartRow;
	private CellValue totalCellValue;
	private boolean hasDipsplay = false;
	private boolean hasChild;
	private Set childSet;
	private List childGroupSet;
	private ColMeta thisMeta;
	private ColMeta nextMeta;
	private Row totalRow;
	public Row getTotalRow() {
		return totalRow;
	}
	public void setTotalRow(Row totalRow) {
		this.totalRow = totalRow;
	}
	public ColMeta getNextMeta() {
		return nextMeta;
	}
	public void setNextMeta(ColMeta nextMeta) {
		this.nextMeta = nextMeta;
	}
	public CellValue getTotalCellValue() {
		return totalCellValue;
	}
	public void setTotalCellValue(CellValue totalCellValue) {
		this.totalCellValue = totalCellValue;
	}
	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getModStartRow() {
		return modStartRow;
	}
	public void setModStartRow(int modStartRow) {
		this.modStartRow = modStartRow;
	}
	public int getModEndRow() {
		return modEndRow;
	}
	public void setModEndRow(int modEndRow) {
		this.modEndRow = modEndRow;
	}
	public int compareTo(Object o) {
		return (((FreeQueryGroupModel) o).getTotalCellValue()).compareTo(this.getTotalCellValue());
	}

	public Set getChildSet() {
		return childSet;
	}
	public void setChildSet(Set childSet) {
		this.childSet = childSet;
	}
	public boolean isHasChild() {
		return hasChild;
	}
	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}
	public ColMeta getThisMeta() {
		return thisMeta;
	}
	public void setThisMeta(ColMeta thisMeta) {
		this.thisMeta = thisMeta;
	}
	public List getChildGroupSet() {
		return childGroupSet;
	}
	public void setChildGroupSet(List childGroupSet) {
		this.childGroupSet = childGroupSet;
	}
	public boolean isHasDipsplay() {
		return hasDipsplay;
	}
	public void setHasDipsplay(boolean hasDipsplay) {
		this.hasDipsplay = hasDipsplay;
	}
	public int getCurrentStartRow() {
		return currentStartRow;
	}
	public void setCurrentStartRow(int currentStartRow) {
		this.currentStartRow = currentStartRow;
	}

}
