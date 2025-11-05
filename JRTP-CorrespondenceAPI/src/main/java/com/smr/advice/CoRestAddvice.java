package com.smr.advice;

import java.io.FileNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CoRestAddvice {

	@ExceptionHandler(FileNotFoundException.class)
	public ResponseEntity<ExceptionInfo> handleFileNotFoundException(FileNotFoundException fnf){
		ExceptionInfo info = new ExceptionInfo();
		info.setMessage(fnf.getMessage());
		info.setCode(3000);
		return new ResponseEntity(info,HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionInfo> handleAllExceptions(Exception e){
		ExceptionInfo info = new ExceptionInfo();
		info.setMessage(e.getMessage());
		info.setCode(3000);
		return new ResponseEntity(info,HttpStatus.INTERNAL_SERVER_ERROR);

	}
}
