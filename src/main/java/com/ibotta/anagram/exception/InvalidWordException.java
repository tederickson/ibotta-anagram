package com.ibotta.anagram.exception;

public class InvalidWordException extends AnagramException {

	private static final long serialVersionUID = -7974069081287502181L;

	public InvalidWordException() {
	}

	public InvalidWordException(String message) {
		super(message);
	}

	public InvalidWordException(Throwable cause) {
		super(cause);
	}

	public InvalidWordException(String message, Throwable cause) {
		super(message, cause);
	}

}
