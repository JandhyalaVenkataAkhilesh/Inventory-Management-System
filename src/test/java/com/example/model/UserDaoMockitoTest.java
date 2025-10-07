package com.example.model;

import com.infosys.inventory.dao.UserDao;
import com.infosys.inventory.exceptions.InvalidCredentials;
import com.infosys.inventory.exceptions.UserNotFound;
import com.infosys.inventory.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserDaoMockitoTest {

    private static UserDao dao;

    @BeforeAll
    public static void setup() {
        dao = Mockito.mock(UserDao.class); // Mock the UserDao
    }

    @Test
    public void testAddUser() throws SQLException {
        User u = new User(102, "Suresh", "password", "Faculty");
        doNothing().when(dao).addUser(u);
        doNothing().when(dao).getUserById(102);
        dao.addUser(u);
        dao.getUserById(102);
        verify(dao).addUser(u);
        verify(dao).getUserById(102);
    }

    @Test
    public void testValidateUser_Success() throws SQLException {

        doNothing().when(dao).validateUser(101, "Ramu", "password123");

        assertDoesNotThrow(() -> dao.validateUser(101, "Ramu", "password123"));
        verify(dao).validateUser(101, "Ramu", "password123");
    }

    @Test
    public void testValidateUser_InvalidCredentials() throws SQLException {

        doThrow(new InvalidCredentials("Invalid Credentials"))
                .when(dao).validateUser(101, "wrongUser", "wrongPass");

        Exception exception = assertThrows(InvalidCredentials.class, () ->
                dao.validateUser(101, "wrongUser", "wrongPass"));

        assertEquals("Invalid Credentials", exception.getMessage());
        verify(dao).validateUser(101, "wrongUser", "wrongPass");
    }

    @Test
    public void testValidateUser_UserNotFound() throws SQLException {

        doThrow(new UserNotFound("No user exists with id 999"))
                .when(dao).validateUser(999, "noUser", "noPass");

        Exception exception = assertThrows(UserNotFound.class, () ->
                dao.validateUser(999, "noUser", "noPass"));

        assertTrue(exception.getMessage().contains("No user exists with id"));
        verify(dao).validateUser(999, "noUser", "noPass");
    }

    @Test
    public void testGetUserById_UserNotFound() throws SQLException {

        doThrow(new UserNotFound("No user exists with id 999"))
                .when(dao).getUserById(999);

        Exception exception = assertThrows(UserNotFound.class, () ->
                dao.getUserById(999));

        assertTrue(exception.getMessage().contains("No user exists with id"));
        verify(dao).getUserById(999);
    }

    @Test
    public void testGetAllUser_PrintsData() throws SQLException {

        doNothing().when(dao).getAllUser();

        dao.getAllUser();

        verify(dao).getAllUser();
    }
}
