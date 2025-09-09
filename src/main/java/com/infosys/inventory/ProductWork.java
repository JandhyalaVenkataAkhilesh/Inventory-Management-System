package com.infosys.inventory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ProductWork {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ProductAccess product = new ProductAccess();

        System.out.println("Enter the following Product Details");
        System.out.println("Enter Product Id : ");
        int productId = sc.nextInt();

        System.out.println("Enter Product Name : ");
        String productName = sc.next();

        System.out.println("Enter Product Quantity : ");
        int productQuantity = sc.nextInt();

        System.out.println("Enter Product Price : ");
        double productPrice = sc.nextDouble();

        sc.nextLine();

        System.out.println("Enter Product DeliveryDate(dd-MM-yyyy) : ");
        String dateInput = sc.nextLine();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date productDeliveryDate = null;
        try {
            productDeliveryDate = sdf.parse(dateInput);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        product.insertProduct(productId,productName,productQuantity,productPrice,productDeliveryDate);
        System.out.println("----Product Details----");
        product.displayProduct();
    }

}
