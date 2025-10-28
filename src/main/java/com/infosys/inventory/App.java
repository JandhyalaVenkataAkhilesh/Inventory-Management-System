package com.infosys.inventory;

import com.infosys.inventory.model.Product;
import com.infosys.inventory.service.InventoryService;
import com.infosys.inventory.exceptions.InvalidProductException;
import com.infosys.inventory.exceptions.ProductNotFoundException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        InventoryService service = new InventoryService();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== INVENTORY MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Product");
            System.out.println("2. Get Product By ID");
            System.out.println("3. View All Products");
            System.out.println("4. Update Product");
            System.out.println("5. Delete Product");
            System.out.println("6. Filter Products (by price range)");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = 0;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("❌ Invalid format! Please enter a number for your choice. Try again.");
                sc.nextLine();
                continue;
            }

            switch (choice) {

                case 1:
                    try {
                        System.out.print("Enter Product ID: ");
                        int id = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter Product Name: ");
                        String name = sc.nextLine();

                        System.out.print("Enter Quantity: ");
                        int qty = sc.nextInt();

                        System.out.print("Enter Price: ");
                        double price = sc.nextDouble();
                        sc.nextLine();

                        System.out.print("Enter Category: ");
                        String cat = sc.nextLine();

                        System.out.print("Enter Threshold: ");
                        String thresholdInput = sc.nextLine();
                        int threshold = thresholdInput.isEmpty() ? 10 : Integer.parseInt(thresholdInput);

                        Product p = new Product(id, name, qty, price, cat, threshold);
                        service.addProduct(p);

                    } catch (InputMismatchException e) {
                        System.out.println("❌ Invalid format! Please enter correct value types for each field. Try again.");
                        sc.nextLine();
                    } catch (InvalidProductException | ProductNotFoundException e) {
                        System.out.println("❌ Failed to insert record – " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("❌ Unexpected error – " + e.getMessage());
                    }
                    break;

                case 2:
                    try {
                        System.out.print("Enter Product ID to fetch: ");
                        int fetchId = sc.nextInt();
                        sc.nextLine();

                        Product fetched = service.getProductById(fetchId);
                        if (fetched != null) {
                            System.out.println("\nProduct Details:");
                            System.out.println("-------------------------------------------------------------------------------");
                            System.out.printf("%-10s %-20s %-10s %-10s %-15s %-15s\n",
                                    "ID", "Name", "Qty", "Price", "Category", "Threshold");
                            System.out.println("-------------------------------------------------------------------------------");
                            System.out.printf("%-10d %-20s %-10d %-10.2f %-15s %-15d\n",
                                    fetched.getProductId(), fetched.getProductName(),
                                    fetched.getQuantity(), fetched.getPrice(),
                                    fetched.getCategory(), fetched.getThreshold());
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("❌ Invalid format! Product ID should be a number. Try again.");
                        sc.nextLine();
                    } catch (InvalidProductException | ProductNotFoundException e) {
                        System.out.println("❌ Failed to fetch record – " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("❌ Unexpected error – " + e.getMessage());
                    }
                    break;

                case 3:
                    try {
                        ArrayList<Product> all = service.getAllProducts();
                        if (all.isEmpty()) {
                            System.out.println("No products available.");
                        } else {
                            System.out.println("\nAll Products:");
                            System.out.println("-------------------------------------------------------------------------------");
                            System.out.printf("%-10s %-20s %-10s %-10s %-15s %-15s\n",
                                    "ID", "Name", "Qty", "Price", "Category", "Threshold");
                            System.out.println("-------------------------------------------------------------------------------");
                            for (Product prod : all) {
                                System.out.printf("%-10d %-20s %-10d %-10.2f %-15s %-15d\n",
                                        prod.getProductId(), prod.getProductName(),
                                        prod.getQuantity(), prod.getPrice(),
                                        prod.getCategory(), prod.getThreshold());
                            }
                        }
                    } catch (ProductNotFoundException e) {
                        System.out.println("❌ Failed to fetch all records – " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("❌ Unexpected error – " + e.getMessage());
                    }
                    break;

                case 4:
                    try {
                        System.out.print("Enter Product Id to Update : ");
                        int updateId = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter Product Name (leave empty if no change) : ");
                        String updateName = sc.nextLine();

                        System.out.print("Enter Quantity (0 if no change) : ");
                        int updateQuantity = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter Price (0 if no change) : ");
                        double updatePrice = sc.nextDouble();
                        sc.nextLine();

                        System.out.print("Enter Product Category (leave empty if no change) : ");
                        String updateCategory = sc.nextLine();

                        System.out.print("Enter Product Threshold (0 if no change) : ");
                        int updateThreshold = sc.nextInt();
                        sc.nextLine();

                        Product updateProduct = new Product(updateId, updateName, updateQuantity, updatePrice, updateCategory, updateThreshold, true);
                        service.updateInventory(updateProduct);
                    } catch (InputMismatchException e) {
                        System.out.println("❌ Invalid format! Please enter correct value types for each field. Try again.");
                        sc.nextLine();
                    } catch (InvalidProductException | ProductNotFoundException e) {
                        System.out.println("❌ Failed to update record – " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("❌ Unexpected error – " + e.getMessage());
                    }
                    break;

                case 5:
                    try {
                        System.out.print("Enter Product ID to delete: ");
                        int deleteId = sc.nextInt();
                        sc.nextLine();

                        service.deleteInventory(deleteId);
                    } catch (InputMismatchException e) {
                        System.out.println("❌ Invalid format! Product ID should be a number. Try again.");
                        sc.nextLine();
                    } catch (InvalidProductException | ProductNotFoundException e) {
                        System.out.println("❌ Failed to delete record – " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("❌ Unexpected error – " + e.getMessage());
                    }
                    break;

                case 6:
                    try {
                        System.out.print("Enter minimum price: ");
                        double min = sc.nextDouble();

                        System.out.print("Enter maximum price: ");
                        double max = sc.nextDouble();
                        sc.nextLine();

                        service.filterProducts(min, max);
                        System.out.println("✅ Products filtered successfully!");
                    } catch (InputMismatchException e) {
                        System.out.println("❌ Invalid format! Price must be a number. Try again.");
                        sc.nextLine();
                    } catch (InvalidProductException | SQLException e) {
                        System.out.println("❌ Failed to filter records – " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("❌ Unexpected error – " + e.getMessage());
                    }
                    break;

                case 7:
                    System.out.println("Exiting application...");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }
}