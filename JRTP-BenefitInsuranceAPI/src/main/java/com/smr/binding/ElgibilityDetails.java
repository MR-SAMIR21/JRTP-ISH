package com.smr.binding;

import lombok.Data;

@Data
public class ElgibilityDetails {

	private Integer caseNo;
	private String holderName;
	private Long holderSSN;
	private String planName;
	private String planStatus;
	private Double benifitAmt;
	private String bankName;
	private Long accountNumber;

}
