package com.infosys.inventory.model;
public class Product {

    private int productId;
    private String productName;
    private int quantity;
    private double price;
    private String Category;

    public Product(){
        super();
    }

    public Product(int productId, String productName, int quantity, double price, String Category) {
        setProductId(productId);
        setProductName(productName);
        setQuantity(quantity);
        setPrice(price);
        setCategory(Category);
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        if(productId < 0)
            throw new IllegalArgumentException("❌ Product Id should be positive number");
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        if(productName==null || productName.trim().isEmpty())
            throw new IllegalArgumentException("❌ Product Name should no empty");
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if(quantity<0)
            throw new IllegalArgumentException("❌ Quantity should be positive number");
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if(price < 0)
            throw new IllegalArgumentException("❌ price should be positive number");
        this.price = price;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        if (category == null || category.trim().isEmpty())
            throw new IllegalArgumentException("❌ Product Category should no empty");
        Category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", deliveryDate=" + Category +
                '}';
    }
}
