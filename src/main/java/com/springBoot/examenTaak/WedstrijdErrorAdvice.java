package com.springBoot.examenTaak;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import exceptions.DuplicateWedstrijdException;
import exceptions.WedstrijdNotFoundException;

@RestControllerAdvice
class WedstrijdErrorAdvice {
	
	@ResponseBody
	@ExceptionHandler(WedstrijdNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String wedstrijdNotFoundHandler(WedstrijdNotFoundException ex) {
		return ex.getMessage();
	}
	
	@ResponseBody
	@ExceptionHandler(DuplicateWedstrijdException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	String duplicateWedstrijdHandler(DuplicateWedstrijdException ex) {
		return ex.getMessage();
	}

}
