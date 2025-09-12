package com.infosys.inventory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ProductAccess {

    HashMap<Integer,Product> map = new HashMap<>();
    Scanner sc = new Scanner(System.in);
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");


    public void insertProduct(){

        Product material = new Product();
        System.out.println("Enter the following Product Details");
        int productId = 0;
        String productName = null;
        int productQuantity = 0;
        double productPrice = 0;
        String dateInput = null;
        try {
            System.out.print("Enter Product Id : ");
            productId = sc.nextInt();
            sc.nextLine();
            //verifying if the product exists in the inventory
            if(map.containsKey(productId)){
                throw new CustomExceptions.ProductExists("Hey user, Product Id " + productId + " is already exists");
            }

            System.out.print("Enter Product Name : ");
            productName = sc.nextLine();
            //verifying if the product exists in the inventory
            for (Product p : map.values()) {
                if (p.getProductName().equalsIgnoreCase(productName)) {
                    throw new CustomExceptions.ProductExists("Product Name " + productName + " already exists");
                }
            }

            System.out.print("Enter Product Quantity : ");
            productQuantity = sc.nextInt();
            System.out.print("Enter Product Price : ");
            productPrice = sc.nextDouble();
            sc.nextLine();
            System.out.print("Enter Product DeliveryDate(dd-MM-yyyy) : ");
            dateInput = sc.nextLine();
        } catch (Exception e) {
            throw new CustomExceptions.NoProduct("Invalid input. Product not added.");
        }


        Date productDeliveryDate;
        try {
            productDeliveryDate = sdf.parse(dateInput);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        material.setProductId(productId);
        material.setProductName(productName);
        material.setQuantity(productQuantity);
        material.setPrice(productPrice);
        material.setDeliveryDate(productDeliveryDate);
        map.put(productId,material);
        System.out.println("Product added successfully");
    }

    public void searchProductById(){
        int searchKey = 0;
        try {
            System.out.print("Enter Product Id : ");
            searchKey = sc.nextInt();
        }  catch (Exception e) {
            throw new RuntimeException(e);
        }
        sc.nextLine();

        if(map.isEmpty()){
            throw new CustomExceptions.InventoryEmpty("Inventory is Empty");
        }

        if(map.containsKey(searchKey)){
            Product p = map.get(searchKey);
            System.out.println("Product Id : " + p.getProductId());
            System.out.println("Product Name : " + p.getProductName());
            System.out.println("Product Quantity : " + p.getQuantity());
            System.out.println("Product Price : " + p.getPrice());
            System.out.println("Product Delivery Date : " + p.getDeliveryDate());
        }else{
            throw new CustomExceptions.NoProduct("There are no products with Id " + searchKey);
        }
    }

    public void updateProduct(){

            System.out.print("Enter Product Id to Update the Product : ");
            int searchKey = sc.nextInt();
            sc.nextLine();

            if(map.isEmpty()){
                throw new CustomExceptions.InventoryEmpty("Inventory is Empty");
            }

            if(map.containsKey(searchKey)){
                Product p = map.get(searchKey);
                System.out.print("Do you want to change the Product Name(Yes/No): ");
                String changeName = sc.nextLine();
                if(changeName.equalsIgnoreCase("yes")){
                    System.out.print("Enter New Product Name : ");
                    String newName = sc.nextLine();
                    p.setProductName(newName);
                    System.out.println("Product Name successfully Updated");
                }else{
                    System.out.println("Ok, you can change it Later");
                }

                System.out.print("Do you want to change the Product Quantity(Yes/No): ");
                String changeQuantity = sc.nextLine();
                if(changeQuantity.equalsIgnoreCase("yes")){
                    System.out.print("Enter New Product Quantity : ");
                    int newQuantity = sc.nextInt();
                    sc.nextLine();
                    p.setQuantity(newQuantity);
                    System.out.println("Product Quantity successfully Updated");
                }else{
                    System.out.println("Ok, you can change it Later");
                }

                System.out.print("Do you want to change the Product Price(Yes/No): ");
                String changePrice = sc.nextLine();
                if(changePrice.equalsIgnoreCase("yes")){
                    System.out.print("Enter New Product Price : ");
                    double newPrice = sc.nextDouble();
                    sc.nextLine();
                    p.setPrice(newPrice);
                    System.out.println("Product Price successfully Updated");
                }else{
                    System.out.println("Ok, you can change it Later");
                }

                System.out.print("Do you want to change the Product Delivery Date(Yes/No): ");
                String changeDate = sc.nextLine();
                if(changeDate.equalsIgnoreCase("yes")){
                    System.out.print("Enter New Product Delivery Date : ");
                    String dateInput = sc.nextLine();
                    try {
                        p.setDeliveryDate(sdf.parse(dateInput));
                        System.out.println("Product Date successfully Updated");
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    System.out.println("Ok, you can change it Later");
                }
            }
    }

    public void deleteProduct(){
        System.out.print("Enter the Product Id : ");
        int deleteId = sc.nextInt();
        sc.nextLine();
        if(map.isEmpty()){
            throw new CustomExceptions.InventoryEmpty("Inventory is empty, not possible for delete operation");
        }
        System.out.print("Are you sure do you want to delete the product(Yes/No) :");
        String deleteChoice = sc.nextLine();
        if(deleteChoice.equalsIgnoreCase("yes")){
            if(map.containsKey(deleteId)){
                map.remove(deleteId);
                System.out.println("Product Deleted Successfully");
            }else{
                throw new CustomExceptions.NoProduct("There is no product with Id " + deleteId);
            }
        }else{
            System.out.println("The product is not deleted");
        }
    }

    public void viewAllProducts(){
        if(map.isEmpty()){
            throw new CustomExceptions.NoProduct("Inventory is empty");
        }
        for(Product p: map.values()){
            System.out.println(p.toString());
        }
    }

}
