package com.dao;

import com.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
    private Connection conn;
    public UserDAO(Connection conn){
        super();
        this.conn = conn;
    }
    // Method() to store user registration details in sql table.
    public boolean userRegister(User u){
        boolean f = false;
        try{
            String insertQuery = "INSERT INTO user (name, email, password) VALUES(?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertQuery);
            ps.setString(1,u.getUsername());
            ps.setString(2,u.getEmail());
            ps.setString(3,u.getPassword());

            int i = ps.executeUpdate();
            if(i == 1){
                f = true;
            }

        } catch (Exception e1){
            e1.printStackTrace();
        }
        return f;
    }

    // Method() for Login
    public User userLogin(String e, String p){
        User user = null;
        try{
            String selectQuery = "SELECT * FROM user WHERE email = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(selectQuery);
            ps.setString(1,e);
            ps.setString(2,p);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                user = new User();
                user.setId(rs.getInt(1));
                user.setUsername(rs.getString(2));
                user.setEmail(rs.getString(3));
                user.setPassword(rs.getString(4));
            }
        } catch (Exception e2){
            e2.printStackTrace();
        }
        return user;
    }


}
