package com.smr.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="JR_DC_CHILDREN")
@Data
public class DcChildrenEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer childId;
	private Integer caseNo;
	private LocalDate childDOB;
	private long childSSN;
}
