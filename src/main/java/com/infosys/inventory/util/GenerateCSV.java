package com.infosys.inventory.util;

import com.infosys.inventory.dao.ProductDao;
import com.infosys.inventory.model.Product;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class GenerateCSV {
    public void reportCSV(String filePath){
        ProductDao dao = new ProductDao();
        ArrayList<Product> productArrayList = dao.getAllProducts();
        try(PrintWriter writer = new PrintWriter(new File(filePath))){
            writer.println("ProductId,ProductName,ProductQuantity,ProductPrice,ProductCategory");
            for(Product pro:productArrayList){
                writer.println(pro.getProductId()+","+pro.getProductName()+","+pro.getQuantity()+","+ pro.getPrice()+","+pro.getCategory());
            }
            System.out.println("File is created at : " + filePath);
        }catch (FileNotFoundException e){
            System.out.println("Error cause while creating csv file : "+ e.getMessage());
        }

    }
}
