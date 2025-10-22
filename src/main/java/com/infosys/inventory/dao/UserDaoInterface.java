package com.infosys.inventory.dao;

import com.infosys.inventory.model.User;

import java.sql.SQLException;

public interface UserDaoInterface {
    void addUser(User user) throws SQLException;
    User getUserByName(String userName) throws SQLException;
    void getAllUser() throws SQLException;
    boolean getUserByEmail(String email) throws SQLException;
    boolean isVerified(String email) throws SQLException;
    boolean makeVerify(String email) throws SQLException;
}
