/*
 *Created on 2005-11-25
 */
package fmss.common.ui.controller;


/**
 * 系统的根exception
 * @author pesome
 * @email:pesome@163.com
 */
public class AnalyzingException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1949230544234573722L;
	private int errorCode=-1;
	
	public int getErrorCode(){
		return errorCode;
	}
	
	public AnalyzingException(String msg){
		super(msg);
	}
	
	public AnalyzingException(String msg,int errorCode){
		super(msg);
		this.errorCode=errorCode;
	}
	
	public AnalyzingException(String msg, Throwable ex,int errorCode){
		super(msg,ex);
	}

}
