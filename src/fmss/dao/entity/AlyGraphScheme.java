package fmss.dao.entity;

/**
 * AlyGraphScheme entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AlyGraphScheme implements java.io.Serializable {

	// Fields

	private Long id;
	private String graphCode;
	private String schemeCode;
	private String parameter1;
	private String parameter2;
	private String parameter3;
	private String value1;
	private String value2;
	private String value3;
	private String graphcontext;
	private String graphtype;
	private String graphformula;
	private String graphmode;
	private String grapdescription;

	// Constructors

	/** default constructor */
	public AlyGraphScheme() {
	}

	/** minimal constructor */
	public AlyGraphScheme(String graphCode, String schemeCode) {
		this.graphCode = graphCode;
		this.schemeCode = schemeCode;
	}

	/** full constructor */
	public AlyGraphScheme(String graphCode, String schemeCode,
			String parameter1, String parameter2, String parameter3,
			String value1, String value2, String value3, String graphcontext,
			String graphtype, String graphformula, String graphmode,
			String grapdescription) {
		this.graphCode = graphCode;
		this.schemeCode = schemeCode;
		this.parameter1 = parameter1;
		this.parameter2 = parameter2;
		this.parameter3 = parameter3;
		this.value1 = value1;
		this.value2 = value2;
		this.value3 = value3;
		this.graphcontext = graphcontext;
		this.graphtype = graphtype;
		this.graphformula = graphformula;
		this.graphmode = graphmode;
		this.grapdescription = grapdescription;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGraphCode() {
		return this.graphCode;
	}

	public void setGraphCode(String graphCode) {
		this.graphCode = graphCode;
	}

	public String getSchemeCode() {
		return this.schemeCode;
	}

	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}

	public String getParameter1() {
		return this.parameter1;
	}

	public void setParameter1(String parameter1) {
		this.parameter1 = parameter1;
	}

	public String getParameter2() {
		return this.parameter2;
	}

	public void setParameter2(String parameter2) {
		this.parameter2 = parameter2;
	}

	public String getParameter3() {
		return this.parameter3;
	}

	public void setParameter3(String parameter3) {
		this.parameter3 = parameter3;
	}

	public String getValue1() {
		return this.value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return this.value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getValue3() {
		return this.value3;
	}

	public void setValue3(String value3) {
		this.value3 = value3;
	}

	public String getGraphcontext() {
		return this.graphcontext;
	}

	public void setGraphcontext(String graphcontext) {
		this.graphcontext = graphcontext;
	}

	public String getGraphtype() {
		return this.graphtype;
	}

	public void setGraphtype(String graphtype) {
		this.graphtype = graphtype;
	}

	public String getGraphformula() {
		return this.graphformula;
	}

	public void setGraphformula(String graphformula) {
		this.graphformula = graphformula;
	}

	public String getGraphmode() {
		return this.graphmode;
	}

	public void setGraphmode(String graphmode) {
		this.graphmode = graphmode;
	}

	public String getGrapdescription() {
		return this.grapdescription;
	}

	public void setGrapdescription(String grapdescription) {
		this.grapdescription = grapdescription;
	}

}