package com.DB;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {
    static String url = "jdbc:mysql://localhost:3306/ebook-app";
    static String username = "root";
    static String password = "password";

    private static Connection conn;
    public static Connection getConn(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url,username,password);
        } catch (Exception e){
            e.printStackTrace();
        }

        return conn;
    }
}
