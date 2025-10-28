package com.infosys.inventory.service;

import com.infosys.inventory.dao.UserDao;
import com.infosys.inventory.model.User;

import java.sql.SQLException;

public class UserService {
    private UserDao dao;
    public UserService(){
        dao = new UserDao();
    }

    public UserService(UserDao dao) {
        this.dao = dao;
    }


    public User login(String userName, String password) throws SQLException {

        User user = dao.getUserByName(userName);

        if (user == null || !user.getPassword().equals(password)) {
//            System.out.println("❌ Invalid username or password.");
            return null;
        }

        if (!user.isVerified()) {
//            System.out.println("⚠️ Please verify your email before login.");
            return null;
        }

        System.out.println("✅ Login Successful");
        System.out.println("Welcome " + user.getUserName());
        System.out.println("Your Role is " + user.getRole());
        return user;
    }

}