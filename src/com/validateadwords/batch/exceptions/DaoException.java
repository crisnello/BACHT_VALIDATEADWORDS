package com.validateadwords.batch.exceptions;

public class DaoException extends Throwable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3689598115498347285L;
	private Throwable cause;
	private String msg;
	
	public Throwable getCause() {
		return cause;
	}
	
	public String getMessage() {
		return msg;
	}
	
	public DaoException(String msg) {
		this.msg = msg;
	}
	
	public DaoException(String msg,Throwable cause) {
		this.msg = msg;
		this.cause = cause;
	}

}
