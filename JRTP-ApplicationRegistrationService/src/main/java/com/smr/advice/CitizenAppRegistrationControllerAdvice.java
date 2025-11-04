package com.smr.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.smr.exceptions.InvalidSSNException;

@RestControllerAdvice
public class CitizenAppRegistrationControllerAdvice {

	@ExceptionHandler(InvalidSSNException.class)
	public ResponseEntity<ExceptionInfo> handleInvalidSSN(InvalidSSNException ie){
		ExceptionInfo info = new ExceptionInfo();
		info.setMessage(ie.getMessage());
		info.setCode(3000);
		return new ResponseEntity(info,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionInfo> handleAllException(Exception e){
		ExceptionInfo info = new ExceptionInfo();
		info.setMessage(e.getMessage());
		info.setCode(3000);
		return new ResponseEntity(info,HttpStatus.INTERNAL_SERVER_ERROR);

	}
}
