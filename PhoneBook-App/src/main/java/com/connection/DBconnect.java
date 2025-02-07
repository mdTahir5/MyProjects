package com.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBconnect {

    private static String url = "jdbc:mysql://localhost:3306/phonebook";
    private static String username = "root";
    private static String password = "password";
    private static Connection conn;

    // This Method() call for the Connection with SQL database and return Connection.
    public static Connection getConn(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try {
                conn = DriverManager.getConnection(url, username, password);

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}