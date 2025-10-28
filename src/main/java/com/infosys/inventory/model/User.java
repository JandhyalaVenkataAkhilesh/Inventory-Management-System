package com.infosys.inventory.model;

import java.util.InputMismatchException;

public class User {
    private int id;
    private String userName;
    private String password;
    private String role;
    private String email;
    private boolean isVerified;

    public User(int id, String userName, String password, String role, String email) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.email = email;
        this.isVerified = false;
    }

    public User(int id, String userName, String password, String role, String email, boolean isVerified) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.email = email;
        this.isVerified = isVerified;
    }

    public User() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if(id<=0)
            throw new InputMismatchException("Id should be positive integer");
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        if(userName == null || userName.isEmpty())
            throw new InputMismatchException("User Name should not be Empty, It should be unique");
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if(password == null || password.isEmpty())
            throw new InputMismatchException("Password should not be Empty, It should be unique");
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if(role == null || role.isEmpty())
            throw new InputMismatchException("Role should not be Empty");
        this.role = role;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(email == null || email.isEmpty())
            throw new InputMismatchException("email should not be Empty");
        this.email = email;
    }

    public boolean isVerified(){
        return this.isVerified;
    }

}