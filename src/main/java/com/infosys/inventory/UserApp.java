package com.infosys.inventory;

import com.infosys.inventory.dao.ProductDao;
import com.infosys.inventory.dao.UserDao;
import com.infosys.inventory.model.Product;
import com.infosys.inventory.model.User;
import com.infosys.inventory.service.EmailService;
import com.infosys.inventory.service.StockAlertService;
import com.infosys.inventory.service.UserService;
import com.infosys.inventory.util.GenerateCSV;
import com.infosys.inventory.util.OTPService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Integer.parseInt;

public class UserApp {

    // ANSI Color Codes
    public static final String RESET = "\u001B[0m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String PURPLE = "\u001B[35m";

    // Bold Colors
    public static final String WHITE_BOLD = "\u001B[1m\u001B[37m";
    public static final String GREEN_BOLD = "\u001B[1m\u001B[32m";
    public static final String RED_BOLD = "\u001B[1m\u001B[31m";
    public static final String CYAN_BOLD = "\u001B[1m\u001B[36m";
    public static final String YELLOW_BOLD = "\u001B[1m\u001B[33m";
    public static final String PURPLE_BOLD = "\u001B[1m\u001B[35m";

    // Background Colors
    public static final String BLUE_BACKGROUND = "\u001B[44m";
    public static final String CYAN_BACKGROUND = "\u001B[46m";

    // Menu Borders
    private static final String MENU_TOP_BORDER = CYAN_BOLD + "=========================================================" + RESET;
    private static final String MENU_SEPARATOR = CYAN_BOLD + "---------------------------------------------------------" + RESET;
    private static final String MENU_BOTTOM_BORDER = CYAN_BOLD + "=========================================================" + RESET;

    // Table formatting
    private static final String TABLE_HEADER_FORMAT = CYAN_BOLD + "%-5s | %-25s | %-12s | %-12s | %-20s | %-10s" + RESET + "%n";
    private static final String TABLE_ROW_FORMAT = "%-5d | %-25s | %-12d | %-12s | %-20s | %-10d%n";
    private static final String TABLE_SEPARATOR = CYAN + "------+---------------------------+--------------+--------------+----------------------+------------" + RESET;

    public static ProductDao productDao = new ProductDao();
    public static UserDao userDao = new UserDao();
    public static UserService userService = new UserService();
    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println(MENU_TOP_BORDER);
            System.out.println(CYAN_BACKGROUND + WHITE_BOLD + "          🧾  INVENTORY MANAGEMENT SYSTEM  " + RESET);
            System.out.println(MENU_SEPARATOR);
            System.out.println(GREEN_BOLD + "  1️⃣  " + RESET + "New User Registration");
            System.out.println(GREEN_BOLD + "  2️⃣  " + RESET + "Existing User Login");
            System.out.println(GREEN_BOLD + "  3️⃣  " + RESET + "Verify Email (OTP)");
            System.out.println(RED_BOLD + "  4️⃣  " + RESET + "Exit");
            System.out.println(MENU_BOTTOM_BORDER);
            System.out.print(YELLOW_BOLD + "\n  » Enter your choice (1-4): " + RESET);

            int choice = 0;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println(RED_BOLD + "\n  ❌ INVALID INPUT! Please enter a number (1-4)." + RESET);
                sc.nextLine();
                continue;
            }

            switch (choice) {
                case 1 -> newUserRegistration();
                case 2 -> existingUserLogin();
                case 3 -> verifyEmail();
                case 4 -> {
                    System.out.println(MENU_TOP_BORDER);
                    System.out.println(CYAN_BACKGROUND + WHITE_BOLD + "  🙏 Thank you for using the Inventory System! 👋  " + RESET);
                    System.out.println(MENU_BOTTOM_BORDER);
                    System.exit(0);
                }
                default -> System.out.println(RED_BOLD + "\n  ❌ Invalid Choice. Please enter 1-4." + RESET);
            }
        }
    }

    // -------------------- HELPER METHODS --------------------

    public static void runLoadingSpinner(int durationMs) {
        String[] spinner = {CYAN + "⠋" + RESET, CYAN + "⠙" + RESET, CYAN + "⠹" + RESET, CYAN + "⠸" + RESET, CYAN + "⠼" + RESET, CYAN + "⠴" + RESET, CYAN + "⠦" + RESET, CYAN + "⠧" + RESET, CYAN + "⠇" + RESET, CYAN + "⠏" + RESET};
        int delayMs = 100;
        int steps = durationMs / delayMs;
        String message = YELLOW + "  ⏳ Authenticating... " + RESET;
        for (int i = 0; i < steps; i++) {
            System.out.print("\r" + message + spinner[i % spinner.length]);
            try { Thread.sleep(delayMs); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        System.out.print("\r" + " ".repeat(message.length() + 2) + "\r");
        System.out.println();
    }

    public static void newUserRegistration() {
        try {
            System.out.println(PURPLE_BOLD + "\n  ========================================");
            System.out.println("  │ " + WHITE_BOLD + "  🆕  NEW USER REGISTRATION  " + PURPLE_BOLD + "  │");
            System.out.println("  ========================================" + RESET);
            System.out.print(CYAN + "  🪪  Enter User ID (Number) : " + RESET);
            int id = sc.nextInt(); sc.nextLine();
            System.out.print(CYAN + "  👤  Enter Username         : " + RESET);
            String userName = sc.nextLine();
            System.out.print(CYAN + "  🔐  Enter Password         : " + RESET);
            String password = sc.nextLine();
            System.out.print(CYAN + "  🎭  Enter Role (ADMIN/USER): " + RESET);
            String role = sc.nextLine().toUpperCase();
            System.out.print(CYAN + "  📧  Enter Email            : " + RESET);
            String email = sc.nextLine();

            User user = new User(id, userName, password, role, email);
            userDao.addUser(user);
            System.out.println(GREEN_BOLD + "\n  ✅ REGISTRATION SUCCESSFUL! Please verify your email before login.\n" + RESET);
        } catch (InputMismatchException e) {
            System.out.println(RED_BOLD + "\n  ❌ Invalid Input! User ID must be a number." + RESET);
            sc.nextLine();
        } catch (SQLException e) {
            System.out.println(YELLOW_BOLD + "\n  ⚠️ DATABASE ERROR: User ID or Email may already exist. " + e.getMessage() + RESET);
        } catch (Exception e) {
            System.out.println(RED_BOLD + "\n  ❌ UNEXPECTED ERROR: " + (e.getMessage() != null ? e.getMessage() : "Please try again.") + RESET);
        }
    }

    public static void existingUserLogin() {
        try {
            System.out.println(PURPLE_BOLD + "\n  ========================================");
            System.out.println("  │ " + WHITE_BOLD + "          🔐  USER LOGIN      " + PURPLE_BOLD + "  │");
            System.out.println("  ========================================" + RESET);
            System.out.print(CYAN + "  👤  Enter Username : " + RESET);
            String userName = sc.nextLine();
            System.out.print(CYAN + "  🔑 Enter Password  : " + RESET);
            String password = sc.nextLine();
            System.out.print(CYAN + "  📧 Enter Email     : " + RESET);
            String email = sc.nextLine();

            runLoadingSpinner(1500);

            User loggedUser = userService.login(userName, password);
            if (loggedUser == null) {
                if (!userDao.isVerified(email)) System.out.println(YELLOW_BOLD + "  ⚠️ Please verify your email before login." + RESET);
                else System.out.println(RED_BOLD + "  ❌ INVALID CREDENTIALS. Please try again.\n" + RESET);
                return;
            }
            if (loggedUser.getRole().equalsIgnoreCase("ADMIN")) {
                new Thread(()->{
                    StockAlertService alertService = new StockAlertService();
                    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                    scheduler.scheduleAtFixedRate(() -> alertService.checkAlertService(email, userName), 0, 5, TimeUnit.MINUTES);
                }).start();
                adminMenu();
            } else{
                userMenu();
            }
        } catch (SQLException e) {
            System.out.println(YELLOW_BOLD + "\n  ⚠️ DATABASE ERROR: " + e.getMessage() + RESET);
        } catch (Exception e) {
            System.out.println(RED_BOLD + "\n  ❌ UNEXPECTED ERROR: " + (e.getMessage() != null ? e.getMessage() : "Please try again.") + RESET);
        }
    }

    public static void verifyEmail() {
        try {
            System.out.println(PURPLE_BOLD + "\n  ========================================");
            System.out.println("  │ " + WHITE_BOLD + "     📧  EMAIL VERIFICATION (OTP) " + PURPLE_BOLD + "  │");
            System.out.println("  ========================================" + RESET);
            System.out.print(CYAN + "  📧 Enter Email: " + RESET);
            String email = sc.nextLine();

            boolean exists = userDao.getUserByEmail(email);
            if (!exists) { System.out.println(RED_BOLD + "\n  ❌ No user exists with this email!\n" + RESET); return; }

            boolean isVerify = userDao.isVerified(email);
            if (isVerify) { System.out.println(CYAN_BOLD + "\n  ℹ EMAIL IS ALREADY VERIFIED. No need to verify again!" + RESET); return; }

            String otp = OTPService.generateOTP(email);
            EmailService.sendOTP(email, otp);
            System.out.println(GREEN_BOLD + "\n  ✅ OTP sent successfully! Check your inbox." + RESET);

            System.out.print(CYAN + "  🔢 Enter the OTP sent to your email: " + RESET);
            String enteredOTP = sc.nextLine();

            if (OTPService.validateOTP(email, enteredOTP)) {
                userDao.makeVerify(email);
                System.out.println(GREEN_BOLD + "\n  ✅ Email verified successfully!" + RESET);
            } else System.out.println(RED_BOLD + "\n  ❌ Invalid OTP! Email verification failed." + RESET);

        } catch (SQLException e) {
            System.out.println(YELLOW_BOLD + "\n  ⚠️ Database Error: " + e.getMessage() + RESET);
        } catch (Exception e) {
            System.out.println(RED_BOLD + "\n  ❌ Unexpected Error: " + (e.getMessage() != null ? e.getMessage() : "Please try again.") + RESET);
        }
    }

    // -------------------- ADMIN & USER MENUS --------------------

    public static void adminMenu() {
        while (true) {
            System.out.println(MENU_TOP_BORDER);
            System.out.println(BLUE_BACKGROUND + WHITE_BOLD + "       🛠️  ADMIN INVENTORY DASHBOARD  " + RESET);
            System.out.println(MENU_SEPARATOR);
            System.out.println(GREEN + "  1️⃣  " + RESET + "Add New Product");
            System.out.println(GREEN + "  2️⃣  " + RESET + "Search Product by ID");
            System.out.println(GREEN + "  3️⃣  " + RESET + "View All Products");
            System.out.println(GREEN + "  4️⃣  " + RESET + "Update Product Details");
            System.out.println(GREEN + "  5️⃣  " + RESET + "Delete Product");
            System.out.println(GREEN + "  6️⃣  " + RESET + "Export Inventory Report (CSV) & Email");
            System.out.println(GREEN + "  7️⃣  " + RESET + "Filter Products by Price Range");
            System.out.println(RED + "  8️⃣  " + RESET + "Logout");
            System.out.println(MENU_BOTTOM_BORDER);
            System.out.print(YELLOW_BOLD + "\n  » Select an option (1-8): " + RESET);

            int choice = 0;
            try { choice = sc.nextInt(); sc.nextLine(); } catch (InputMismatchException e) { System.out.println(RED_BOLD + "\n  ❌ Invalid Input! Please enter a number (1-9)." + RESET); sc.nextLine(); continue; }

            switch (choice) {
                case 1 -> addProduct();
                case 2 -> searchProductById();
                case 3 -> viewAllProducts();
                case 4 -> updateProduct();
                case 5 -> deleteProduct();
                case 6 -> {
                    System.out.print(CYAN + "  📧 Enter recipient email for report: " + RESET);
                    String email = sc.nextLine().trim();
                    if(email.isEmpty()) { System.out.println(RED_BOLD + "  ❌ Email cannot be empty." + RESET); break; }
                    try {
                        System.out.println(YELLOW + "\n  ⏳ Generating and sending report... Please wait..." + RESET);
                        ArrayList<Product> products = productDao.getAllProducts();
                        String filePath = GenerateCSV.generateProductsReport(products,"ADMIN");
                        EmailService.sendReport(email,"Daily Inventory Report","Attached is your latest Inventory Report",filePath);
                        System.out.println(GREEN_BOLD + "\n  ✅ Inventory Report sent to " + email + " successfully!\n" + RESET);
                    } catch (Exception e) { System.out.println(RED_BOLD + "\n  ❌ Error generating/sending report: " + e.getMessage() + RESET); }
                }
                case 7 -> filterProduct();
                case 8 -> { System.out.println(CYAN_BOLD + "\n  👋 Admin logged out successfully.\n" + RESET); return; }
                default -> System.out.println(RED_BOLD + "\n  ❌ Invalid Choice. Please try again." + RESET);
            }
        }
    }

    public static void userMenu() {
        while (true) {
            System.out.println(MENU_TOP_BORDER);
            System.out.println(PURPLE_BOLD + "        📦  USER INVENTORY DASHBOARD  " + RESET);
            System.out.println(MENU_SEPARATOR);
            System.out.println(GREEN + "  1️⃣  " + RESET + "Search Product by ID");
            System.out.println(GREEN + "  2️⃣  " + RESET + "View All Products");
            System.out.println(GREEN + "  3️⃣  " + RESET + "Filter Products by Price Range");
            System.out.println(RED + "  4️⃣  " + RESET + "Logout");
            System.out.println(MENU_BOTTOM_BORDER);
            System.out.print(YELLOW_BOLD + "\n  » Select an option (1-4): " + RESET);

            int choice = 0;
            try { choice = sc.nextInt(); sc.nextLine(); } catch (InputMismatchException e) { System.out.println(RED_BOLD + "\n  ❌ Invalid Input! Please enter a number (1-4)." + RESET); sc.nextLine(); continue; }

            switch (choice) {
                case 1 -> searchProductById();
                case 2 -> viewAllProducts();
                case 3 -> filterProduct();
                case 4 -> { System.out.println(CYAN_BOLD + "\n  👋 Logged out successfully.\n" + RESET); return; }
                default -> System.out.println(RED_BOLD + "\n  ❌ Invalid Choice. Please try again." + RESET);
            }
        }
    }

    // -------------------- PRODUCT OPERATIONS --------------------

    public static void addProduct() {
        try {
            System.out.println(PURPLE_BOLD + "\n  ========================================");
            System.out.println("  │ " + WHITE_BOLD + "  📦  ADD NEW PRODUCT   " + PURPLE_BOLD + "   │");
            System.out.println("  ========================================" + RESET);
            System.out.print(CYAN + "  🔢  Enter Product ID (Number) : " + RESET);
            int productId = sc.nextInt(); sc.nextLine();
            System.out.print(CYAN + "  🏷️  Enter Product Name      : " + RESET);
            String productName = sc.nextLine().trim();
            System.out.print(CYAN + "  📦  Enter Quantity (Number) : " + RESET);
            int quantity = sc.nextInt(); sc.nextLine();
            System.out.print(CYAN + "  💰  Enter Price (Decimal)   : " + RESET);
            double price = sc.nextDouble(); sc.nextLine();
            System.out.print(CYAN + "  🗂️  Enter Category          : " + RESET);
            String category = sc.nextLine().trim();
            System.out.print(CYAN + "  🚨  Enter Threshold Limit   : " + RESET);
            String thresholdValue = sc.nextLine();
            int threshold = (thresholdValue==null || thresholdValue.isEmpty())? 15 : parseInt(thresholdValue);

            Product p = new Product(productId, productName, quantity, price, category, threshold);
            productDao.addProduct(p);
            System.out.println(GREEN_BOLD + "\n  ✅ Product added successfully!\n" + RESET);
        } catch (InputMismatchException e) {
            System.out.println(RED_BOLD + "\n  ❌ Invalid Input! Please check your data types." + RESET);
            sc.nextLine();
        } catch (Exception e) {
            System.out.println(RED_BOLD + "\n  ❌ UNEXPECTED ERROR: " + (e.getMessage() != null ? e.getMessage() : "Please try again.") + RESET);
        }
    }

    public static void searchProductById() {
        try {
            System.out.println(PURPLE_BOLD + "\n  ========================================");
            System.out.println("  │ " + WHITE_BOLD + "  🔍  SEARCH PRODUCT BY ID  " + PURPLE_BOLD + "  │");
            System.out.println("  ========================================" + RESET);
            System.out.print(CYAN + "  🔢 Enter Product ID : " + RESET);
            int searchId = sc.nextInt(); sc.nextLine();
            Product p = productDao.getProductById(searchId);

            if (p == null) { System.out.println(YELLOW_BOLD + "\n  ⚠️  No product found with ID " + searchId + RESET); return; }

            System.out.println(GREEN_BOLD + "\n  📋 Search Results for ID " + searchId + ":\n" + RESET);
            System.out.printf(TABLE_HEADER_FORMAT, "ID", "Product Name", "Quantity", "Price", "Category", "Threshold");
            System.out.println(TABLE_SEPARATOR);
            System.out.printf(TABLE_ROW_FORMAT, p.getProductId(), p.getProductName(), p.getQuantity(), String.format("$%.2f", p.getPrice()), p.getCategory(), p.getThreshold());
        } catch (InputMismatchException e) { System.out.println(RED_BOLD + "\n  ❌ Invalid Input! Please enter a valid number." + RESET); sc.nextLine(); }
        catch (Exception e) { System.out.println(RED_BOLD + "\n  ❌ UNEXPECTED ERROR: " + (e.getMessage() != null ? e.getMessage() : "Please try again.") + RESET); }
    }

    public static void viewAllProducts() {
        try {
            System.out.println(PURPLE_BOLD + "\n  =======================================================");
            System.out.println("  │ " + WHITE_BOLD + "  📦  COMPLETE PRODUCT INVENTORY   " + PURPLE_BOLD + "            │");
            System.out.println("  =======================================================" + RESET);
            ArrayList<Product> products = productDao.getAllProducts();

            if (products.isEmpty()) { System.out.println(YELLOW_BOLD + "\n  ⚠️  No products available in inventory.\n" + RESET); return; }

            System.out.printf(TABLE_HEADER_FORMAT, "ID", "Product Name", "Quantity", "Price", "Category", "Threshold");
            System.out.println(TABLE_SEPARATOR);
            for (Product p : products) {
                System.out.printf(TABLE_ROW_FORMAT, p.getProductId(), p.getProductName(), p.getQuantity(), String.format("$%.2f", p.getPrice()), p.getCategory(), p.getThreshold());
            }
            System.out.println(CYAN_BOLD + "\n  Total Products: " + products.size() + RESET);
        } catch (Exception e) { System.out.println(RED_BOLD + "\n  ❌ UNEXPECTED ERROR: " + (e.getMessage() != null ? e.getMessage() : "Please try again.") + RESET); }
    }

    public static void updateProduct() {
        try {
            System.out.println(PURPLE_BOLD + "\n  ========================================");
            System.out.println("  │ " + WHITE_BOLD + "  ✏️  UPDATE PRODUCT DETAILS  " + PURPLE_BOLD + "   │");
            System.out.println("  ========================================" + RESET);
            System.out.print(CYAN + "  🔢  Enter Product ID to update : " + RESET);
            int updateId = sc.nextInt(); sc.nextLine();
            System.out.print(CYAN + "  🏷️  Enter New Product Name (leave blank) : " + RESET);
            String updateName = sc.nextLine().trim();
            System.out.print(CYAN + "  📦  Enter New Quantity (0 for no change) : " + RESET);
            int updateQuantity = sc.nextInt(); sc.nextLine();
            System.out.print(CYAN + "  💰  Enter New Price (0.0 for no change)  : " + RESET);
            double updatePrice = sc.nextDouble(); sc.nextLine();
            System.out.print(CYAN + "  🗂️  Enter New Category (leave blank)     : " + RESET);
            String updateCategory = sc.nextLine().trim();
            System.out.print("Enter new Threshold Value (0 for no change): ");
            String updateThre = sc.nextLine();
            int updateThreshold = parseInt(updateThre);

            Product updateProduct = new Product(updateId, updateName, updateQuantity, updatePrice, updateCategory, updateThreshold, true);
            productDao.updateInventory(updateProduct);
            System.out.println(GREEN_BOLD + "\n  ✅ Product details updated successfully!\n" + RESET);
        } catch (InputMismatchException e) { System.out.println(RED_BOLD + "\n  ❌ Invalid Input! ID, Quantity, and Price must be numbers." + RESET); sc.nextLine(); }
        catch (Exception e) { System.out.println(RED_BOLD + "\n  ❌ UNEXPECTED ERROR: " + (e.getMessage() != null ? e.getMessage() : "Please try again.") + RESET); }
    }

    public static void deleteProduct() {
        try {
            System.out.println(PURPLE_BOLD + "\n  ========================================");
            System.out.println("  │ " + WHITE_BOLD + "      🗑️  DELETE PRODUCT      " + PURPLE_BOLD + "   │");
            System.out.println("  ========================================" + RESET);
            System.out.print(CYAN + "  🔢 Enter Product ID to delete : " + RESET);
            int deleteId = sc.nextInt(); sc.nextLine();
            productDao.deleteProduct(deleteId);
            System.out.println(RED_BOLD + "\n  ⚠️  Product with ID " + deleteId + " deleted successfully!\n" + RESET);
        } catch (InputMismatchException e) { System.out.println(RED_BOLD + "\n  ❌ Invalid Input! Please enter a valid number." + RESET); sc.nextLine(); }
        catch (Exception e) { System.out.println(RED_BOLD + "\n  ❌ UNEXPECTED ERROR: " + (e.getMessage() != null ? e.getMessage() : "Please try again.") + RESET); }
    }

    public static void filterProduct() {
        try {
            System.out.println(PURPLE_BOLD + "\n  ========================================");
            System.out.println("  │ " + WHITE_BOLD + "  📊  FILTER PRODUCTS BY PRICE RANGE " + PURPLE_BOLD + "  │");
            System.out.println("  ========================================" + RESET);
            System.out.print(CYAN + "  💰 Enter Minimum Price : " + RESET);
            double minPrice = sc.nextDouble(); sc.nextLine();
            System.out.print(CYAN + "  💰 Enter Maximum Price : " + RESET);
            double maxPrice = sc.nextDouble(); sc.nextLine();

            System.out.println(YELLOW_BOLD + "\n  🔎 Products in the price range $" + String.format("%.2f", minPrice) + " - $" + String.format("%.2f", maxPrice) + ":\n" + RESET);
            productDao.FilterRange(minPrice, maxPrice);
        } catch (InputMismatchException e) { System.out.println(RED_BOLD + "\n  ❌ Invalid input! Please enter numeric values for price." + RESET); sc.nextLine(); }
        catch (Exception e) { System.out.println(RED_BOLD + "\n  ❌ UNEXPECTED ERROR: " + (e.getMessage() != null ? e.getMessage() : "Please try again.") + RESET); }
    }
}
