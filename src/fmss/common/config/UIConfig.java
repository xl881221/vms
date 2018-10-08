package fmss.common.config;

public class UIConfig {

	public static final String FIXED_RESOURCE_PATH = "";

	private static UIPicture loginLogoPic = new UIPicture();

	private static UIPicture topLogoPic = new UIPicture();

	public UIPicture getLoginLogoPic() {
		return loginLogoPic;
	}

	public UIPicture getTopLogoPic() {
		return topLogoPic;
	}

	public void setTopLogoPic(UIPicture topLogoPic) {
		UIConfig.topLogoPic = topLogoPic;
	}

	public void setLoginLogoPic(UIPicture loginLogoPic) {
		UIConfig.loginLogoPic = loginLogoPic;
	}

	private static UIConfig instance = null;

	public synchronized static UIConfig getInstance() {
		if (instance == null)
			instance = new UIConfig();
		return instance;
	}
}
