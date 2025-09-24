package com.infosys.inventory;
import com.infosys.inventory.model.Product;
import com.infosys.inventory.service.NewInventoryService;
import com.infosys.inventory.exceptions.InvalidProductException;
import com.infosys.inventory.exceptions.ProductNotFoundException;

import java.util.List;
import java.util.Scanner;
public class NewApp {
    public static void main(String[] args) {
        NewInventoryService service = new NewInventoryService();
        Scanner sc = new Scanner(System.in);

        try {
            // Add a product
            Product p1 = new Product();
            p1.setProductId(108);
            p1.setProductName("Laptop");
            p1.setQuantity(5);
            p1.setPrice(75000);
            p1.setCategory("Electronics");
            service.addProduct(p1);

            //Get all products
            List<Product> products = service.getAllProducts();
            System.out.println("All Products:");
            for (Product p : products) {
                System.out.println(p.getProductId() + " | " + p.getProductName() + " | " + p.getQuantity() + " | " + p.getPrice() + " | " + p.getCategory());
            }

            // Get product by ID
            Product fetched = service.getProductById(101);
            System.out.println("\nFetched Product: " + fetched.getProductName());

            // Update product quantity
            boolean updated = service.updateProduct(101, 10);
            System.out.println("Update Result: " + updated);

            // Delete product
            boolean deleted = service.deleteProduct(101);
            System.out.println("Delete Result: " + deleted);

        } catch (InvalidProductException | ProductNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }

        sc.close();
    }
}
