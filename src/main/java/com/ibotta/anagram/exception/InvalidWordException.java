package com.ibotta.anagram.exception;

import java.io.Serial;

public class InvalidWordException extends AnagramException {

    @Serial
    private static final long serialVersionUID = -7974069081287502181L;


    public InvalidWordException(String message) {
        super(message);
    }


}
