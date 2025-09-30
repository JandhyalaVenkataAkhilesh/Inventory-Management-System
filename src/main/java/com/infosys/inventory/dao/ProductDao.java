package com.infosys.inventory.dao;

import com.infosys.inventory.exceptions.ProductNotFoundException;
import com.infosys.inventory.model.Product;
import com.infosys.inventory.util.DbConnection;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductDao {

    // insert a record into inventory
    public void addProduct(Product p) {
        try(Connection con = DbConnection.getConnect();
            PreparedStatement pstmt = con.prepareStatement("insert into products values(?,?,?,?,?);")
        ){
            pstmt.setInt(1,p.getProductId());
            pstmt.setString(2,p.getProductName());
            pstmt.setInt(3,p.getQuantity());
            pstmt.setDouble(4,p.getPrice());
            pstmt.setString(5,p.getCategory());
            int count = pstmt.executeUpdate();
            if(count == 1){
                System.out.println(count + " record successfully inserted");
            }else{
                System.out.println("Failed to insert the record into inventory");
            }
        }catch(SQLException e){
            throw new RuntimeException("Terminated due to " + e.getMessage());
        }
    }

    // retrieve a specific record from inventory
    public Product getProductById(int productId){
        try(Connection con = DbConnection.getConnect();
            PreparedStatement pstmt = con.prepareStatement("select * from products where product_id=?;")
        ){
            pstmt.setInt(1,productId);
            ResultSet rs = pstmt.executeQuery();

            if(!rs.next()) {
                throw new ProductNotFoundException("Inventory is empty");
            }
//            System.out.println("Product Name : " + rs.getString(2));
//            System.out.println("Quantity : " + rs.getInt(3));
//            System.out.println("Price : " + rs.getDouble(4));
//            System.out.println("Category : " + rs.getString(5));
              Product p = new Product();
              p.setProductId(rs.getInt(1));
              p.setProductName(rs.getString(2));
              p.setQuantity(rs.getInt(3));
              p.setPrice(rs.getDouble(4));
              p.setCategory(rs.getString(5));
              return p;
        } catch (SQLException e) {
            throw new RuntimeException("Terminated due to " + e.getMessage());
        }
    }

    // retrieve all records from inventory
//    public void getProducts(){
//        try(Connection con = DbConnection.getConnect();
//            Statement stmt = con.createStatement();
//            ResultSet rs = stmt.executeQuery("select * from products;")
//        ){
//            boolean found = false;
//            while(rs.next()){
//                found = true;
//                System.out.println("Product ID : " + rs.getInt(1));
//                System.out.println("Product Name : " + rs.getString(2));
//                System.out.println("Quantity : " + rs.getInt(3));
//                System.out.println("Price : " + rs.getDouble(4));
//                System.out.println("Category : " + rs.getString(5));
//                System.out.println();
//            }
//            if(!found){
//                throw new ProductNotFoundException("Inventory is empty");
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException("Terminated due to " + e.getMessage());
//        }
//    }

    //update the record from inventory
    public void updateInventory(Product p){
        try(Connection con = DbConnection.getConnect();
            PreparedStatement pstmt = con.prepareStatement("update products set product_name=?,product_quantity=?,product_price=?,product_category=? where product_id=?;");
            PreparedStatement spstmt = con.prepareStatement("select * from products where product_id=?")

        ){
            spstmt.setInt(1,p.getProductId());
            ResultSet rs = spstmt.executeQuery();
            if(!rs.next()){
                throw new ProductNotFoundException("Inventory is empty");
            }
            Product obj = new Product();
            obj.setProductId(rs.getInt(1));
            obj.setProductName(rs.getString(2));
            obj.setQuantity(rs.getInt(3));
            obj.setPrice(rs.getDouble(4));
            obj.setCategory(rs.getString(5));

            if(p.getProductName().isEmpty() || p.getProductName() == null){
                p.setProductName(obj.getProductName());
            }
            if(p.getQuantity() == 0){
                p.setQuantity(obj.getQuantity());
            }
            if(p.getPrice() == 0){
                p.setPrice(obj.getPrice());
            }
            if(p.getCategory() == null || p.getCategory().isEmpty()){
                p.setCategory(obj.getCategory());
            }

            pstmt.setString(1,p.getProductName());
            pstmt.setInt(2,p.getQuantity());
            pstmt.setDouble(3,p.getPrice());
            pstmt.setString(4,p.getCategory());
            pstmt.setInt(5,p.getProductId());

            int count = pstmt.executeUpdate();

            if(count == 1){
                System.out.println(count + " record successfully updated");
            }else{
                System.out.println("Failed to update");
            }
        }catch (SQLException e){
            throw new RuntimeException("Terminated due to " + e.getMessage());
        }
    }

    //delete a record from inventory
    public void deleteProduct(int productId){
        try(Connection con = DbConnection.getConnect();
            PreparedStatement pstmt = con.prepareStatement("delete from products where product_id=?;")
        ){
            pstmt.setInt(1,productId);
            int count = pstmt.executeUpdate();
            if(count == 1){
                System.out.println(count + " record successfully deleted");
            }else{
                throw new ProductNotFoundException("Inventory is empty");
            }
        }catch (SQLException e){
            throw new RuntimeException("Terminated due to " + e.getMessage());
        }
    }

    public ArrayList<Product> getAllProducts(){
        try(Connection con = DbConnection.getConnect();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from products")
        ){
            ArrayList<Product> allProducts = new ArrayList<>();
            while (rs.next()){
                Product obj = new Product();
                obj.setProductId(rs.getInt(1));
                obj.setProductName(rs.getString(2));
                obj.setQuantity(rs.getInt(3));
                obj.setPrice(rs.getDouble(4));
                obj.setCategory(rs.getString(5));
                allProducts.add(obj);
            }

            return allProducts;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
