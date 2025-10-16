package com.infosys.inventory;

import com.infosys.inventory.dao.ProductDao;
import com.infosys.inventory.dao.UserDao;
import com.infosys.inventory.model.Product;
import com.infosys.inventory.model.User;
import com.infosys.inventory.service.EmailService;
import com.infosys.inventory.service.UserService;
import com.infosys.inventory.util.GenerateCSV;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UserApp {
    public static ProductDao productDao = new ProductDao();
    public static UserDao userDao = new UserDao();
    public static UserService userService = new UserService();
    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("=====================================================");
            System.out.println("🧾  Welcome to the Inventory Management System");
            System.out.println("=====================================================");
            System.out.println("1️⃣  New User Registration");
            System.out.println("2️⃣  Existing User Login");
            System.out.println("3️⃣  Exit");
            System.out.print("\n👉 Enter your choice (1-3): ");

            int choice = 0;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("❌ Invalid Input! Please enter a number between 1 and 3.\n");
                sc.nextLine();
                continue;
            }

            switch (choice) {
                case 1 -> newUserRegistration();
                case 2 -> existingUserLogin();
                case 3 -> {
                    System.out.println("\n=====================================================");
                    System.out.println("🙏 Thank you for using the Inventory Management System!");
                    System.out.println("-----------------------------------------------------");
                    System.out.println("✨ Have a great day! Goodbye! 👋");
                    System.exit(0);
                }
                default -> System.out.println("❌ Invalid Choice. Please enter 1-3.");
            }
        }
    }

    public static void newUserRegistration() {
        try {
            System.out.println("\n🆕 --- New User Registration ---");
            System.out.print("🪪  Enter User ID          : ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.print("👤  Enter Username          : ");
            String userName = sc.nextLine();
            System.out.print("🔐  Enter Password          : ");
            String password = sc.nextLine();
            System.out.print("🎭  Enter Role (Admin/User) : ");
            String role = sc.nextLine();

            User user = new User(id, userName, password, role);
            userDao.addUser(user);
            System.out.println("\n✅ Registration successful! You can now log in.\n");


            System.out.println("\n✅ Login Successful");
            System.out.println("Welcome " + userName);
            System.out.println("Your Role is " + role);

            if (role.equalsIgnoreCase("ADMIN")) adminMenu();
            else userMenu();

        } catch (InputMismatchException e) {
            System.out.println("❌ Invalid Input! Please enter correct data types.");
            sc.nextLine();
        } catch (SQLException e) {
            System.out.println("⚠️  Database Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Unexpected Error: " +
                    (e.getMessage() != null ? e.getMessage() : "Please try again."));
        }
    }


    public static void existingUserLogin() {
        try {
            System.out.println("\n🔐 --- User Login ---");
            System.out.print("👤  Enter Username : ");
            String userName = sc.nextLine();
            System.out.print("🔑  Enter Password : ");
            String password = sc.nextLine();
            System.out.println("\n⏳ Authenticating... Please wait...\n");

            User loggedUser = userService.login(userName, password);
            if (loggedUser == null) {
                System.out.println("❌ Invalid credentials. Please try again.\n");
                return;
            }

            if (loggedUser.getRole().equalsIgnoreCase("ADMIN")) adminMenu();
            else userMenu();
        } catch (SQLException e) {
            System.out.println("⚠️  Database Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Unexpected Error: " +
                    (e.getMessage() != null ? e.getMessage() : "Please try again."));
        }
    }


    public static void adminMenu() {
        while (true) {
            System.out.println("\n=====================================================");
            System.out.println("🛠️  ADMIN INVENTORY DASHBOARD");
            System.out.println("=====================================================");
            System.out.println("1️⃣  Add New Product");
            System.out.println("2️⃣  Search Product by ID");
            System.out.println("3️⃣  View All Products");
            System.out.println("4️⃣  Update Product Details");
            System.out.println("5️⃣  Delete Product");
            System.out.println("6️⃣  Export Inventory Report (CSV)");
            System.out.println("7️⃣  Filter Products");
            System.out.println("8️⃣  Logout");
            System.out.print("\n👉 Select an option (1-8): ");

            int choice = 0;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("❌ Invalid Input! Please enter a number between 1 and 7.");
                sc.nextLine();
                continue;
            }

            switch (choice) {
                case 1 -> addProduct();
                case 2 -> searchProductById();
                case 3 -> viewAllProducts();
                case 4 -> updateProduct();
                case 5 -> deleteProduct();
                case 6 -> {
                    var products = productDao.getAllProducts();
                    String filePath = GenerateCSV.generateProductsReport(products,"ADMIN");
                    EmailService.sendReport("akhileshjandhyala2006@gmail.com",
                            "Daily Inventory Report",
                            "Attached is your latest Inventory Report",
                            filePath);
                }
                case 7 -> filterProduct();
                case 8 -> {
                    System.out.println("\n👋 Admin logged out successfully.\n");
                    return;
                }
                default -> System.out.println("❌ Invalid Choice. Please try again.");
            }
        }
    }



    public static void userMenu() {
        while (true) {
            System.out.println("\n=====================================================");
            System.out.println("📦  USER INVENTORY DASHBOARD");
            System.out.println("=====================================================");
            System.out.println("1️⃣  Search Product by ID");
            System.out.println("2️⃣  View All Products");
            System.out.println("3️⃣  Filter Products");
            System.out.println("4️⃣  logout");
            System.out.print("\n👉 Select an option (1-4): ");

            int choice = 0;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("❌ Invalid Input! Please enter a number between 1 and 3.");
                sc.nextLine();
                continue;
            }

            switch (choice) {
                case 1 -> searchProductById();
                case 2 -> viewAllProducts();
                case 3 -> filterProduct();
                case 4 -> {
                    System.out.println("\n👋 Logged out successfully.\n");
                    return;
                }
                default -> System.out.println("❌ Invalid Choice. Please try again.");
            }
        }
    }


    public static void addProduct() {
        try {
            System.out.println("\n📦 --- Add New Product ---");
            System.out.print("🔢  Enter Product ID      : ");
            int productId = sc.nextInt();
            sc.nextLine();
            System.out.print("🏷️  Enter Product Name    : ");
            String productName = sc.nextLine().trim();
            System.out.print("📦  Enter Quantity        : ");
            int quantity = sc.nextInt();
            sc.nextLine();
            System.out.print("💰  Enter Price           : ");
            double price = sc.nextDouble();
            sc.nextLine();
            System.out.print("🗂️  Enter Category        : ");
            String category = sc.nextLine().trim();

            Product p = new Product(productId, productName, quantity, price, category);
            productDao.addProduct(p);
            System.out.println("\n✅ Product added successfully!\n");
        } catch (InputMismatchException e) {
            System.out.println("❌ Invalid Input! Please check your data types.");
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("❌ Unexpected Error: " +
                    (e.getMessage() != null ? e.getMessage() : "Please try again."));
        }
    }

    public static void searchProductById() {
        try {
            System.out.println("\n🔍 --- Search Product by ID ---");
            System.out.print("Enter Product ID : ");
            int searchId = sc.nextInt();
            sc.nextLine();
            Product p = productDao.getProductById(searchId);

            if (p == null) {
                System.out.println("⚠️  No product found with ID " + searchId);
                return;
            }

            System.out.println("\n📋 Search Results:\n");
            System.out.printf("%-20s %-10s %-10s %-15s%n", "Product Name", "Quantity", "Price", "Category");
            System.out.println("---------------------------------------------------------------");
            System.out.printf("%-20s %-10d %-10.2f %-15s%n",
                    p.getProductName(), p.getQuantity(), p.getPrice(), p.getCategory());
        } catch (InputMismatchException e) {
            System.out.println("❌ Invalid Input! Please enter a valid number.");
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("❌ Unexpected Error: " +
                    (e.getMessage() != null ? e.getMessage() : "Please try again."));
        }
    }

    public static void viewAllProducts() {
        try {
            System.out.println("\n📦 --- Complete Product Inventory ---");
            ArrayList<Product> products = productDao.getAllProducts();

            if (products.isEmpty()) {
                System.out.println("⚠️  No products available in inventory.");
                return;
            }

            System.out.printf("%-5s %-20s %-10s %-10s %-15s%n", "ID", "Product Name", "Quantity", "Price", "Category");
            System.out.println("--------------------------------------------------------------------------");
            for (Product p : products) {
                System.out.printf("%-5d %-20s %-10d %-10.2f %-15s%n",
                        p.getProductId(), p.getProductName(), p.getQuantity(), p.getPrice(), p.getCategory());
            }
        } catch (Exception e) {
            System.out.println("❌ Unexpected Error: " +
                    (e.getMessage() != null ? e.getMessage() : "Please try again."));
        }
    }

    public static void updateProduct() {
        try {
            System.out.println("\n✏️ --- Update Product Details ---");
            System.out.print("🔢  Enter Product ID to update : ");
            int updateId = sc.nextInt();
            sc.nextLine();
            System.out.print("🏷️  Enter New Product Name (leave blank for no change) : ");
            String updateName = sc.nextLine().trim();
            System.out.print("📦  Enter New Quantity (0 for no change)                : ");
            int updateQuantity = sc.nextInt();
            sc.nextLine();
            System.out.print("💰  Enter New Price (0 for no change)                   : ");
            double updatePrice = sc.nextDouble();
            sc.nextLine();
            System.out.print("🗂️  Enter New Category (leave blank for no change)      : ");
            String updateCategory = sc.nextLine().trim();

            Product updateProduct = new Product(updateId, updateName, updateQuantity, updatePrice, updateCategory, true);
            productDao.updateInventory(updateProduct);
            System.out.println("\n✅ Product details updated successfully!\n");
        } catch (InputMismatchException e) {
            System.out.println("❌ Invalid Input! Please check the entered data types.");
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("❌ Unexpected Error: " +
                    (e.getMessage() != null ? e.getMessage() : "Please try again."));
        }
    }

    public static void deleteProduct() {
        try {
            System.out.println("\n🗑️ --- Delete Product ---");
            System.out.print("Enter Product ID to delete : ");
            int deleteId = sc.nextInt();
            sc.nextLine();
            productDao.deleteProduct(deleteId);
            System.out.println("\n⚠️  Product deleted successfully!\n");
        } catch (InputMismatchException e) {
            System.out.println("❌ Invalid Input! Please enter a valid number.");
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("❌ Unexpected Error: " +
                    (e.getMessage() != null ? e.getMessage() : "Please try again."));
        }
    }

    public static void generateCSV() {
        try {
            System.out.println("\n📁 --- Export Inventory Report ---");
            System.out.print("Enter file location to save CSV : ");
            String filePath = sc.nextLine().trim();
            GenerateCSV csv = new GenerateCSV();
            csv.reportCSV(filePath);
            System.out.println("\n✅ Report generated successfully at the given location.\n");
        } catch (Exception e) {
            System.out.println("❌ Unexpected Error: " +
                    (e.getMessage() != null ? e.getMessage() : "Please try again."));
        }
    }

    public static void filterProduct() {
        try {
            System.out.println("\n📊 --- Filter Products by Price Range ---");
            System.out.print("Enter Minimum Price : ");
            double minPrice = sc.nextDouble();
            sc.nextLine();
            System.out.print("Enter Maximum Price : ");
            double maxPrice = sc.nextDouble();
            sc.nextLine();

            System.out.println("\n🔎 Products in the price range " + minPrice + " - " + maxPrice + ":\n");

            productDao.FilterRange(minPrice, maxPrice);


        } catch (InputMismatchException e) {
            System.out.println("❌ Invalid input! Please enter numeric values for price.");
            sc.nextLine();
        } catch (SQLException e) {
            System.out.println("⚠️ Database Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Unexpected Error: " + (e.getMessage() != null ? e.getMessage() : "Please try again."));
        }
    }

}
