package com.example.model.dao;

import com.infosys.inventory.dao.UserDao;
import com.infosys.inventory.exceptions.UserNotFound;
import com.infosys.inventory.model.User;
import com.infosys.inventory.util.DbConnection;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserDaoTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    private UserDao userDao;
    private MockedStatic<DbConnection> mockedDbConnection;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        mockedDbConnection = mockStatic(DbConnection.class);
        mockedDbConnection.when(DbConnection::getConnect).thenReturn(mockConnection);
        userDao = new UserDao();
    }

    @AfterEach
    void tearDown() {
        if (mockedDbConnection != null) {
            mockedDbConnection.close();
        }
    }


    @Test
    void testAddUserSuccess() throws Exception {
        User user = new User(1, "Ramesh", "1234", "ADMIN");

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        userDao.addUser(user);

        verify(mockPreparedStatement).setInt(1, 1);
        verify(mockPreparedStatement).setString(2, "Ramesh");
        verify(mockPreparedStatement).setString(3, "1234");
        verify(mockPreparedStatement).setString(4, "ADMIN");
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    void testAddUserDuplicate() throws Exception {
        User user = new User(101, "Ramu", "password123", "admin");

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        doThrow(new SQLIntegrityConstraintViolationException("Duplicate entry"))
                .when(mockPreparedStatement).executeUpdate();

        assertThrows(SQLIntegrityConstraintViolationException.class, () -> userDao.addUser(user));
        verify(mockPreparedStatement).executeUpdate();
    }



    @Test
    void testGetUserByNameSuccess() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);
        when(mockResultSet.getString(2)).thenReturn("Ramesh");
        when(mockResultSet.getString(3)).thenReturn("1234");
        when(mockResultSet.getString(4)).thenReturn("ADMIN");

        User user = userDao.getUserByName("Ramesh");

        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("Ramesh", user.getUserName());
        assertEquals("1234", user.getPassword());
        assertEquals("ADMIN", user.getRole());
    }

    @Test
    void testGetUserByNameNotFound() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        assertThrows(UserNotFound.class, () -> userDao.getUserByName("nonexistent"));
    }



    @Test
    void testGetAllUserWithData() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt(1)).thenReturn(1, 2);
        when(mockResultSet.getString(2)).thenReturn("Ramesh", "Suresh");
        when(mockResultSet.getString(3)).thenReturn("1234", "abcd");
        when(mockResultSet.getString(4)).thenReturn("ADMIN", "USER");

        assertDoesNotThrow(() -> userDao.getAllUser());

        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet, times(3)).next();
    }

    @Test
    void testGetAllUserNoData() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        assertDoesNotThrow(() -> userDao.getAllUser());
        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet).next();
    }
}
