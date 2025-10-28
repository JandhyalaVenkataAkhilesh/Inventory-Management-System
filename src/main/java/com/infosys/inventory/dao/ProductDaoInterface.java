package com.infosys.inventory.dao;

import com.infosys.inventory.model.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ProductDaoInterface {
    void addProduct(Product P) ;
    ArrayList<Product> getAllProducts() ;
    Product getProductById(int productId) ;
    void updateProduct(Product p) ;
    void deleteProduct(int productId) ;
    void FilterRange(double minPrice, double maxPrice);
}