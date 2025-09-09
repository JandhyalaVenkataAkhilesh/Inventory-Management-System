package com.infosys.inventory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProductAccess {
    ArrayList<Product> list = new ArrayList<>();

    public void insertProduct(int productId, String productName, int productQuantity, double productPrice,Date deliveryDate){
        Product material = new Product();
        material.setProductId(productId);
        material.setProductName(productName);
        material.setQuantity(productQuantity);
        material.setPrice(productPrice);
        material.setDeliveryDate(deliveryDate);
        list.add(material);
    }

    public void displayProduct(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        for(Product pro:list){
            System.out.println("Product Id : " + pro.getProductId());
            System.out.println("Product Name : " + pro.getProductName());
            System.out.println("Product Quantity : " + pro.getQuantity());
            System.out.println("Product Price : " + pro.getPrice());
            System.out.println("Product Delivery Date : " + sdf.format(pro.getDeliveryDate()));
        }
    }

}
