package com.infosys.inventory.service;

import com.infosys.inventory.dao.ProductDao;
import com.infosys.inventory.exceptions.InvalidProductException;
import com.infosys.inventory.exceptions.ProductNotFoundException;
import com.infosys.inventory.model.Product;

import java.sql.SQLException;
import java.util.ArrayList;

public class InventoryService {

    private final ProductDao dao;

    public InventoryService() {
        dao = new ProductDao();
    }

    public void addProduct(Product p) {
        validateProduct(p);
        dao.addProduct(p);
    }

    public Product getProductById(int productId) throws ProductNotFoundException {
        if (productId < 0) {
            throw new InvalidProductException("Product ID cannot be negative");
        }
        Product p = dao.getProductById(productId);
        return p;
    }

    public ArrayList<Product> getAllProducts() throws ProductNotFoundException {
        ArrayList<Product> allMaterials = dao.getAllProducts();
        return allMaterials;
    }

    public void updateInventory(Product p) throws ProductNotFoundException {
        if (p.getProductId() < 0) {
            throw new InvalidProductException("Product ID cannot be negative");
        }
        dao.updateInventory(p);
    }

    public void deleteInventory(int productId) throws ProductNotFoundException {
        if (productId < 0) {
            throw new InvalidProductException("Product ID cannot be negative");
        }
        dao.deleteProduct(productId);
    }

    private void validateProduct(Product p) {
        if (p.getProductId() < 0) {
            throw new InvalidProductException("Product ID cannot be negative");
        }
        if (p.getProductName() == null || p.getProductName().isEmpty()) {
            throw new InvalidProductException("Product name cannot be empty");
        }
        if (p.getQuantity() < 0) {
            throw new InvalidProductException("Quantity must be greater than or equal to 0");
        }
        if (p.getPrice() < 0) {
            throw new InvalidProductException("Price must be greater than or equal to 0");
        }
        if (p.getCategory() == null || p.getCategory().isEmpty()){
            throw new InvalidProductException("Product category cannot empty");
        }
    }

    public void filterProducts(double minPrice, double maxPrice) throws SQLException {
        if(minPrice<0)
            throw new InvalidProductException("Price must be greater than or equal to 0");
        if(maxPrice<0)
            throw new InvalidProductException("Price must be greater than or equal to 0");
        dao.FilterRange(minPrice,maxPrice);
    }
}
