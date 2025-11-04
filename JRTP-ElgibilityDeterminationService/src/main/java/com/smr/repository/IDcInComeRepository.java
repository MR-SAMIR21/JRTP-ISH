package com.smr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smr.entity.DcIncomeEntity;

public interface IDcInComeRepository extends JpaRepository<DcIncomeEntity, Integer> {

	public DcIncomeEntity findByCaseNo(int caseNo);
}
