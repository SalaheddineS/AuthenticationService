package com.AuthenticationService.Authentication;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Date;

@Document(collection = "authentication")
public class Authentication {
    @Id
    private String id;
    private String email;
    private String password;
    private Date birthDate;
    private String address;
    private String token;

    public Authentication(String id, String email, String password, Date birthDate, String address, String token) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.address = address;
        this.token = token;
    }
    public Authentication(){

    }

    public Authentication(String email, String password, Date birthDate, String address, String token) {
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.address = address;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
