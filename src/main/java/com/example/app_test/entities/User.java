package com.example.app_test.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity()
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int UserID;

    @Column(length = 50)
    @NotBlank
    private String name;

    @Column(length = 50)
    @NotBlank
    private String pass;

    @Column(length = 255)
    @Email
    @NotBlank
    private String mail;

    public User() {
    }

    public User(int userID, String name, String pass, String mail) {
        UserID = userID;
        this.name = name;
        this.pass = pass;
        this.mail = mail;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public String toString() {
        return "User{" +
                "UserID=" + UserID +
                ", name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }
}
