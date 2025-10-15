package com.example.model.service;

import com.infosys.inventory.dao.UserDao;
import com.infosys.inventory.model.User;
import com.infosys.inventory.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserDao mockDao;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(mockDao);
    }

    @Test
    void testLoginSuccess() throws SQLException {
        User user = new User(1, "Ramesh", "1234", "ADMIN");
        when(mockDao.getUserByName("Ramesh")).thenReturn(user);

        User result = userService.login("Ramesh", "1234");

        assertNotNull(result);
        assertEquals("Ramesh", result.getUserName());
        assertEquals("ADMIN", result.getRole());
    }

    @Test
    void testLoginUserNotFound() throws SQLException {
        when(mockDao.getUserByName("nonexistent")).thenReturn(null);

        User result = userService.login("nonexistent", "1234");
        assertNull(result);
    }

    @Test
    void testLoginInvalidPassword() throws SQLException {
        User user = new User(2, "Ramesh", "abcd", "USER");
        when(mockDao.getUserByName("Ramesh")).thenReturn(user);

        User result = userService.login("Ramesh", "wrongPassword");
        assertNull(result);
    }
}
