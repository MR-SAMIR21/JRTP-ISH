package com.smr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smr.entity.DcEducationEntity;
import com.smr.entity.DcIncomeEntity;

public interface IDcEducationRepository extends JpaRepository<DcEducationEntity, Integer> {

	public DcEducationEntity findByCaseNo(int caseNo);
}
