package com.example.model;

import com.infosys.inventory.dao.ProductDao;
import com.infosys.inventory.model.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDaoTest {
    private static ProductDao dao;

    @BeforeAll
    public static void setUp() {
        dao = new ProductDao();
    }

    @Test
    public void testAddProduct() throws SQLException {
        Product p = new Product(107,"Switch",1,257.58,"Electronics");
        dao.addProduct(p);

        Product product = dao.getProductById(107);
        assertEquals(107, product.getProductId());
        assertEquals("Switch", product.getProductName());
        assertEquals(1, product.getQuantity());
        assertEquals(257.58, product.getPrice(),0.001);
        assertEquals("Electronics", product.getCategory());
    }

    @Test
    public void testProductById() throws SQLException {
        Product product = dao.getProductById(101);
        assertNotNull(product);
        assertEquals(101, product.getProductId());
        assertEquals("Bulb", product.getProductName());
        assertEquals(1, product.getQuantity());
        assertEquals(100.75, product.getPrice(), 0.001);
        assertEquals("Electric", product.getCategory());
    }

    @Test
    public void testAllProducts() throws SQLException {
        List<Product> products = dao.getAllProducts();
        assertNotNull(products);
        assertEquals(7, products.size());

        assertEquals("Bulb", products.get(0).getProductName());
        assertEquals("Notebook", products.get(1).getProductName());
        assertEquals("Pen", products.get(2).getProductName());
        assertEquals("Bottle", products.get(3).getProductName());
        assertEquals("Chair", products.get(4).getProductName());
        assertEquals("Table", products.get(5).getProductName());
        assertEquals("Switch", products.get(6).getProductName());

    }

    @Test
    public void testUpdateProduct() throws SQLException {
        Product product = dao.getProductById(102);
        assertNotNull(product);

        product.setQuantity(250);
        product.setPrice(2200.00);
        dao.updateProduct(product);

        Product updated = dao.getProductById(102);
        assertEquals(250, updated.getQuantity());
        assertEquals(2200.00, updated.getPrice(), 0.001);
    }

    @Test
    public void testDeleteProduct() throws SQLException {
        Product beforeDelete = dao.getProductById(105);
        assertNotNull(beforeDelete);
        dao.deleteProduct(105);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            dao.getProductById(105);
        });
        assertTrue(exception.getMessage().contains("Inventory is empty"));
    }


}