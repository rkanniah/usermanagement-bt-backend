package com.rk.usermanagement.exception;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 9176024510944049631L;

	public ResourceNotFoundException() {
		super();
	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(Throwable cause) {
		super(cause);
	}
}