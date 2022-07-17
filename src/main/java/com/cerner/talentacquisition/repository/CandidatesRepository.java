package com.cerner.talentacquisition.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cerner.talentacquisition.model.Candidates;

public interface CandidatesRepository extends JpaRepository<Candidates, Long> {
	
	List<Candidates> findByBusinessUnit(String id);
	//List<Candidates> findByAcrossBusinessUnit(String acrossBu);
	
	List<Candidates> findByInterviewerId(long id);

}
