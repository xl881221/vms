package fmss.common.util;

import java.util.ArrayList;
import java.util.List;

public class ColBean {
	private String name;
	private String nullable;//�Ƿ��Ϊ��
	private String deft;//Ĭ��ֵ
	private String type;//����
	private String length;//����
	private String scale;//����
	private String constraint_type;//Լ������	
	
	private List diffType;//�в�ͬ��Ϣ  0Ϊȱ��,  1Ϊtype��ͬ,  2Ϊlength��ͬ,  3Ϊnullable��ͬ
	
	private String err_nullable;//�Ƿ��Ϊ��
	private String err_deft;//Ĭ��ֵ
	private String err_type;//����
	private String err_length;//����
	private String err_scale;//����
	private String err_constraint_type;//Լ������	
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
