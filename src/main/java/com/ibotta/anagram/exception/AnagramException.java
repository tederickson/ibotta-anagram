package com.ibotta.anagram.exception;

import java.io.IOException;

public class AnagramException extends IOException {

	private static final long serialVersionUID = 677247787923829916L;

	public AnagramException() {
	}

	public AnagramException(String message) {
		super(message);
	}

	public AnagramException(Throwable cause) {
		super(cause);
	}

	public AnagramException(String message, Throwable cause) {
		super(message, cause);
	}

}
