package com.infosys.inventory.dao;

import com.infosys.inventory.model.User;

import java.sql.SQLException;

public interface UserDaoInterface {
    void addUser(User user) throws SQLException;
    void getUserById(int id) throws SQLException;
    void getAllUser() throws SQLException;
}
