package com.cerner.talentacquisition.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cerner.talentacquisition.model.Candidates;
import com.cerner.talentacquisition.model.Feedback;

public interface FeedbackRepository  extends JpaRepository<Feedback, Long>{

}
