package com.cerner.talentacquisition.controller;

import java.util.List;
import java.util.Optional;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cerner.talentacquisition.model.Candidates;
import com.cerner.talentacquisition.model.Interviewer;
import com.cerner.talentacquisition.repository.BusinessUnitRepository;
import com.cerner.talentacquisition.repository.CandidatesRepository;
import com.cerner.talentacquisition.repository.InterviewerRepository;

@RestController
@RequestMapping("/api")
public class TalentacquisitionController {
	
	@Autowired
	private InterviewerRepository intervieRepository;
	
	public InterviewerRepository getIntervieRepository() {
		return intervieRepository;
	}
	
	@Autowired
	private CandidatesRepository candRepository;
	
	public CandidatesRepository getCandRepository() {
		return candRepository;
	}
	
	@Autowired
	private BusinessUnitRepository buRepository;
	
	public BusinessUnitRepository getBuRepository() {
		return buRepository;
	}
	@ApiOperation(value = "post request for interviewer")
	@PostMapping("/register")
	Interviewer createOrSaveInterviewer(@RequestBody Interviewer newInterviewer) {
		return intervieRepository.save(newInterviewer);
	}
	
	@PostMapping("/candidates")
	Candidates createOrSaveCandidates(@RequestBody Candidates newCandidates) {
		return candRepository.save(newCandidates);
	}
	
	@GetMapping("/reports/{bu}")
	public List<Candidates> findByBusinessUnit(@PathVariable("bu") String bu) {
		List<Candidates> reportsData = buRepository.findByBusinessUnit(bu);
		if (!reportsData.isEmpty()) {
			return reportsData;
		} else {
			return null;
		}
	}

	@PutMapping("/candidates/{id}")
	public ResponseEntity<Candidates> updateTutorial(@PathVariable("id") long id, @RequestBody Candidates tutorial) {
		Optional<Candidates> interviewData = candRepository.findById(id);
		if (interviewData.isPresent()) {
			Candidates _interview = interviewData.get();
			_interview.setCandiateStatus(tutorial.getCandiateStatus());
			return new ResponseEntity<>(candRepository.save(_interview), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
