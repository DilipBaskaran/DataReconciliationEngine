package com.techmahindra.datareconciliation.exceptions;

public class ApplicationException extends Exception {

	String msg;
	public ApplicationException(String msg){
		super(msg);
		this.msg = msg;
	}
	
}
