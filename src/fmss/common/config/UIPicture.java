package fmss.common.config;

import org.apache.commons.lang.StringUtils;

public final class UIPicture {
	private String offsetX;
	private String offsetY;
	private String width;
	private String height;
	private String path;
	private String cssFilter;
	private String display;

	public String getCssFilter() {
		return cssFilter;
	}

	public void setCssFilter(String cssFilter) {
		this.cssFilter = cssFilter;
	}

	public String getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(String offsetX) {
		this.offsetX = offsetX;
	}

	public String getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(String offsetY) {
		this.offsetY = offsetY;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isDisplayP() {
		return (display != null && ("1".equals(display) || "yes".equals(display) || "YES".equals(display)));
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getDisplay() {
		return display;
	}

	public String toHtml() {
		StringBuffer sb = new StringBuffer("&nbsp;");
		if (isDisplayP()) {
			sb = new StringBuffer("<img src=");
			sb.append(path);
			if (StringUtils.isNotEmpty(width)) {
				sb.append(" width=").append(width);
			}
			if (StringUtils.isNotEmpty(height)) {
				sb.append(" height=").append(height);
			}
			if (StringUtils.isNotEmpty(cssFilter)) {
				sb.append(" style=\"").append(cssFilter).append("\"");
			}
			sb.append(">");
		}
		return sb.toString();
	}

	public String toString() {
		return toHtml();
	}

}