package fmss.services;

import java.util.List;

public class LoggingUserDifference extends LoggingDifference {
	public LoggingUserDifference(String desc, String[] before, String[] after) {
		super(desc, before, after);
	}

	public LoggingUserDifference(String desc, List before, List after) {
		super(desc, before, after);
	}

	public String[] fetchAuthName(String[] array) {
		return AuthorityNameFetcher.fetchUserName(array);
	}
}