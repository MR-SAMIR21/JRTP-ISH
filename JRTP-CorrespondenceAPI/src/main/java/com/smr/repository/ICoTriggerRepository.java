package com.smr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smr.entity.CoTriggersEntity;

public interface ICoTriggerRepository extends JpaRepository<CoTriggersEntity, Integer> {

	public List<CoTriggersEntity> findByTriggerStatus(String status);
	public CoTriggersEntity findByCaseNo(int caseNo);
	   
}
