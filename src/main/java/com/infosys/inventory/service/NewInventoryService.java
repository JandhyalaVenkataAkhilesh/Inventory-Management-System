package com.infosys.inventory.service;

import com.infosys.inventory.dao.ProductImplement;
import com.infosys.inventory.exceptions.InvalidProductException;
import com.infosys.inventory.exceptions.ProductNotFoundException;
import com.infosys.inventory.model.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NewInventoryService {
    public ProductImplement dao;
    public NewInventoryService(){
        dao = new ProductImplement();
    }

    public void addProduct(Product p){
        try{
            validate(p);
            dao.addProduct(p);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Product> getAllProducts() throws ProductNotFoundException {
        List<Product> records = new ArrayList<>();
        try {
            records = dao.getAllProducts();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return records;
    }

    public Product getProductById(int productId) throws ProductNotFoundException{
        if(productId<0){
            throw new InvalidProductException("product id should be positive number");
        }
        try{
            return dao.getProductById(productId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ProductNotFoundException(
                    "Error fetching product with ID: " + productId
            );
        }
    }

    public boolean updateProduct(int productId, int productQuantity){
        if(productId<0){
            throw new InvalidProductException("Product Id should positive number");
        }
        if(productQuantity<0){
            throw new InvalidProductException("At-least one quantity should be there");
        }
        boolean result;
        try {
            result = dao.updateProduct(productId, productQuantity);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return result;
    }

    public boolean deleteProduct(int productId){
        if(productId<0){
            throw new InvalidProductException("Product Id should positive number");
        }
        boolean result;
        try{
            result=dao.deleteProduct(productId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return result;
    }

    public void validate(Product p){
        if(p.getProductId()<0){
            throw new InvalidProductException("Product Id should positive number");
        }
        if(p.getProductName() == null || p.getProductName().isEmpty()){
            throw new InvalidProductException("Product Name should be valid name");
        }
        if(p.getQuantity()<0){
            throw new InvalidProductException("At-least one quantity should be there");
        }
        if(p.getPrice()<0){
            throw new InvalidProductException("Price should be valid");
        }
        if(p.getCategory() == null || p.getCategory().isEmpty()){
            throw new InvalidProductException("Product Category should be valid");
        }
    }
}
