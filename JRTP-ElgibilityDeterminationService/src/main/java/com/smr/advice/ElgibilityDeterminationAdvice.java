package com.smr.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ElgibilityDeterminationAdvice {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionInfo> handleAllExceptions(Exception e){
		ExceptionInfo info = new ExceptionInfo();
		info.setMessage(e.getMessage());
		info.setCode(3000);
		return new ResponseEntity(info,HttpStatus.INTERNAL_SERVER_ERROR);

	}
}
