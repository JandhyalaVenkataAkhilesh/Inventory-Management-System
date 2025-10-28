package com.infosys.inventory.dao;

import com.infosys.inventory.model.Product;
import com.infosys.inventory.util.DbConnection;

import java.sql.*;
import java.util.ArrayList;

public class ProductDao implements ProductDaoInterface{
    public static final String RESET = "\u001B[0m";
    public static final String GREEN_BOLD = "\u001B[1m\u001B[32m";
    public static final String RED_BOLD = "\u001B[1m\u001B[31m";
    // insert a record into inventory
    @Override
    public void addProduct(Product p) {
        try (Connection con = DbConnection.getConnect();
             PreparedStatement pstmt = con.prepareStatement("insert into products values(?,?,?,?,?,?)")) {

            pstmt.setInt(1, p.getProductId());
            pstmt.setString(2, p.getProductName());
            pstmt.setInt(3, p.getQuantity());
            pstmt.setDouble(4, p.getPrice());
            pstmt.setString(5, p.getCategory());
            pstmt.setInt(6,p.getThreshold());
            int count=pstmt.executeUpdate();
            if(count==1)
                System.out.println(GREEN_BOLD + "\n  ✅ Product added successfully!\n" + RESET);
            else
                System.out.println(" ❌User not Found");

        } catch (SQLException e) {
            System.out.println("❌ Failed to insert record – " + e.getMessage());
        }
    }

    // retrieve a specific record
    @Override
    public Product getProductById(int productId) {
        try (Connection con = DbConnection.getConnect();
             PreparedStatement pstmt = con.prepareStatement("select * from products where product_id=?")) {

            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ Failed to retrieve record – product not found.");
                return null;
            }

            Product p = new Product();
            p.setProductId(rs.getInt(1));
            p.setProductName(rs.getString(2));
            p.setQuantity(rs.getInt(3));
            p.setPrice(rs.getDouble(4));
            p.setCategory(rs.getString(5));
            p.setThreshold(rs.getInt(6));
            return p;

        } catch (SQLException e) {
            System.out.println("❌ Failed to retrieve record – " + e.getMessage());
            return null;
        }
    }

    // update record
    @Override
    public void updateProduct(Product p) {
        try (Connection con = DbConnection.getConnect();
             PreparedStatement pstmt = con.prepareStatement(
                     "update products set product_name=?,product_quantity=?,product_price=?,product_category=?,threshold=? where product_id=?");
             PreparedStatement spstmt = con.prepareStatement("select * from products where product_id=?")) {

            spstmt.setInt(1, p.getProductId());
            ResultSet rs = spstmt.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ Failed to update record – product not found.");
                return;
            }

            Product obj = new Product(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDouble(4), rs.getString(5),rs.getInt(6));

            if (p.getProductName() == null || p.getProductName().isEmpty()) p.setProductName(obj.getProductName());
            if (p.getQuantity() == 0) p.setQuantity(obj.getQuantity());
            if (p.getPrice() == 0) p.setPrice(obj.getPrice());
            if (p.getCategory() == null || p.getCategory().isEmpty()) p.setCategory(obj.getCategory());
            if(p.getThreshold() == 0) p.setThreshold(obj.getThreshold());
            pstmt.setString(1, p.getProductName());
            pstmt.setInt(2, p.getQuantity());
            pstmt.setDouble(3, p.getPrice());
            pstmt.setString(4, p.getCategory());
            pstmt.setInt(5,p.getThreshold());
            pstmt.setInt(6, p.getProductId());

            int count=pstmt.executeUpdate();
            if(count==1)
                System.out.println(GREEN_BOLD + "\n  ✅ Product details updated successfully!\n" + RESET);
            else
                System.out.println("❌ No product found with this ID: " + p.getProductId());

        } catch (SQLException e) {
            System.out.println("❌ Failed to update record – " + e.getMessage());
        }
    }

    // delete record
    @Override
    public void deleteProduct(int productId) {
        try (Connection con = DbConnection.getConnect();
             PreparedStatement pstmt = con.prepareStatement("delete from products where product_id=?")) {

            pstmt.setInt(1, productId);
            int count=pstmt.executeUpdate();

            if(count==1)
                System.out.println(RED_BOLD + "\n  ⚠️  Product with ID " + productId + " deleted successfully!\n" + RESET);
            else
                System.out.println("❌ No product found with this ID: " + productId);
        } catch (SQLException e) {
            System.out.println("❌ Failed to delete record – " + e.getMessage());
        }
    }

    // retrieve all records
    @Override
    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> allProducts = new ArrayList<>();
        try (Connection con = DbConnection.getConnect();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("select * from products")) {

            while (rs.next()) {
                Product obj = new Product();
                obj.setProductId(rs.getInt(1));
                obj.setProductName(rs.getString(2));
                obj.setQuantity(rs.getInt(3));
                obj.setPrice(rs.getDouble(4));
                obj.setCategory(rs.getString(5));
                obj.setThreshold(rs.getInt(6));
                allProducts.add(obj);
            }

            if (allProducts.isEmpty())
                System.out.println("❌ Failed to retrieve records – inventory is empty.");

        } catch (SQLException e) {
            System.out.println("❌ Failed to retrieve records – " + e.getMessage());
        }
        return allProducts;
    }

    // filter products by price range
    @Override
    public void FilterRange(double minPrice, double maxPrice) {
        try (Connection con = DbConnection.getConnect();
             PreparedStatement pstmt = con.prepareStatement("SELECT * FROM products WHERE product_price BETWEEN ? AND ?")) {

            pstmt.setDouble(1, minPrice);
            pstmt.setDouble(2, maxPrice);
            ResultSet rs = pstmt.executeQuery();

            boolean found = false;
            System.out.println("-----------------------------------------------------------------------------------------");
            System.out.printf("%-10s %-25s %-15s %-10s %-15s %-15s%n",
                    "ID", "PRODUCT NAME", "QUANTITY", "PRICE", "CATEGORY", "THRESHOLD");
            System.out.println("-----------------------------------------------------------------------------------------");

            while (rs.next()) {
                found = true;
                System.out.printf("%-10d %-25s %-15d %-10.2f %-15s %-15d%n",
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("product_quantity"),
                        rs.getDouble("product_price"),
                        rs.getString("product_category"),
                        rs.getInt("threshold"));
            }

            if (!found)
                System.out.println("❌ No products found within the given price range.");

            System.out.println("-----------------------------------------------------------------------------------------");

        } catch (SQLException e) {
            System.out.println("❌ Failed to filter products – " + e.getMessage());
        }
    }
}
