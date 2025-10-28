package com.infosys.inventory;

import static com.infosys.inventory.util.DbConnection.*;

public class Demo {
    public static void main(String[] args) {
        System.out.println("connectionUrl: " + connectionUrl);
        System.out.println("userName: " + userName);
        System.out.println("password: " + (password != null ? password : null));
    }
}