package com.smr.service;

import org.springframework.batch.core.JobExecution;

public interface IBenifitInsuranceMgmtService {

	public JobExecution sendAmountToBenificries() throws Exception;
}
