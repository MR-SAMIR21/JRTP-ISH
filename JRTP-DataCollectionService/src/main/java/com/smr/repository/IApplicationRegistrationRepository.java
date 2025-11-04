package com.smr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smr.entity.CitizenAppRegistrationEntity;

public interface IApplicationRegistrationRepository extends JpaRepository<CitizenAppRegistrationEntity, Integer> {

}
