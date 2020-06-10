package com.ibotta.anagram.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ibotta.anagram.exception.AnagramException;
import com.ibotta.anagram.exception.InvalidWordException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class AnagramExceptionAdvice {

	@ResponseBody
	@ExceptionHandler(AnagramException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	String serviceExceptionHandler(AnagramException ex) {
		log.error("Unexpected exception", ex);
		return ex.getMessage();
	}

	@ResponseBody
	@ExceptionHandler(InvalidWordException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String serviceExceptionHandler(InvalidWordException ex) {
		log.warn("invalid word", ex);
		return ex.getMessage();
	}

}
