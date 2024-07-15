package com.ibotta.anagram.rest;

import com.ibotta.anagram.exception.AnagramException;
import com.ibotta.anagram.exception.InvalidWordException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class AnagramExceptionAdvice {

    @ExceptionHandler(AnagramException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String serviceExceptionHandler(AnagramException ex) {
        log.error("Unexpected exception", ex);
        return ex.getMessage();
    }

    @ExceptionHandler(InvalidWordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String serviceExceptionHandler(InvalidWordException ex) {
        log.warn("invalid word", ex);
        return ex.getMessage();
    }
}
