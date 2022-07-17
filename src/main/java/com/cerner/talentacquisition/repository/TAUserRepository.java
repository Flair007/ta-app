package com.cerner.talentacquisition.repository;

import com.cerner.talentacquisition.model.TA_User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TAUserRepository extends JpaRepository<TA_User, Long> {

        TA_User findByEmailAndPassword(String email,String password);
}
