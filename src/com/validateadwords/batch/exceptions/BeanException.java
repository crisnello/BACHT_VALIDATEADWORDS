package com.validateadwords.batch.exceptions;

public class BeanException extends Throwable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -36895998347285L;
	private Throwable cause;
	private String msg;
	
	public Throwable getCause() {
		return cause;
	}
	
	public String getMessage() {
		return msg;
	}
	
	public BeanException(String msg) {
		this.msg = msg;
	}
	
	public BeanException(String msg,Throwable cause) {
		this.msg = msg;
		this.cause = cause;
	}

}
