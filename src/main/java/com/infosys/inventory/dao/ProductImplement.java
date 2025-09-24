package com.infosys.inventory.dao;

import com.infosys.inventory.exceptions.ProductNotFoundException;
import com.infosys.inventory.model.Product;
import com.infosys.inventory.util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductImplement implements ProductDaoInterface {

    @Override
    public void addProduct(Product p) throws SQLException {
        Connection con = DbConnection.getConnect();
        PreparedStatement pstmt = con.prepareStatement("insert into products values (?,?,?,?,?);");
        pstmt.setInt(1,p.getProductId());
        pstmt.setString(2,p.getProductName());
        pstmt.setInt(3,p.getQuantity());
        pstmt.setDouble(4,p.getPrice());
        pstmt.setString(5,p.getCategory());
        int count = pstmt.executeUpdate();
        if(count == 1){
            System.out.println(count + "record successfully inserted");
        }else{
            System.out.println("Record is failed to insert");
        }
    }

    @Override
    public List<Product> getAllProducts() throws SQLException {
        List<Product> records = new ArrayList<>();
        Connection con = DbConnection.getConnect();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from products;");
        while(rs.next()){
            Product p = new Product();
            p.setProductId(rs.getInt(1));
            p.setProductName(rs.getString(2));
            p.setQuantity(rs.getInt(3));
            p.setPrice(rs.getDouble(4));
            p.setCategory(rs.getString(5));
            records.add(p);
        }
        return records;
    }

    @Override
    public Product getProductById(int productId) throws SQLException {
        Connection con = DbConnection.getConnect();
        PreparedStatement pstmt = con.prepareStatement("select * from products where product_id=?;");
        pstmt.setInt(1,productId);
        ResultSet rs = pstmt.executeQuery();
        if(!rs.next()){
            throw new ProductNotFoundException("No products in the inventory");
        }
        Product p = new Product();
        p.setProductId(rs.getInt(1));
        p.setProductName(rs.getString(2));
        p.setQuantity(rs.getInt(3));
        p.setPrice(rs.getDouble(4));
        p.setCategory(rs.getString(5));
        return p;
    }

    @Override
    public boolean updateProduct(int productId, int productQuantity) throws SQLException {
        Connection con = DbConnection.getConnect();
        PreparedStatement spstmt = con.prepareStatement("select * from products where product_id=?;");
        spstmt.setInt(1,productId);
        ResultSet rs = spstmt.executeQuery();
        if(!rs.next()){
            throw new ProductNotFoundException("No products with Product ID : " + productId);
        }
        PreparedStatement pstmt = con.prepareStatement("update products set product_quantity=? where product_id=?;");
        pstmt.setInt(1,productQuantity);
        pstmt.setInt(2,productId);
        int count = pstmt.executeUpdate();
        if(count == 1){
            System.out.println(count + "record successfully updated");
        }else{
            System.out.println("fail to update record");
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteProduct(int productId) throws SQLException {
        Connection con = DbConnection.getConnect();
        PreparedStatement spstmt = con.prepareStatement("select * from products where product_id=?;");
        spstmt.setInt(1,productId);
        ResultSet rs = spstmt.executeQuery();
        if(!rs.next()){
            throw new ProductNotFoundException("No products with Product ID : " + productId);
        }
        PreparedStatement pstmt = con.prepareStatement("delete from products where product_id=?;");
        pstmt.setInt(1,productId);
        int count = pstmt.executeUpdate();
        if(count == 1){
            System.out.println(count + "record successfully deleted");
        }else{
            System.out.println("failed to delete record");
            return false;
        }
        return true;
    }
}
