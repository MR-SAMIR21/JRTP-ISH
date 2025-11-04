package com.smr.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="JR_DC_CASES")
@Data
public class DcCaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
//	private Integer caseId;
	private Integer caseNo;
	private Integer appId;
	private Integer planId;
	
}
