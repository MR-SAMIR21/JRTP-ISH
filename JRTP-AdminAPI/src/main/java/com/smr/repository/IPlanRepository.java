package com.smr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smr.entity.PlanEntity;

public interface IPlanRepository extends JpaRepository<PlanEntity, Integer> {

}
