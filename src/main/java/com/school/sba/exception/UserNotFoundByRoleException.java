package com.school.sba.exception;

public class UserNotFoundByRoleException extends RuntimeException {
	private String message;

	public UserNotFoundByRoleException(String message) {
		super();
		this.message = message;
	}
	@Override
	public String getMessage() {
		return message;
	}
}
