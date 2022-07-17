package com.cerner.talentacquisition.controller;

import com.cerner.talentacquisition.model.Candidates;
import com.cerner.talentacquisition.model.Interviewer;
import com.cerner.talentacquisition.model.TA_User;
import com.cerner.talentacquisition.repository.BusinessUnitRepository;
import com.cerner.talentacquisition.repository.CandidatesRepository;
import com.cerner.talentacquisition.repository.InterviewerRepository;
import com.cerner.talentacquisition.repository.TAUserRepository;
import com.cerner.talentacquisition.service.DocStorageService;
import com.cerner.talentacquisition.utils.SecurityInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.Assert;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(TalentacquisitionController.class)
@TestPropertySource(locations="classpath:application.properties")
public class TalentacquisitionControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Value("${yourapp.http.auth-token-header-name}")
    private String headername;
    @Value("${yourapp.http.auth-token}")
    private  String headervalue;

    @MockBean
    private InterviewerRepository interviewerRepository;
    @MockBean
    private CandidatesRepository candidatesRepository;

    @MockBean
    private TAUserRepository taUserRepository;
    @MockBean
    private BusinessUnitRepository buRepository;
    @MockBean
    DocStorageService docStorageService;
    @Autowired
    private ObjectMapper objectMapper;


    @InjectMocks
    private TalentacquisitionController talentacquisitionController;

    public TalentacquisitionControllerTests() {
    }

    private InputStream is;
    
    @Test
    public void check_Post_Interviewer_Positive() throws Exception {
        Interviewer interviewer=new Interviewer();
        interviewer.setInterviewerName("ABC");
        interviewer.setAvailability("9:00");
        interviewer.setBusinessUnit("RC");
        interviewer.setEmailId("abc@gmail.com");
        interviewer.setPassword("abdsjbaj");
        interviewer.setPhoneNumber("8914289");
        interviewer.setType("L1");
        interviewer.setId(1L);
        BDDMockito.given(interviewerRepository.save(ArgumentMatchers.any(Interviewer.class)))
                .willAnswer((invocation)->invocation.getArgument(0));

        mockMvc.perform(post("/api/register")
                .header(headername,headervalue)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(interviewer))
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",aMapWithSize(8)))
                .andExpect(jsonPath("$.businessUnit", CoreMatchers.is(interviewer.getBusinessUnit())));

    }
    @Test
    public void check_Post_Interviewer_Negative_Exception() throws Exception {
        Interviewer interviewer=new Interviewer();
        interviewer.setInterviewerName("ABC");
        interviewer.setAvailability("9:00");
        interviewer.setBusinessUnit("RC");
        interviewer.setEmailId("abc@gmail.com");
        interviewer.setPassword("abdsjbaj");
        interviewer.setPhoneNumber("8914289");
        interviewer.setType("L1");
        interviewer.setId(1L);
        BDDMockito.given(interviewerRepository.save(ArgumentMatchers.any(Interviewer.class)))
                .willThrow(new RuntimeException());

        mockMvc.perform(post("/api/register")
                        .header(headername,headervalue)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(interviewer))
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isExpectationFailed());

    }

    @Test
    public void check_Post_Candidate_Positive() throws Exception {
        Candidates candidate=new Candidates();
        candidate.setSkills("Java");
        candidate.setCandidateName("Can-1");
        candidate.setPosition("SE1");
        candidate.setExperience("2");
        candidate.setInterviewerId(1L);
        candidate.setBusinessUnit("RC");

        BDDMockito.given(candidatesRepository.save(ArgumentMatchers.any(Candidates.class)))
               .willAnswer((invocation)->invocation.getArgument(0));

        ResultActions response=mockMvc.perform(post("/api/candidates")
                .contentType("application/json")
                .header(headername,headervalue)
                .content(objectMapper.writeValueAsString(candidate)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",aMapWithSize(10)))
                .andExpect(jsonPath("$.businessUnit", CoreMatchers.is(candidate.getBusinessUnit())));

    }

    @Test
    public void check_Post_Candidate_Negative_Exception() throws Exception {
        Candidates candidate=new Candidates();
        candidate.setSkills("Java");
        candidate.setCandidateName("Can-1");
        candidate.setPosition("SE1");
        candidate.setExperience("2");
        candidate.setInterviewerId(1L);
        candidate.setBusinessUnit("RC");

        BDDMockito.given(candidatesRepository.save(ArgumentMatchers.any(Candidates.class)))
                .willThrow(new RuntimeException());

        ResultActions response=mockMvc.perform(post("/api/candidates")
                .contentType("application/json")
                .header(headername,headervalue)
                .content(objectMapper.writeValueAsString(candidate)));

        response.andDo(print())
                .andExpect(status().isExpectationFailed());
    }


    @Test
    public void check_Get_List_Candidate_Positive() throws Exception {
    Candidates candidate=new Candidates();
    candidate.setSkills("Java");
    candidate.setCandidateName("Can-1");
    candidate.setPosition("SE1");
    candidate.setExperience("2");
    candidate.setInterviewerId(1L);
    candidate.setBusinessUnit("RC");
    BDDMockito.when(buRepository.findByBusinessUnit(candidate.getBusinessUnit())).thenReturn(Arrays.asList(candidate));
    mockMvc.perform(get("/api/reports/{bu}",candidate.getBusinessUnit())
            .contentType("application/json")
            .header(headername,headervalue)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$",hasSize(1)))
            .andExpect(jsonPath("$[0].businessUnit",is(candidate.getBusinessUnit())));

}

    @Test
    public void check_Get_List_Candidate_Negative_No_Content() throws Exception {
        Candidates candidate=new Candidates();
        candidate.setSkills("Java");
        candidate.setCandidateName("Can-1");
        candidate.setPosition("SE1");
        candidate.setExperience("2");
        candidate.setInterviewerId(1L);
        candidate.setBusinessUnit("RC");
        BDDMockito.given(buRepository.findByBusinessUnit(candidate.getBusinessUnit())).willReturn(Arrays.asList());
        mockMvc.perform(get("/api/reports/{bu}",candidate.getBusinessUnit())
                        .contentType("application/json")
                        .header(headername,headervalue)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void check_Get_List_Candidate_Negative_Exception() throws Exception {
        Candidates candidate=new Candidates();
        candidate.setSkills("Java");
        candidate.setCandidateName("Can-1");
        candidate.setPosition("SE1");
        candidate.setExperience("2");
        candidate.setInterviewerId(1L);
        candidate.setBusinessUnit("RC");
        BDDMockito.given(buRepository.findByBusinessUnit(candidate.getBusinessUnit())).willThrow(new RuntimeException());
        mockMvc.perform(get("/api/reports/{bu}",candidate.getBusinessUnit())
                        .contentType("application/json")
                        .header(headername,headervalue)
                )
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }



    @Test
    public void check_Put_Candidate_Positive() throws Exception {

    Candidates savedCandidate=new Candidates();
    Long candidateId=1L;
    savedCandidate.setSkills("Java");
    savedCandidate.setCandidateName("Can-1");
    savedCandidate.setPosition("SE1");
    savedCandidate.setExperience("2");
    savedCandidate.setInterviewerId(1L);
    savedCandidate.setBusinessUnit("RC");
    savedCandidate.setId(candidateId);

    Candidates updatedCandidate=new Candidates();
    updatedCandidate.setSkills("Spring");
    updatedCandidate.setCandidateName("Can-2");
    updatedCandidate.setPosition("SE3");
    updatedCandidate.setExperience("5");
    updatedCandidate.setInterviewerId(1L);
    updatedCandidate.setBusinessUnit("RC");
    updatedCandidate.setId(candidateId);

    BDDMockito.given(candidatesRepository.save(updatedCandidate)).willReturn(updatedCandidate);
    BDDMockito.given(candidatesRepository.findById(savedCandidate.getId())).willReturn(Optional.of(savedCandidate));

    mockMvc.perform(put("/api/candidates/{id}",updatedCandidate.getId())
                    .contentType("application/json")
                    .header(headername,headervalue)
                    .content(objectMapper.writeValueAsString(updatedCandidate))
                    .accept("application/json")
            )
            .andDo(print())
            .andExpect(status().isOk());

}
    @Test
    public void check_Put_Candidate_Negative_Not_Found() throws Exception {

        Candidates savedCandidate=new Candidates();
        Long candidateId=2L;
        savedCandidate.setSkills("Java");
        savedCandidate.setCandidateName("Can-1");
        savedCandidate.setPosition("SE1");
        savedCandidate.setExperience("2");
        savedCandidate.setInterviewerId(1L);
        savedCandidate.setBusinessUnit("RC");
        savedCandidate.setId(candidateId);

        Candidates updatedCandidate=new Candidates();
        updatedCandidate.setSkills("Spring");
        updatedCandidate.setCandidateName("Can-2");
        updatedCandidate.setPosition("SE3");
        updatedCandidate.setExperience("5");
        updatedCandidate.setInterviewerId(1L);
        updatedCandidate.setBusinessUnit("RC");
        updatedCandidate.setId(1L);

        BDDMockito.given(candidatesRepository.findById(savedCandidate.getId())).willReturn(Optional.empty());

        mockMvc.perform(put("/api/candidates/{id}",updatedCandidate.getId())
                        .contentType("application/json")
                        .header(headername,headervalue)
                        .content(objectMapper.writeValueAsString(updatedCandidate))
                        .accept("application/json")
                )
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    public void check_Post_TAorInterviewer_PositiveTA() throws Exception {
        TA_User taUser=new TA_User();
        taUser.setPassword("tauser1");
        taUser.setEmail("tauser1@cerner.com");
        taUser.setName("tauser1");
        SecurityInfo securityInfo=new SecurityInfo();
        securityInfo.setEmail("tauser1@cerner.com");
        securityInfo.setPassword("tauser1");
        BDDMockito.given(taUserRepository.findByEmailAndPassword(securityInfo.getEmail(),securityInfo.getPassword())).willReturn(taUser);

        ResultActions response=mockMvc.perform(post("/api/check")
                .contentType("application/json")
                .header(headername,headervalue)
                .content(objectMapper.writeValueAsString(securityInfo)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",aMapWithSize(4)));

    }
    @Test
    public void check_Post_TAorInterviewer_Negative_Exception() throws Exception {
        TA_User taUser=new TA_User();
        taUser.setPassword("tauser1");
        taUser.setEmail("tauser1@cerner.com");
        taUser.setName("tauser1");
        SecurityInfo securityInfo=new SecurityInfo();
        securityInfo.setEmail("tauser1@cerner.com");
        securityInfo.setPassword("tauser1");
        BDDMockito.given(taUserRepository.findByEmailAndPassword(securityInfo.getEmail(),securityInfo.getPassword())).willThrow(new RuntimeException());

        ResultActions response=mockMvc.perform(post("/api/check")
                .contentType("application/json")
                .header(headername,headervalue)
                .content(objectMapper.writeValueAsString(securityInfo)));

        response.andDo(print())
                .andExpect(status().isInternalServerError());

    }
    
	@Test
	public void testUploadFile() throws Exception {
		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "excel.xlsx", "multipart/form-data", is);
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.multipart("/api/uploadresume").file(mockMultipartFile)
						.contentType(MediaType.MULTIPART_FORM_DATA).header(headername, headervalue))
				.andExpect(MockMvcResultMatchers.status().is(200)).andReturn();
		Assert.assertEquals(200, result.getResponse().getStatus());
		Assert.assertNotNull(result.getResponse().getContentAsString());
	}

	@Test
	public void testDownloadFile() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/downloadresume/1")
				.contentType(MediaType.MULTIPART_FORM_DATA).header(headername, headervalue))
				.andExpect(MockMvcResultMatchers.status().is(404)).andReturn();
		Assert.assertEquals(404, result.getResponse().getStatus());
	}
}
