package com.employee.portal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sessions")
public class LoginSuccess {
    @Id
    @Column(name = "sessionId")
    private String sessionId;

    @Column(name = "username")
    private String username;


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "LoginSuccess{" +
                "sessionId=" + sessionId +
                ", username='" + username + '\'' +
                '}';
    }
}
