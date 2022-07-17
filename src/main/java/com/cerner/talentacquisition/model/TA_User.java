package com.cerner.talentacquisition.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="tauser")
public class TA_User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;
    private String name;
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String password;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "TA_User{" + "id=" + id +", name='" + name + '\'' +", email='" + email + '\'' + ", password='" + password + '\'' + '}';
    }
}
