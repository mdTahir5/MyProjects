package com.DAO;

import com.entity.BookOrder;

import java.awt.print.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BookOrderDAOImpl implements BookOrderDAO{

    private Connection conn;

    public BookOrderDAOImpl(Connection conn){
        super();
        this.conn = conn;
    }

    @Override
    public boolean saveOrder(List<BookOrder> blist) {
        boolean f = false;
        try{
            String sql = "INSERT INTO bookorders(order_id, user_name, email, address, phone, book_name, author, price, payment) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(sql);

            for(BookOrder b : blist) {
                ps.setString(1, b.getOrderId());
                ps.setString(2, b.getUserName());
                ps.setString(3, b.getEmail());
                ps.setString(4, b.getFullAddress());
                ps.setString(5, b.getPhno());
                ps.setString(6, b.getBookName());
                ps.setString(7, b.getAuthor());
                ps.setString(8, b.getPrice());
                ps.setString(9, b.getPaymentType());
                ps.addBatch();
            }
            int [] count = ps.executeBatch();
            conn.commit();
            f = true;
            conn.setAutoCommit(true);

        } catch (Exception e){
            e.printStackTrace();
        }
        return f;
    }

    @Override
    public List<BookOrder> getBook(String email) {
        List<BookOrder> list = new ArrayList<BookOrder>();
        BookOrder o = null;

        try{
            String sql = "SELECT * FROM bookorders WHERE email=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,email);

            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                o = new BookOrder();
                o.setId(rs.getInt(1));
                o.setOrderId(rs.getString(2));
                o.setUserName(rs.getString(3));
                o.setEmail(rs.getString(4));
                o.setFullAddress(rs.getString(5));
                o.setPhno(rs.getString(6));
                o.setBookName(rs.getString(7));
                o.setAuthor(rs.getString(8));
                o.setPrice(rs.getString(9));
                o.setPaymentType(rs.getString(10));
                list.add(o);

            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<BookOrder> getAllOrders() {
        List<BookOrder> list = new ArrayList<BookOrder>();
        BookOrder o = null;

        try{
            String sql = "SELECT * FROM bookorders";
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                o = new BookOrder();
                o.setId(rs.getInt(1));
                o.setOrderId(rs.getString(2));
                o.setUserName(rs.getString(3));
                o.setEmail(rs.getString(4));
                o.setFullAddress(rs.getString(5));
                o.setPhno(rs.getString(6));
                o.setBookName(rs.getString(7));
                o.setAuthor(rs.getString(8));
                o.setPrice(rs.getString(9));
                o.setPaymentType(rs.getString(10));
                list.add(o);

            }
        } catch (Exception e){
            e.printStackTrace();
        }

    return list;
    }



}
