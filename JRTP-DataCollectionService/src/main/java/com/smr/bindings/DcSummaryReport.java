package com.smr.bindings;

import java.util.List;

import lombok.Data;

@Data
public class DcSummaryReport {

	private EducationInputs educationDetails;
	private List<ChildInputs> childDetails;
	private IncomeInputs incomeDetails;
	private CitizenAppRegistrationInputs citizenDetails;;
	private String planName;
}
