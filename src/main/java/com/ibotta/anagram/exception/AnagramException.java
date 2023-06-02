package com.ibotta.anagram.exception;

import java.io.IOException;
import java.io.Serial;

public class AnagramException extends IOException {

    @Serial
    private static final long serialVersionUID = 677247787923829916L;

    public AnagramException(String message) {
        super(message);
    }

}
