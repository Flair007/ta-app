package com.cerner.talentacquisition.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cerner.talentacquisition.model.Doc;

public interface DocRepository  extends JpaRepository<Doc,Integer>{

}
