package com.infosys.inventory.service;

import com.infosys.inventory.dao.ProductDao;
import com.infosys.inventory.model.Product;

import java.util.ArrayList;

public class StockAlertService {
    public ProductDao dao = new ProductDao();
    public void checkAlertService(String email, String userName){
        try {
            ArrayList<Product> allProducts = dao.getAllProducts();
            int targetStockLevel = 50;
            for(Product p: allProducts){
                if(p.getQuantity()<p.getThreshold()){
                    int reorderQuantity = targetStockLevel = p.getQuantity();
                    System.out.println("\n⚠️ Low Stock Alert: " + p.getProductName());
                    System.out.println("Current Quantity: " + p.getQuantity());
                    System.out.println("Required Reorder Quantity: " + reorderQuantity);
                    String msg = "Product " + p.getProductName() + " us low in stock.\n"
                            + "Current Quantity: " + p.getQuantity()
                            + "\nRequired Reorder: " + reorderQuantity;
                    EmailService.sendAlert(email,p.getProductName(),msg, userName);
                }

            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}
