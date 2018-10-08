package fmss.services;

import org.springframework.core.NestedRuntimeException;

public class InstLevelException extends NestedRuntimeException{
	public InstLevelException(String msg) {
		super(msg);
	}
	
	public InstLevelException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}