package com.example.model.dao;

import com.infosys.inventory.dao.ProductDao;
import com.infosys.inventory.model.Product;
import com.infosys.inventory.util.DbConnection;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductDaoTest {

    @Mock
    Connection mockConnection;
    @Mock
    PreparedStatement mockPreparedStatement;
    @Mock
    Statement mockStatement;
    @Mock
    ResultSet mockResultSet;

    ProductDao productDao;

    MockedStatic<DbConnection> mockedDbConnection;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        mockedDbConnection = mockStatic(DbConnection.class);
        mockedDbConnection.when(DbConnection::getConnect).thenReturn(mockConnection);

        productDao = new ProductDao();
    }

    @AfterEach
    void tearDown() {
        mockedDbConnection.close();
    }

    @Test
    void testAddProduct_Positive() throws Exception {
        Product p = new Product(101, "Mobile", 5, 5000.0, "Electronics", 50);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        productDao.addProduct(p);

        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testAddProduct_Negative_Duplicate() throws Exception {
        Product p = new Product(101, "Mobile", 5, 5000.0, "Electronics", 50);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Duplicate entry"));

        productDao.addProduct(p);

        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testGetProductById_Positive() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(101);
        when(mockResultSet.getString(2)).thenReturn("Mobile");
        when(mockResultSet.getInt(3)).thenReturn(5);
        when(mockResultSet.getDouble(4)).thenReturn(5000.0);
        when(mockResultSet.getString(5)).thenReturn("Electronics");
        when(mockResultSet.getInt(6)).thenReturn(50);

        Product result = productDao.getProductById(101);

        assertNotNull(result);
        assertEquals(101, result.getProductId());
        assertEquals(50, result.getThreshold());
    }

    @Test
    void testGetProductById_Negative_NotFound() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Product result = productDao.getProductById(999);

        assertNull(result);
    }

    @Test
    void testUpdateInventory_Positive() throws Exception {
        Product p = new Product(101, "MobileX", 10, 5500.0, "Electronics", 40);

        PreparedStatement mockUpdate = mock(PreparedStatement.class);
        PreparedStatement mockSelect = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConnection.prepareStatement(startsWith("update"))).thenReturn(mockUpdate);
        when(mockConnection.prepareStatement(startsWith("select"))).thenReturn(mockSelect);
        when(mockSelect.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt(1)).thenReturn(101);
        when(mockRs.getString(2)).thenReturn("Mobile");
        when(mockRs.getInt(3)).thenReturn(5);
        when(mockRs.getDouble(4)).thenReturn(5000.0);
        when(mockRs.getString(5)).thenReturn("Electronics");
        when(mockRs.getInt(6)).thenReturn(50);
        when(mockUpdate.executeUpdate()).thenReturn(1);

        productDao.updateProduct(p);

        verify(mockUpdate, times(1)).executeUpdate();
    }

    @Test
    void testUpdateInventory_Negative_NotFound() throws Exception {
        Product p = new Product(999, "TubeLights", 0, 0, "Electronics", 10);

        PreparedStatement mockUpdate = mock(PreparedStatement.class);
        PreparedStatement mockSelect = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConnection.prepareStatement(startsWith("update"))).thenReturn(mockUpdate);
        when(mockConnection.prepareStatement(startsWith("select"))).thenReturn(mockSelect);
        when(mockSelect.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        productDao.updateProduct(p);

        verify(mockUpdate, never()).executeUpdate();
    }

    @Test
    void testDeleteProduct_Positive() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        productDao.deleteProduct(101);

        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testDeleteProduct_Negative_NotFound() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        productDao.deleteProduct(999);

        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testGetAllProducts_Positive() throws Exception {
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt(1)).thenReturn(101);
        when(mockResultSet.getString(2)).thenReturn("Mobile");
        when(mockResultSet.getInt(3)).thenReturn(5);
        when(mockResultSet.getDouble(4)).thenReturn(5000.0);
        when(mockResultSet.getString(5)).thenReturn("Electronics");
        when(mockResultSet.getInt(6)).thenReturn(50);

        ArrayList<Product> products = productDao.getAllProducts();

        assertEquals(1, products.size());
        assertEquals(50, products.get(0).getThreshold());
    }

    @Test
    void testGetAllProducts_Negative_Empty() throws Exception {
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        ArrayList<Product> products = productDao.getAllProducts();

        assertTrue(products.isEmpty());
    }

    @Test
    void testFilterRange_Positive() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("product_id")).thenReturn(101);
        when(mockResultSet.getString("product_name")).thenReturn("Mobile");
        when(mockResultSet.getInt("product_quantity")).thenReturn(5);
        when(mockResultSet.getDouble("product_price")).thenReturn(5000.0);
        when(mockResultSet.getString("product_category")).thenReturn("Electronics");
        when(mockResultSet.getInt("threshold")).thenReturn(50);

        productDao.FilterRange(1000, 6000);

        verify(mockPreparedStatement, times(1)).executeQuery();
    }

    @Test
    void testFilterRange_Negative_NoProducts() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        productDao.FilterRange(1000, 6000);

        verify(mockPreparedStatement, times(1)).executeQuery();
    }
}
