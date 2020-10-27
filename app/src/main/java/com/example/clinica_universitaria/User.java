package com.example.clinica_universitaria;

import java.util.Date;

public class User {
    String username;
    String fullName;
    String cita;
    String id;
    Date sessionExpiryDate;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setCita(String cita) {
        this.cita = cita;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSessionExpiryDate(Date sessionExpiryDate) {
        this.sessionExpiryDate = sessionExpiryDate;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getCita() {
        return cita;
    }

    public String getId() {
        return id;
    }

    public Date getSessionExpiryDate() {
        return sessionExpiryDate;
    }
}
