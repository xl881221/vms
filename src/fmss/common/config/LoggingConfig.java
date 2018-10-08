package fmss.common.config;

public class LoggingConfig {
	private static String loggingParamChanges;

	public static String getLoggingParamChanges() {
		return loggingParamChanges;
	}

	public void setLoggingParamChanges(String loggingParamChanges) {
		LoggingConfig.loggingParamChanges = loggingParamChanges;
	}

	public static boolean isLoggingParamChanges() {
		return (loggingParamChanges != null && ("1".equals(loggingParamChanges)
				|| "yes".equalsIgnoreCase(loggingParamChanges) || "true".equalsIgnoreCase(loggingParamChanges)));
	}

	private static String loggingConfigedName;

	public static String getLoggingConfigedName() {
		return loggingConfigedName;
	}

	public void setLoggingConfigedName(String loggingConfigedName) {
		LoggingConfig.loggingConfigedName = loggingConfigedName;
	}

	public static boolean isloggingConfigedName() {
		return (loggingConfigedName != null && ("1".equals(loggingConfigedName)
				|| "yes".equalsIgnoreCase(loggingConfigedName) || "true".equalsIgnoreCase(loggingConfigedName)));
	}
}
