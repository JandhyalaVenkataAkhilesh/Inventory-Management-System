package com.infosys.inventory.dao;

import com.infosys.inventory.model.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDaoInterface {
    void addProduct(Product P) throws SQLException;
    List<Product> getAllProducts() throws SQLException;
    Product getProductById(int productId) throws SQLException;
    boolean updateProduct(int productId, int ProductQuantity) throws SQLException;
    boolean deleteProduct(int productId) throws SQLException;
}
