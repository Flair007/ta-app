package com.cerner.talentacquisition.controller;

import com.cerner.talentacquisition.model.Candidates;
import com.cerner.talentacquisition.model.Interviewer;
import com.cerner.talentacquisition.repository.BusinessUnitRepository;
import com.cerner.talentacquisition.repository.CandidatesRepository;
import com.cerner.talentacquisition.repository.InterviewerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TalentacquisitionController.class)
public class TalentacquisitionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InterviewerRepository interviewerRepository;
    @MockBean
    private CandidatesRepository candidatesRepository;

    @MockBean
    private BusinessUnitRepository buRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenInterviewer_saveInterviewer_thenReturnInterviewer() throws Exception {
        Interviewer interviewer = new Interviewer();
        interviewer.setInterviewerName("Int-1");
        interviewer.setBusinessUnit("RC");
        interviewer.setAvailability("9:00");
        interviewer.setEmailId("int1@gmail.com");
        interviewer.setPassword("int1");
        interviewer.setType("L1-SE1");
        interviewerRepository.save(interviewer);
        BDDMockito.given(interviewerRepository.save(interviewer)).willReturn(interviewer);
        ResultActions response=mockMvc.perform(post("/api/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(interviewer)));

                response.andExpect(status().isOk());
    }
    @Test
    public void givenCandidate_saveCandidate_thenReturnCandidate() throws Exception {
        Candidates candidate=new Candidates();
        candidate.setSkills("Java");
        candidate.setCandidateName("Can-1");
        candidate.setPosition("SE1");
        candidate.setExperience("2");
        candidate.setInterviewerId(1L);
        candidate.setBusinessUnit("RC");
        candidatesRepository.save(candidate);
        BDDMockito.given(candidatesRepository.save(candidate)).willReturn(candidate);

        ResultActions response=mockMvc.perform(post("/api/candidates")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(candidate)));

        response.andExpect(status().isOk());

    }
}
