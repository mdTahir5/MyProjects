package com.DAO;

import com.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAOImpl implements UserDAO{
    public Connection conn;

    public UserDAOImpl(Connection conn){
        super();
        this.conn = conn;
    }
    @Override
    public boolean userRegister(User us) {
        boolean f = false;
        try{
            String insertRegister = "INSERT INTO user(name, email, phno, password) VALUES(?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(insertRegister);
            ps.setString(1,us.getName());
            ps.setString(2,us.getEmail());
            ps.setString(3,us.getPhno());
            ps.setString(4,us.getPassword());

            int i = ps.executeUpdate();
            if(i == 1){
                f = true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return f;
    }

    @Override
    public User login(String email, String password) {
        User us = null;

        try{
            String loginQuery = "SELECT * FROM user WHERE email=? AND password=?";
            PreparedStatement ps = conn.prepareStatement(loginQuery);
            ps.setString(1, email);
            ps.setString(2,password);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                us = new User();
                us.setId(rs.getInt(1));
                us.setName(rs.getString(2));
                us.setEmail(rs.getString(3));
                us.setPhno(rs.getString(4));
                us.setPassword(rs.getString(5));
                us.setAddress(rs.getString(6));
                us.setLandmark(rs.getString(7));
                us.setCity(rs.getString(8));
                us.setPincode(rs.getString(9));
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return us;
    }

    @Override
    public boolean checkPassword(int id, String psd) {
    boolean f = false;
    try{
        String sql = "SELECT * FROM user WHERE id=? AND password=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,id);
        ps.setString(2,psd);

        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            f = true;
        }
    } catch (Exception e){
        e.printStackTrace();
    }
    return f;
    }

    @Override
    public boolean updateProfile(User us) {
        boolean f = false;
        try{
            String insertRegister = "UPDATE user SET name=?, email=?, phno=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(insertRegister);
            ps.setString(1,us.getName());
            ps.setString(2,us.getEmail());
            ps.setString(3,us.getPhno());
            ps.setInt(4,us.getId());

            int i = ps.executeUpdate();
            if(i == 1){
                f = true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return f;
    }

    @Override
    public boolean checkUser(String email) {
        boolean f = true;
        try{
            String insertRegister = "SELECT * FROM user WHERE email=?";
            PreparedStatement ps = conn.prepareStatement(insertRegister);
            ps.setString(1,email);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                f = false;
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return  f;
    }


}
