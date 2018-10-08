package fmss.common.config;

public class PrivateConfig {
	private static String canMetaConfig;

	public static String getCanMetaConfig() {
		return canMetaConfig;
	}

	public void setCanMetaConfig(String canMetaConfig) {
		PrivateConfig.canMetaConfig = canMetaConfig;
	}

	public static boolean isCanMetaConfig() {
		return (canMetaConfig != null && ("1".equals(canMetaConfig) || "yes".equalsIgnoreCase(canMetaConfig) || "true"
				.equalsIgnoreCase(canMetaConfig)));
	}
}
