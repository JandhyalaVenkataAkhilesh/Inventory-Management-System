package com.example.model;

import com.infosys.inventory.dao.UserDao;
import com.infosys.inventory.exceptions.InvalidCredentials;
import com.infosys.inventory.exceptions.UserNotFound;
import com.infosys.inventory.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class UserDaoTest {

    private static UserDao dao;

    @BeforeAll
    public static void setUp() {
        dao = new UserDao();
    }

    @Test
    public void testAddUser() throws SQLException {
        User u = new User(102, "Suresh", "password", "Faculty");
        dao.addUser(u);
        assertDoesNotThrow(() -> dao.getUserById(102));
    }


    @Test
    public void testGetAllUser_PrintsData() throws SQLException {
        dao.getAllUser();
    }


    @Test
    public void testValidateUser_Success() throws SQLException {
        assertDoesNotThrow(() -> dao.validateUser(101, "Ramu", "password123"));
    }

    @Test
    public void testValidateUser_InvalidCredentials() {
        Exception exception = assertThrows(InvalidCredentials.class, () -> {
            dao.validateUser(101, "wrongUser", "wrongPass");
        });
        assertEquals("Invalid Credentials", exception.getMessage());
    }

    @Test
    public void testValidateUser_UserNotFound() {
        Exception exception = assertThrows(UserNotFound.class, () -> {
            dao.validateUser(999, "noUser", "noPass");
        });
        assertTrue(exception.getMessage().contains("No user exists with id"));
    }


    @Test
    public void testGetUserById_UserNotFound() {
        Exception exception = assertThrows(UserNotFound.class, () -> {
            dao.getUserById(999);
        });
        assertTrue(exception.getMessage().contains("No user exists with id"));
    }
}
