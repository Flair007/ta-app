package com.cerner.talentacquisition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cerner.talentacquisition.model.Interviewer;

@Repository
public interface InterviewerRepository extends JpaRepository<Interviewer, Long> {

}
