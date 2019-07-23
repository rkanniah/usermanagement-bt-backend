package com.rk.usermanagement.exception;

public class ResourceDatabaseOperationException extends RuntimeException {

	private static final long serialVersionUID = 6381761296962258382L;

	public ResourceDatabaseOperationException() {
		super();
	}

	public ResourceDatabaseOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceDatabaseOperationException(String message) {
		super(message);
	}

	public ResourceDatabaseOperationException(Throwable cause) {
		super(cause);
	}
}