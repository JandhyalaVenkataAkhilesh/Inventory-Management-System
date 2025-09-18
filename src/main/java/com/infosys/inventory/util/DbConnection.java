package com.infosys.inventory.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private final static String connectionUrl = "jdbc:mysql://localhost:3306/inventorydb";
    private final static String userName = "root";
    private final static String password = "root";

    public static Connection getConnect() throws SQLException {
        return DriverManager.getConnection(connectionUrl,userName,password);
    }
}
