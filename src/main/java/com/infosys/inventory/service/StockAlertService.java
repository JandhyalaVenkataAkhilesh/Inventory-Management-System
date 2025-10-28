package com.infosys.inventory.service;

import com.infosys.inventory.dao.ProductDao;
import com.infosys.inventory.model.Product;
import java.util.ArrayList;

public class StockAlertService {
    public ProductDao dao = new ProductDao();

    public void checkAlertService(String email, String userName) {
        try {
            ArrayList<Product> allProducts = dao.getAllProducts();
            int targetStockLevel = 50;

            ArrayList<Product> lowStockProducts = new ArrayList<>();

            for (Product p : allProducts) {
                if (p.getQuantity() < p.getThreshold()) {
                    lowStockProducts.add(p);
                }
            }

            if (!lowStockProducts.isEmpty()) {
                StringBuilder msg = new StringBuilder();
                msg.append("⚠️ Low Stock Alert for the following products:\n\n");

                for (Product p : lowStockProducts) {
                    int reorderQuantity = targetStockLevel - p.getQuantity();
                    msg.append("Product: ").append(p.getProductName()).append("\n")
                            .append("Current Quantity: ").append(p.getQuantity()).append("\n")
                            .append("Required Reorder Quantity: ").append(reorderQuantity).append("\n\n");
                }

                EmailService.sendAlert(email, "Low Stock Alert Summary", msg.toString(), userName);
                System.out.println("📧 Combined low-stock alert email sent successfully!");
            } else {
                System.out.println("✅ All products have sufficient stock.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}
