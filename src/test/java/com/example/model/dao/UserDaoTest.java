package com.example.model.dao;

import com.infosys.inventory.dao.UserDao;
import com.infosys.inventory.exceptions.UserNotFound;
import com.infosys.inventory.model.User;
import com.infosys.inventory.util.DbConnection;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoTest {

    private UserDao userDao;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private MockedStatic<DbConnection> dbStatic;

    @BeforeAll
    void init() throws SQLException {
        userDao = new UserDao();
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        dbStatic = mockStatic(DbConnection.class);
        dbStatic.when(DbConnection::getConnect).thenReturn(mockConnection);
    }

    @AfterAll
    void tearDown() {
        dbStatic.close();
    }

    @BeforeEach
    void setUpMocks() throws SQLException {
        reset(mockConnection, mockPreparedStatement, mockResultSet);
    }

    @Test
    void testAddUser_Positive() throws SQLException {
        User user = new User(1, "John", "pass123", "Admin", "john@example.com", false);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> userDao.addUser(user));
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testGetUserByName_Positive() throws SQLException, UserNotFound {
        User user = new User(1, "John", "pass123", "Admin", "john@example.com", false);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(user.getId());
        when(mockResultSet.getString(2)).thenReturn(user.getUserName());
        when(mockResultSet.getString(3)).thenReturn(user.getPassword());
        when(mockResultSet.getString(4)).thenReturn(user.getRole());
        when(mockResultSet.getString(5)).thenReturn(user.getEmail());
        when(mockResultSet.getBoolean(6)).thenReturn(user.isVerified());

        User result = userDao.getUserByName("John");
        assertNotNull(result);
        assertEquals("John", result.getUserName());
        assertEquals("john@example.com", result.getEmail());
    }

    @Test
    void testGetUserByName_Negative_UserNotFound() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        assertThrows(UserNotFound.class, () -> userDao.getUserByName("Alice"));
    }

    @Test
    void testGetUserByEmail_True() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        boolean exists = userDao.getUserByEmail("john@example.com");
        assertTrue(exists);
    }

    @Test
    void testGetUserByEmail_False() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        boolean exists = userDao.getUserByEmail("alice@example.com");
        assertFalse(exists);
    }

    @Test
    void testIsVerified_True() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getBoolean("is_verified")).thenReturn(true);

        boolean verified = userDao.isVerified("john@example.com");
        assertTrue(verified);
    }

    @Test
    void testIsVerified_False() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        boolean verified = userDao.isVerified("alice@example.com");
        assertFalse(verified);
    }

    @Test
    void testMakeVerify_Positive() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = userDao.makeVerify("john@example.com");
        assertTrue(result);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testMakeVerify_Negative() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        boolean result = userDao.makeVerify("alice@example.com");
        assertFalse(result);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }
}
