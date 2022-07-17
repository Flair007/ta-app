package com.cerner.talentacquisition.model;

import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "candidates")
public class Candidates implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private long id;
	private String candidateName;
	private String position;
	private String businessUnit;
	private String experience;
	private String skills;
	private Blob resume;
	private long interviewerId;
	private String candiateStatus;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCandidateName() {
		return candidateName;
	}

	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public Blob getResume() {
		return resume;
	}

	public void setResume(Blob resume) {
		this.resume = resume;
	}

	public long getInterviewerId() {
		return interviewerId;
	}

	public String getCandiateStatus() {
		return candiateStatus;
	}

	public void setCandiateStatus(String candiateStatus) {
		this.candiateStatus = candiateStatus;
	}

	public void setInterviewerId(long interviewerId) {
		this.interviewerId = interviewerId;
	}

	@Override
	public String toString() {
		return "Candidates [id=" + id + ", candidateName=" + candidateName + ", position=" + position
				+ ", businessUnit=" + businessUnit + ", experience=" + experience + ", skills=" + skills + ", resume="
				+ resume + ", interviewerId=" + interviewerId + ", candiateStatus=" + candiateStatus + "]";
	}

}
