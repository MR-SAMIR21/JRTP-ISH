package com.smr.bindings;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ElgibilityDetailsOutput {

	private String holdername;
	private String planName;
	private String planStatus;
	private LocalDate planStartDate;
	private LocalDate planEndDate;
	private Double benifitAmt;
	private String denialReason;
}
