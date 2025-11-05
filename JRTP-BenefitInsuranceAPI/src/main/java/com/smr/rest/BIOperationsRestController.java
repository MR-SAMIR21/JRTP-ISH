package com.smr.rest;

import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smr.service.IBenifitInsuranceMgmtService;

@RestController
@RequestMapping("/bi-api")
public class BIOperationsRestController {
	
	@Autowired
	private IBenifitInsuranceMgmtService biService;

	@GetMapping("/send")
	public ResponseEntity<String> SendAmount()throws Exception{
		//use Service
		JobExecution execution = biService.sendAmountToBenificries();
		return new ResponseEntity<String>(execution.getExitStatus().getExitDescription(),HttpStatus.OK);
		
	}
}
