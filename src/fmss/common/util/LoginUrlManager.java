package fmss.common.util;

/**
 * <p>
 * 版权�?��:(C)2003-2010 北京中软融鑫计算机系统有限公�?
 * </p>
 * 
 * @作�?:
 * @日期:
 * @描述:
 */
public class LoginUrlManager {

	private static LoginUrlManager instance = new LoginUrlManager();

	private LoginUrlManager() {

	}

	private String defaultPort;

	public String getDefaultPort() {
		return defaultPort;
	}

	public String getDefaultIPAddress() {
		return defaultIPAddress;
	}

	private String defaultIPAddress;

	public static LoginUrlManager getInstance() {
		return LoginUrlManager.instance;
	}

	public void setUrlInfo(String sUrl) {
		if (this.defaultIPAddress == null && this.defaultPort == null) {

			int lastIndex = sUrl.lastIndexOf(":");

			String sDefaultPort = sUrl.substring(lastIndex + 1, sUrl.length());
			String sDefaultIPAddress = sUrl.substring(0, lastIndex);

			this.defaultIPAddress = sDefaultIPAddress
					.substring(sDefaultIPAddress.lastIndexOf("/") + 1);
			this.defaultPort = sDefaultPort.substring(0, sDefaultPort
					.indexOf("/"));
		}
	}
}
