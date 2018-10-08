package fmss.action.base;

import org.springframework.core.NestedRuntimeException;

public class AuditException extends NestedRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -112911508836052576L;

	public AuditException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	public AuditException(String msg, Throwable throwable) {
		super(msg, throwable);
		// TODO Auto-generated constructor stub
	}

}
