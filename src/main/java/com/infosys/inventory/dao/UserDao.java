package com.infosys.inventory.dao;

import com.infosys.inventory.exceptions.InvalidProductException;
import com.infosys.inventory.exceptions.UserNotFound;
import com.infosys.inventory.model.User;
import com.infosys.inventory.util.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDao implements UserDaoInterface {
    @Override
    public void addUser(User user) throws SQLException {
        Connection con = DbConnection.getConnect();
        PreparedStatement pstmt = con.prepareStatement("insert into users values(?,?,?,?,?,?);");
        pstmt.setInt(1,user.getId());
        pstmt.setString(2, user.getUserName());
        pstmt.setString(3, user.getPassword());
        pstmt.setString(4, user.getRole());
        pstmt.setString(5,user.getEmail());
        pstmt.setBoolean(6,user.isVerified());
        int count = pstmt.executeUpdate();
        if(count == 1)
            System.out.println(count + " record successfully inserted");
        else
            System.out.println("Failed to insert");
    }


    @Override
    public User getUserByName(String userName) throws SQLException {

        Connection con = DbConnection.getConnect();
        PreparedStatement pstmt = con.prepareStatement("select * from users where username=?;");
        pstmt.setString(1,userName);
        ResultSet rs = pstmt.executeQuery();
        if(!rs.next()){
            throw new UserNotFound("No user exists with name " + userName);
        }
        User user = new User(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getBoolean(6));
        return user;
    }
    @Override
    public void getAllUser() throws SQLException {
        Connection con = DbConnection.getConnect();
        PreparedStatement pstmt = con.prepareStatement("select * from users");
        ResultSet rs = pstmt.executeQuery();


        System.out.printf("%-5s %-15s %-15s %-10s%n", "ID", "UserName", "Password", "Role");
        System.out.println("-----------------------------------------------");

        if (!rs.next()) {
            System.out.println("No users found");
            return;
        } else {
            do {
                int id = rs.getInt(1);
                String userName = rs.getString(2);
                String password = rs.getString(3);
                String role = rs.getString(4);


                System.out.printf("%-5d %-15s %-15s %-10s%n", id, userName, password, role);
            } while (rs.next());
        }
    }

    @Override
    public boolean getUserByEmail(String email) throws SQLException {
        Connection con = DbConnection.getConnect();
        String query = "SELECT 1 FROM users WHERE email = ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, email);
        ResultSet rs = pstmt.executeQuery();

        return rs.next();
    }


    @Override
    public boolean isVerified(String email) throws SQLException {
        Connection con = DbConnection.getConnect();
        String query = "SELECT is_verified FROM users WHERE email = ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, email);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getBoolean("is_verified");
        }
        return false;
    }

    @Override
    public boolean makeVerify(String email) throws SQLException {
        Connection con = DbConnection.getConnect();
        String updateQuery = "UPDATE users SET is_verified = TRUE WHERE email = ? AND is_verified = FALSE";
        PreparedStatement pstmt = con.prepareStatement(updateQuery);
        pstmt.setString(1, email);
        int rowsUpdated = pstmt.executeUpdate();
        return rowsUpdated > 0;
    }
}
