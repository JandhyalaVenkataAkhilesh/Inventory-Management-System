package com.infosys.inventory.model;

public class Product {

    private int productId;
    private String productName;
    private int quantity;
    private double price;
    private String category;
    private int threshold;

    public Product() { super(); }

    // Constructor for adding/updating products (strict validation)
    public Product(int productId, String productName, int quantity, double price, String category) {
        setProductId(productId);
        setProductName(productName);
        setQuantity(quantity);
        setPrice(price);
        setCategory(category);
    }

    // Constructor for loading from DB (bypasses validation)
    public Product(int productId, String productName, int quantity, double price, String category, boolean fromDB) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
    }

    public Product(int productId, String productName, int quantity, double price, String category, int threshold) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
        this.threshold = threshold;
    }

    public Product(int productId, String productName, int quantity, double price, String category, int threshold, boolean fromDB) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
        this.threshold = threshold;
    }

    // Getters and setters with strict validation
    public int getProductId() { return productId; }
    public void setProductId(int productId) {
        if (productId < 0) throw new IllegalArgumentException("❌ Product Id should be positive");
        this.productId = productId;
    }

    public String getProductName() { return productName; }
    public void setProductName(String productName) {
        if (productName == null || productName.trim().isEmpty())
            throw new IllegalArgumentException("❌ Product Name should not be empty");
        this.productName = productName;
    }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("❌ Quantity should be positive");
        this.quantity = quantity;
    }

    public double getPrice() { return price; }
    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("❌ Price should be positive");
        this.price = price;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) {
        if (category == null || category.trim().isEmpty())
            throw new IllegalArgumentException("❌ Product Category should not be empty");
        this.category = category;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public String toString() {
        return String.format("Product{id=%d, name='%s', quantity=%d, price=%.2f, category='%s'}",
                productId, productName, quantity, price, category);
    }
}