package fmss.services;

import java.util.List;

public class LoggingRoleDifference extends LoggingDifference {
	public LoggingRoleDifference(String desc, String[] before, String[] after) {
		super(desc, before, after);
	}

	public LoggingRoleDifference(String desc, List before, List after) {
		super(desc, before, after);
	}

	public String[] fetchAuthName(String[] array) {
		return AuthorityNameFetcher.fetchRoleName(array);
	}
}