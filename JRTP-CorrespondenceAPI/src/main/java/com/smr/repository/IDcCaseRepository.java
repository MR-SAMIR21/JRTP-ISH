package com.smr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smr.entity.DcCaseEntity;

public interface IDcCaseRepository extends JpaRepository<DcCaseEntity ,Integer> {

	public Integer findByCaseNo(int caseNo);
}
