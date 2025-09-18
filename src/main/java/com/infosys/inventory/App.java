package com.infosys.inventory;

import com.infosys.inventory.model.Product;
import com.infosys.inventory.service.InventoryService;
import com.infosys.inventory.exceptions.InvalidProductException;
import com.infosys.inventory.exceptions.ProductNotFoundException;
import com.infosys.inventory.util.GenerateCSV;

import java.util.ArrayList;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InventoryService service = new InventoryService();

        while (true) {
            System.out.println("====Menu====");
            System.out.println("1. Add Product\n2. Search Product By Id\n3. Retrieve All Products\n4. Update Product\n5. Delete Product\n6. CSV Report\n7. Exit");
            System.out.print("Enter your choice : ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter Product Id : ");
                        int productId = scanner.nextInt();
                        scanner.nextLine();

                        System.out.print("Enter Product Name : ");
                        String productName = scanner.nextLine();

                        System.out.print("Enter Quantity : ");
                        int quantity = scanner.nextInt();
                        scanner.nextLine();

                        System.out.print("Enter Price : ");
                        double price = scanner.nextDouble();
                        scanner.nextLine();

                        System.out.print("Enter Category : ");
                        String category = scanner.nextLine();
                        Product p = new Product(productId, productName, quantity, price, category);
                        service.addProduct(p);
                        break;

                    case 2:
                        System.out.print("Enter Product Id : ");
                        int searchId = scanner.nextInt();
                        scanner.nextLine();
                        service.getProductById(searchId);
                        break;

                    case 3:
                        ArrayList<Product> materials=service.getAllProducts();
                        for (Product all:materials){
                            System.out.println(all);
                        }
                        break;

                    case 4:
                        System.out.print("Enter Product Id to Update : ");
                        int updateId = scanner.nextInt();
                        scanner.nextLine();

                        System.out.print("Enter Product Name (leave empty if no change) : ");
                        String updateName = scanner.nextLine();

                        System.out.print("Enter Quantity (0 if no change) : ");
                        int updateQuantity = scanner.nextInt();
                        scanner.nextLine();

                        System.out.print("Enter Price (0 if no change) : ");
                        double updatePrice = scanner.nextDouble();
                        scanner.nextLine();

                        System.out.print("Enter Product Category (leave empty if no change) : ");
                        String updateCategory = scanner.nextLine();

                        Product updateProduct = new Product(updateId,updateName,updateQuantity,updatePrice,updateCategory);
                        service.updateInventory(updateProduct);

                        break;

                    case 5:
                        System.out.print("Enter Product Id to Delete : ");
                        int deleteId = scanner.nextInt();
                        scanner.nextLine();
                        service.deleteInventory(deleteId);
                        break;

                    case 6:
                        System.out.print("Enter the location to create file : ");
                        String filePath = scanner.nextLine();
                        GenerateCSV csv = new GenerateCSV();
                        csv.reportCSV(filePath);
                        break;

                    case 7:
                        System.out.println("Exiting... Goodbye!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            } catch (InvalidProductException | ProductNotFoundException e) {
                System.out.println("Business Error : " + e.getMessage());
                throw e;
            } catch (Exception e) {
                System.out.println("Unexpected Error : " + e.getMessage());
                throw e;
            }

            System.out.println();
        }
    }
}
