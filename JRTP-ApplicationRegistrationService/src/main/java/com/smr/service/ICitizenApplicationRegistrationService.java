package com.smr.service;

import com.smr.bindings.CitizenAppRegistrationInputs;
import com.smr.exceptions.InvalidSSNException;

public interface ICitizenApplicationRegistrationService {

	public Integer registerCitizenApplication(CitizenAppRegistrationInputs inputs)throws InvalidSSNException;
}
