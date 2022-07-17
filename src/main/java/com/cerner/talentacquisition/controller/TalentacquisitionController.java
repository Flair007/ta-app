package com.cerner.talentacquisition.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cerner.talentacquisition.model.TA_User;
import com.cerner.talentacquisition.repository.TAUserRepository;
import com.cerner.talentacquisition.utils.SecurityInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cerner.talentacquisition.message.ResponseMessage;
import com.cerner.talentacquisition.model.Candidates;
import com.cerner.talentacquisition.model.Doc;
import com.cerner.talentacquisition.model.Feedback;
import com.cerner.talentacquisition.model.Interviewer;
import com.cerner.talentacquisition.repository.BusinessUnitRepository;
import com.cerner.talentacquisition.repository.CandidatesRepository;
import com.cerner.talentacquisition.repository.FeedbackRepository;
import com.cerner.talentacquisition.repository.InterviewerRepository;
import com.cerner.talentacquisition.service.DocStorageService;

@RestController
@RequestMapping("/api")
public class TalentacquisitionController {

	@Autowired
	private InterviewerRepository intervieRepository;

	@Autowired
	private CandidatesRepository candRepository;

	@Autowired
	private BusinessUnitRepository buRepository;
	
	@Autowired 
	private DocStorageService docStorageService;
	
	@Autowired
	private TAUserRepository taRepository;
	@Autowired
	private FeedbackRepository feedbackRepository;

	@PostMapping("/register")
	public ResponseEntity<Interviewer> createOrSaveInterviewer(@RequestBody Interviewer interviewer) {
		try {
			Interviewer _interviewer = intervieRepository.save(interviewer);
			return new ResponseEntity<>(_interviewer, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PostMapping("/candidates")
	public ResponseEntity<Candidates> createOrSaveCandidates(@RequestBody Candidates candidates) {
		try {
			Candidates _tutorial = candRepository.save(candidates);
			return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping("/reports/{bu}")
	public ResponseEntity<List<Candidates>> findByPublished(@PathVariable("bu") String bu) {
		try {
			List<Candidates> reportsData = buRepository.findByBusinessUnit(bu);
			if (reportsData.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(reportsData, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/candidates/{id}")
	public ResponseEntity<Candidates> updateTutorial(@PathVariable("id") long id, @RequestBody Candidates tutorial) {
		Optional<Candidates> interviewData = candRepository.findById(id);
		if (interviewData.isPresent()) {
			Candidates _interview = interviewData.get();
			_interview.setCandiateStatus(tutorial.getCandiateStatus());
			candRepository.save(_interview);
			Feedback feedback=new Feedback();
			feedback.setStatus(_interview.getCandiateStatus());
			feedback.setTime(LocalDateTime.now());
			feedback.setCandidateId(id);
			feedbackRepository.save(feedback);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
//	@PutMapping("/candidates/{id}")
//	public ResponseEntity<Candidates> updateTutorial(@PathVariable("id") long id, @RequestBody Candidates tutorial) {
//		Optional<Candidates> interviewData = candRepository.findById(id);
//		if (interviewData.isPresent()) {
//			Candidates _interview = interviewData.get();
//			_interview.setCandiateStatus(tutorial.getCandiateStatus());
//			candRepository.save(_interview);
//			Feedback feedback=new Feedback();
//			feedback.setStatus("");
//			feedback.setTime(LocalDateTime.now());
//			feedback.setCandidateId(id);
//			feedbackRepository.save(feedback);
//			return new ResponseEntity<>(HttpStatus.OK);
//		} else {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//	}
	
	@PostMapping("/uploadresume")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";
		//ResponseMessage response=new ResponseMessage();
		try {
			Doc doc =docStorageService.saveFile(file);
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message,doc.getId()));
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message,(long) 0));
		}
	}
	
	@GetMapping("/downloadresume/{fileId}")
	public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable("fileId") Integer fileId) {
		try {
			System.out.println("id:::"+fileId);
		Doc doc = docStorageService.getFile(fileId).get();
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(doc.getDocType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment:filename=\"" + doc.getDocName() + "\"")
				.body(new ByteArrayResource(doc.getData()));
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/check")
	public ResponseEntity findInterviewerorTAUser(@RequestBody SecurityInfo securityInfo) {
			try {
				TA_User ta_user = taRepository.findByEmailAndPassword(securityInfo.getEmail(), securityInfo.getPassword());
				if (ta_user==null) {

					Interviewer interviewer=intervieRepository.findByEmailIdAndPassword(securityInfo.getEmail(), securityInfo.getPassword());
					if(interviewer==null){
						return new ResponseEntity<>(HttpStatus.NO_CONTENT);
					}else{
						Long interviewerId=interviewer.getId();
						List<Candidates>	candidatesList=	candRepository.findByInterviewerId(interviewerId);
						candidatesList=candidatesList.stream().filter((c)->!(c.getCandiateStatus().equalsIgnoreCase("Rejected"))).collect(Collectors.toList());
						return new ResponseEntity<>(candidatesList,HttpStatus.OK);
					}
				}
				return new ResponseEntity<>(ta_user, HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
}
