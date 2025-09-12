package com.infosys.inventory;

public class CustomExceptions {
    public static class ProductExists extends RuntimeException{
        ProductExists(String  message){
            super(message);
        }
    }

    public static class InventoryEmpty extends RuntimeException{
        InventoryEmpty(String message){
            super(message);
        }
    }

    public static class NoProduct extends RuntimeException{
        NoProduct(String message){
            super(message);
        }
    }
}
