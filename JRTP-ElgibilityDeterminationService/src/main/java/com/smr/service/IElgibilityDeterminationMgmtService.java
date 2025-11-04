package com.smr.service;

import com.smr.bindings.ElgibilityDetailsOutput;

public interface IElgibilityDeterminationMgmtService {

	public ElgibilityDetailsOutput determineElgibility(Integer caseNo);
	
}
