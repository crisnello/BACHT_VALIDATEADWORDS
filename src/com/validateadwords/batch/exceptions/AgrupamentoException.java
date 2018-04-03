package com.validateadwords.batch.exceptions;

public class AgrupamentoException extends Throwable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6723084757355625445L;
	private Throwable cause;
	private String msg;
	
	public Throwable getCause() {
		return cause;
	}
	
	public String getMessage() {
		return msg;
	}
	
	public AgrupamentoException(String msg) {
		this.msg = msg;
	}
	
	public AgrupamentoException(String msg,Throwable cause) {
		this.msg = msg;
		this.cause = cause;
	}

}
