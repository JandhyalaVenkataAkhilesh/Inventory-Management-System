package com.example.model;

import com.infosys.inventory.dao.UserDao;
import com.infosys.inventory.model.User;
import com.infosys.inventory.util.DbConnection;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserDaoNewTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    private UserDao userDao;
    private MockedStatic<DbConnection> mockedDBConnection;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        mockedDBConnection = mockStatic(DbConnection.class);
        mockedDBConnection.when(DbConnection::getConnect).thenReturn(mockConnection);
        userDao = new UserDao();
    }

    @AfterEach
    void tearDown() {
        if (mockedDBConnection != null) {
            mockedDBConnection.close();
        }
    }

    // ✅ Test addUser() - success
    @Test
    void testAddUserSuccess() throws Exception {
        User user = new User(1282, "Ram", "pass456", "ADMIN");

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        userDao.addUser(user);

        verify(mockPreparedStatement).setInt(1, 1282);
        verify(mockPreparedStatement).setString(2, "Ram");
        verify(mockPreparedStatement).setString(3, "pass456");
        verify(mockPreparedStatement).setString(4, "ADMIN");
        verify(mockPreparedStatement).executeUpdate();
    }

    // ✅ Test addUser() - duplicate username
    @Test
    void testAddUserDuplicateUsername() throws Exception {
        User user = new User(2003, "existingUserRam", "abcd123", "USER");

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        doThrow(new SQLIntegrityConstraintViolationException("Duplicate username"))
                .when(mockPreparedStatement).executeUpdate();

        assertDoesNotThrow(() -> userDao.addUser(user));
        verify(mockPreparedStatement).executeUpdate();
    }

    // ✅ Test getUserByUsername() - found
    @Test
    void testGetUserByUsernameSuccess() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1282);
        when(mockResultSet.getString("username")).thenReturn("Ram");
        when(mockResultSet.getString("password")).thenReturn("pass456");
        when(mockResultSet.getString("role")).thenReturn("ADMIN");

        User result = userDao.getUserByName("Ram");

        assertNotNull(result);
        assertEquals("Ram", result.getUserName());
        assertEquals("ADMIN", result.getRole());
    }

    // ✅ Test getUserByUsername() - not found
    @Test
    void testGetUserByUsernameNotFound() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(false);

        User result = userDao.getUserByName("unknownUser");
        assertNull(result);
    }
}
