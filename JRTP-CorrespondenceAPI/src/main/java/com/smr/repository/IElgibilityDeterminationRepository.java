package com.smr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smr.entity.ElgibilityDetailsEntity;

public interface IElgibilityDeterminationRepository extends JpaRepository<ElgibilityDetailsEntity, Integer> {

	public ElgibilityDetailsEntity findByCaseNo(int caseNo);
}
