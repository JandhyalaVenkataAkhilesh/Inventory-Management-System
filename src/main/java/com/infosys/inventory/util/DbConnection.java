package com.infosys.inventory.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    public final static String connectionUrl = System.getenv("connectionUrl");
    public final static String userName = System.getenv("userName");
    public final static String password = System.getenv("password");

    public static Connection getConnect() throws SQLException {
        if(connectionUrl == null || userName == null || password == null)
            throw new RuntimeException("❌ Database environment variables not set");
        return DriverManager.getConnection(connectionUrl,userName,password);
    }
}