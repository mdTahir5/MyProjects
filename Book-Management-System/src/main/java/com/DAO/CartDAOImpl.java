package com.DAO;

import com.entity.BookDetails;
import com.entity.Cart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CartDAOImpl implements CartDAO{

    private Connection conn;

    public CartDAOImpl(Connection conn){
        this.conn = conn;
    }


    @Override
    public boolean addCart(Cart c) {
        boolean f = false;
        try{
            String sql = "INSERT INTO cart (bookId, userId, bookName, author, price, totalPrice) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,c.getBookId());
            ps.setInt(2,c.getUserId());
            ps.setString(3,c.getBookName());
            ps.setString(4,c.getAuthor());
            ps.setDouble(5,c.getPrice());
            ps.setDouble(6,c.getTotalPrice());

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
    public List<Cart> getBookByUser(int userId) {
        List<Cart> list = new ArrayList<Cart>();
        Cart c = null;
        double totalPrice = 0;

        try{
            String sql = "SELECT * FROM cart WHERE userId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,userId);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                c = new Cart();
                c.setCartId(rs.getInt(1));
                c.setBookId(rs.getInt(2));
                c.setUserId(rs.getInt(3));
                c.setBookName(rs.getString(4));
                c.setAuthor(rs.getString(5));
                c.setPrice(rs.getDouble(6));

                totalPrice += rs.getDouble(7);
                c.setTotalPrice(totalPrice);
                list.add(c);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }


    @Override
    public boolean deleteBook(int bid , int uid , int cid) {
        boolean f = false;
        try {
            String sql = "DELETE FROM cart WHERE bookId=? AND userId=? AND cartId=? LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,bid);
            ps.setInt(2,uid);
            ps.setInt(3,cid);

            int i = ps.executeUpdate();
            if(i==1){
                f = true;
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return f;
    }







}
