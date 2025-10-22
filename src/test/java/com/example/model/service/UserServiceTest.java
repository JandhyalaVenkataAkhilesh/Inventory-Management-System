package com.example.model.service;

import com.infosys.inventory.dao.UserDao;
import com.infosys.inventory.model.User;
import com.infosys.inventory.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserDao mockDao;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Success() throws SQLException {
        User user = new User(1, "alice", "pass123", "Admin", "alice@example.com", true);
        when(mockDao.getUserByName("alice")).thenReturn(user);

        User result = userService.login("alice", "pass123");

        assertNotNull(result);
        assertEquals("alice", result.getUserName());
        assertEquals("Admin", result.getRole());
        verify(mockDao, times(1)).getUserByName("alice");
    }

    @Test
    void testLogin_InvalidPassword() throws SQLException {
        User user = new User(1, "alice", "pass123", "Admin", "alice@example.com", true);
        when(mockDao.getUserByName("alice")).thenReturn(user);

        User result = userService.login("alice", "wrongpass");

        assertNull(result);
        verify(mockDao, times(1)).getUserByName("alice");
    }

    @Test
    void testLogin_UnverifiedUser() throws SQLException {
        User user = new User(1, "alice", "pass123", "Admin", "alice@example.com", false);
        when(mockDao.getUserByName("alice")).thenReturn(user);

        User result = userService.login("alice", "pass123");

        assertNull(result);
        verify(mockDao, times(1)).getUserByName("alice");
    }

    @Test
    void testLogin_NonExistentUser() throws SQLException {
        when(mockDao.getUserByName("bob")).thenReturn(null);

        User result = userService.login("bob", "anyPassword");

        assertNull(result);
        verify(mockDao, times(1)).getUserByName("bob");
    }
}
