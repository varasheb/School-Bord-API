package com.school.sba.exception;

public class AdminFoundException extends RuntimeException{
	private String message;

	public AdminFoundException(String message) {
		this.message = message;
	}
	@Override
	public String getMessage() {
		return message;
	}
}
