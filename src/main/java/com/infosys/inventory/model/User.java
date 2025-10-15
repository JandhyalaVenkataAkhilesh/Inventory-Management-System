package com.infosys.inventory.model;

import java.util.InputMismatchException;

public class User {
    private int id;
    private String userName;
    private String password;
    private String role;

    public User(int id, String userName, String password, String role) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.role = role;
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
        if(password == null || password.isEmpty())
            throw new InputMismatchException("Role should not be Empty");
        this.role = role;
    }
}
