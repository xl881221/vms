package fmss.action.base;

/**
 * 用于灵活设置的下拉框对象
 * 
 * @author Larry
 */
public class SelectTag {

	private String key;
	private String value;

	public SelectTag() {
	}

	public SelectTag(String key, String value) {
		this.value = value;
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}