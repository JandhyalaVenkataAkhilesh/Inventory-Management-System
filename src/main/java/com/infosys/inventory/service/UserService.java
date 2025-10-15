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
        if(user == null){
            System.out.println("User not found");
            return user;
        }

        if(user.getPassword().trim().equals(password)){
            System.out.println("✅ Login Successful");
            System.out.println("Welcome " + userName);
            System.out.println("Your Role is " + user.getRole());
            return user;
        }else{
            System.out.println("Invalid User");
            return null;
        }
    }

}
