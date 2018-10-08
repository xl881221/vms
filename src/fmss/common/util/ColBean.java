package fmss.common.util;

import java.util.ArrayList;
import java.util.List;

public class ColBean {
	private String name;
	private String nullable;//是否可为空
	private String deft;//默认值
	private String type;//类型
	private String length;//长度
	private String scale;//精度
	private String constraint_type;//约束类型	
	
	private List diffType;//列不同信息  0为缺列,  1为type不同,  2为length不同,  3为nullable不同
	
	private String err_nullable;//是否可为空
	private String err_deft;//默认值
	private String err_type;//类型
	private String err_length;//长度
	private String err_scale;//精度
	private String err_constraint_type;//约束类型	
	public ColBean(){
		diffType=new ArrayList();
	}
	
	public List getDiffType() {
		return diffType;
	}
	public void setDiffType(List diffType) {
		this.diffType = diffType;
	}
	public String getConstraint_type() {
		return constraint_type;
	}
	public void setConstraint_type(String constraint_type) {
		this.constraint_type = constraint_type;
	}
	public String getDeft() {
		return deft;
	}
	public void setDeft(String deft) {
		this.deft = deft;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	
	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNullable() {
		return nullable;
	}
	public void setNullable(String nullable) {
		this.nullable = nullable;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getErr_constraint_type() {
		return err_constraint_type;
	}

	public void setErr_constraint_type(String err_constraint_type) {
		this.err_constraint_type = err_constraint_type;
	}

	public String getErr_deft() {
		return err_deft;
	}

	public void setErr_deft(String err_deft) {
		this.err_deft = err_deft;
	}

	public String getErr_length() {
		return err_length;
	}

	public void setErr_length(String err_length) {
		this.err_length = err_length;
	}

	public String getErr_nullable() {
		return err_nullable;
	}

	public void setErr_nullable(String err_nullable) {
		this.err_nullable = err_nullable;
	}

	public String getErr_scale() {
		return err_scale;
	}

	public void setErr_scale(String err_scale) {
		this.err_scale = err_scale;
	}

	public String getErr_type() {
		return err_type;
	}

	public void setErr_type(String err_type) {
		this.err_type = err_type;
	}
	
	
	
}
