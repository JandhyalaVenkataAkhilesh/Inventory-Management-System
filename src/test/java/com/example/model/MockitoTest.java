package com.example.model;

import com.infosys.inventory.dao.ProductDao;
import com.infosys.inventory.model.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class MockitoTest {

    private static ProductDao dao;

    @BeforeAll
    public static void setup() {
        dao = Mockito.mock(ProductDao.class);
    }

    @Test
    public void testAddProduct() throws SQLException {
        Product p = new Product(107, "Switch", 1, 257.58, "Electronics");

        // Stubbing and mocking
        doNothing().when(dao).addProduct(p);
        when(dao.getProductById(107)).thenReturn(p);

        dao.addProduct(p);
        Product product = dao.getProductById(107);

        verify(dao).addProduct(p);
        assertEquals(107, product.getProductId());
        assertEquals("Switch", product.getProductName());
        assertEquals(1, product.getQuantity());
        assertEquals(257.58, product.getPrice(), 0.001);
        assertEquals("Electronics", product.getCategory());
    }

    @Test
    public void testGetProductById() throws SQLException {
        Product product = new Product(101, "Bulb", 1, 100.75, "Electric");
        when(dao.getProductById(101)).thenReturn(product);

        Product result = dao.getProductById(101);

        verify(dao).getProductById(101);
        assertNotNull(result);
        assertEquals("Bulb", result.getProductName());
        assertEquals(1, result.getQuantity());
        assertEquals(100.75, result.getPrice(), 0.001);
    }

    @Test
    public void testGetAllProducts() throws SQLException {
        ArrayList<Product> mockProducts = new ArrayList<>(Arrays.asList(
                new Product(101, "Bulb", 1, 100.75, "Electric"),
                new Product(102, "Notebook", 3, 200.00, "Stationery"),
                new Product(103, "Pen", 5, 25.50, "Stationery"),
                new Product(104, "Bottle", 2, 150.00, "Utility"),
                new Product(105, "Chair", 1, 1200.00, "Furniture"),
                new Product(106, "Table", 1, 2200.00, "Furniture"),
                new Product(107, "Switch", 1, 257.58, "Electronics")
        ));

        Mockito.when(dao.getAllProducts()).thenReturn(mockProducts);

        ArrayList<Product> products = dao.getAllProducts();

        Mockito.verify(dao).getAllProducts();
        assertNotNull(products);
        assertEquals(7, products.size());
        assertEquals("Bulb", products.get(0).getProductName());
        assertEquals("Switch", products.get(6).getProductName());
    }

    @Test
    public void testUpdateProduct() throws SQLException {
        Product product = new Product(102, "Notebook", 3, 200.00, "Stationery");
        product.setQuantity(250);
        product.setPrice(2200.00);

        doNothing().when(dao).updateProduct(product);
        when(dao.getProductById(102)).thenReturn(product);

        dao.updateProduct(product);
        Product updated = dao.getProductById(102);

        verify(dao).updateProduct(product);
        assertEquals(250, updated.getQuantity());
        assertEquals(2200.00, updated.getPrice(), 0.001);
    }

    @Test
    public void testDeleteProduct() throws SQLException {
        int productId = 105;

        doNothing().when(dao).deleteProduct(productId);
        when(dao.getProductById(productId)).thenThrow(new RuntimeException("Inventory is empty"));

        dao.deleteProduct(productId);

        verify(dao).deleteProduct(productId);
        Exception exception = assertThrows(RuntimeException.class, () -> dao.getProductById(productId));
        assertTrue(exception.getMessage().contains("Inventory is empty"));
    }
}
