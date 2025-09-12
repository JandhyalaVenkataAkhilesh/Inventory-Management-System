package com.infosys.inventory;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ProductWork {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ProductAccess obj = new ProductAccess();
        while(true){
            System.out.println("1. Add Product\n2.View All Products\n3.Search Product by ID\n4. Update Product\n5.Remove Product\n6.Exit");
            int choice = 0;
            try {
                System.out.print("Enter you choice : ");
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input, Please give valid Input");
            }
            sc.nextLine();
            switch (choice){
                case 1 : obj.insertProduct();break;
                case 2 : obj.viewAllProducts(); break;
                case 3 : obj.searchProductById(); break;
                case 4 : obj.updateProduct();break;
                case 5 : obj.deleteProduct();break;
                case 6 : System.exit(0);
                default : System.out.println("Invalid choice try again");break;
            }
        }
    }

}
