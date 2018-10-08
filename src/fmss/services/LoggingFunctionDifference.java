package fmss.services;

import java.util.List;

public class LoggingFunctionDifference extends LoggingDifference {
	public LoggingFunctionDifference(String desc, String[] before, String[] after) {
		super(desc, before, after);
	}

	public LoggingFunctionDifference(String desc, List before, List after) {
		super(desc, before, after);
	}

	public String[] fetchAuthName(String[] array) {
		return AuthorityNameFetcher.fetchFunctionName(array);
	}
}