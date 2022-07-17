package com.cerner.talentacquisition.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="feedback")
public class Feedback {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private  long id;
	private  long candidateId;
private String status;
private LocalDateTime time;
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public LocalDateTime getTime() {
	return time;
}
public void setTime(LocalDateTime time) {
	this.time = time;
}
public long getCandidateId() {
	return candidateId;
}
public void setCandidateId(long candidateId) {
	this.candidateId = candidateId;
}

}
