package com.cerner.talentacquisition.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cerner.talentacquisition.model.Candidates;

public interface BusinessUnitRepository extends JpaRepository<Candidates, String> {

	List<Candidates> findByBusinessUnit(String businessUnit);
}
