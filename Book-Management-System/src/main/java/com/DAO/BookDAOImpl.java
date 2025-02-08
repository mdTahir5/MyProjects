package com.DAO;

import com.entity.BookDetails;
import jakarta.servlet.http.Cookie;

import java.awt.print.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BookDAOImpl implements BookDAO{

    private Connection conn;

    public BookDAOImpl(Connection conn){
        super();
        this.conn = conn;
    }

    @Override
    public boolean addBooks(BookDetails b) {
        boolean f = false;
        try{
            String bookDetailsInsert = "INSERT INTO bookdetails (bookname,author,price,bookCategory,status,photo,user_email) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(bookDetailsInsert);
            ps.setString(1,b.getBookname());
            ps.setString(2,b.getAuthor());
            ps.setDouble(3,b.getPrice());
            ps.setString(4,b.getBookCategory());
            ps.setString(5,b.getStatus());
            ps.setString(6,b.getPhotoName());
            ps.setString(7,b.getEmail());

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
    public List<BookDetails> getAllBooks() {

        List<BookDetails> list = new ArrayList<BookDetails>();
        BookDetails b = null;
        try{
            String sqlQuery = "SELECT * FROM bookdetails";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                b = new BookDetails();
                b.setBookId(rs.getInt(1));
                b.setBookname(rs.getString(2));
                b.setAuthor(rs.getString(3));
                b.setPrice(rs.getDouble(4));
                b.setBookCategory(rs.getString(5));
                b.setStatus(rs.getString(6));
                b.setPhotoName(rs.getString(7));
                b.setEmail(rs.getString(8));
                list.add(b);

            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public BookDetails getBookById(int id) {
        BookDetails b = null;

        try{
            String sql = "SELECT * FROM bookdetails WHERE bookId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,id);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                b = new BookDetails();
                b.setBookId(rs.getInt(1));
                b.setBookname(rs.getString(2));
                b.setAuthor(rs.getString(3));
                b.setPrice(rs.getDouble(4));
                b.setBookCategory(rs.getString(5));
                b.setStatus(rs.getString(6));
                b.setPhotoName(rs.getString(7));
                b.setEmail(rs.getString(8));

            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return b;
    }

    @Override
    public boolean updateEditBooks(BookDetails b) {
        boolean f = false;
        try{
            String sql = "UPDATE bookdetails SET bookname=?,author=?,price=?,status=? WHERE bookId=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1,b.getBookname());
            ps.setString(2,b.getAuthor());
            ps.setDouble(3,b.getPrice());
            ps.setString(4,b.getStatus());
            ps.setInt(5,b.getBookId());
            int i = ps.executeUpdate();
            if(i==1){
                f = true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return f;
    }

    @Override
    public boolean deleteBooks(int id) {
        boolean f = false;
        try{
            String sql = "DELETE FROM bookdetails WHERE bookId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,id);
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
    public List<BookDetails> getNewBooks() {
        List<BookDetails> list = new ArrayList<BookDetails>();
        BookDetails b = null;
        try{
            String sql = "SELECT * FROM bookdetails WHERE bookCategory=? AND status=? ORDER BY bookId DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "New");
            ps.setString(2, "Active");
            ResultSet rs = ps.executeQuery();
            int i = 1;
            while(rs.next() && i<=4){
                b = new BookDetails();
                b.setBookId(rs.getInt(1));
                b.setBookname(rs.getString(2));
                b.setAuthor(rs.getString(3));
                b.setPrice(rs.getDouble(4));
                b.setBookCategory(rs.getString(5));
                b.setStatus(rs.getString(6));
                b.setPhotoName(rs.getString(7));
                b.setEmail(rs.getString(8));
                list.add(b);
                i++;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<BookDetails> getRecentBooks() {
        List<BookDetails> list = new ArrayList<BookDetails>();
        BookDetails b = null;
        try{
            String sql = "SELECT * FROM bookdetails WHERE status=? ORDER BY bookId DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "Active");
            ResultSet rs = ps.executeQuery();
            int i = 1;
            while(rs.next() && i<=4){
                b = new BookDetails();
                b.setBookId(rs.getInt(1));
                b.setBookname(rs.getString(2));
                b.setAuthor(rs.getString(3));
                b.setPrice(rs.getDouble(4));
                b.setBookCategory(rs.getString(5));
                b.setStatus(rs.getString(6));
                b.setPhotoName(rs.getString(7));
                b.setEmail(rs.getString(8));
                list.add(b);
                i++;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<BookDetails> getOldBooks() {
        List<BookDetails> list = new ArrayList<BookDetails>();
        BookDetails b = null;
        try{
            String sql = "SELECT * FROM bookdetails WHERE bookCategory=? AND status=? ORDER BY bookId DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "Old");
            ps.setString(2, "Active");
            ResultSet rs = ps.executeQuery();
            int i = 1;
            while(rs.next() && i<=4){
                b = new BookDetails();
                b.setBookId(rs.getInt(1));
                b.setBookname(rs.getString(2));
                b.setAuthor(rs.getString(3));
                b.setPrice(rs.getDouble(4));
                b.setBookCategory(rs.getString(5));
                b.setStatus(rs.getString(6));
                b.setPhotoName(rs.getString(7));
                b.setEmail(rs.getString(8));
                list.add(b);
                i++;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<BookDetails> getAllRecentBooks() {
        List<BookDetails> list = new ArrayList<BookDetails>();
        BookDetails b = null;
        try{
            String sql = "SELECT * FROM bookdetails WHERE status=? ORDER BY bookId DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "Active");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                b = new BookDetails();
                b.setBookId(rs.getInt(1));
                b.setBookname(rs.getString(2));
                b.setAuthor(rs.getString(3));
                b.setPrice(rs.getDouble(4));
                b.setBookCategory(rs.getString(5));
                b.setStatus(rs.getString(6));
                b.setPhotoName(rs.getString(7));
                b.setEmail(rs.getString(8));
                list.add(b);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<BookDetails> getAllNewBooks() {
        List<BookDetails> list = new ArrayList<BookDetails>();
        BookDetails b = null;
        try{
            String sql = "SELECT * FROM bookdetails WHERE bookCategory=? AND status=? ORDER BY bookId DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "New");
            ps.setString(2, "Active");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                b = new BookDetails();
                b.setBookId(rs.getInt(1));
                b.setBookname(rs.getString(2));
                b.setAuthor(rs.getString(3));
                b.setPrice(rs.getDouble(4));
                b.setBookCategory(rs.getString(5));
                b.setStatus(rs.getString(6));
                b.setPhotoName(rs.getString(7));
                b.setEmail(rs.getString(8));
                list.add(b);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<BookDetails> getAllOldBooks() {
        List<BookDetails> list = new ArrayList<BookDetails>();
        BookDetails b = null;
        try{
            String sql = "SELECT * FROM bookdetails WHERE bookCategory=? AND status=? ORDER BY bookId DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "Old");
            ps.setString(2, "Active");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                b = new BookDetails();
                b.setBookId(rs.getInt(1));
                b.setBookname(rs.getString(2));
                b.setAuthor(rs.getString(3));
                b.setPrice(rs.getDouble(4));
                b.setBookCategory(rs.getString(5));
                b.setStatus(rs.getString(6));
                b.setPhotoName(rs.getString(7));
                b.setEmail(rs.getString(8));
                list.add(b);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<BookDetails> getBookByOld(String email, String category) {
        List<BookDetails> list = new ArrayList<BookDetails>();
        BookDetails b = null;
        try{
            String sql = "SELECT * FROM bookdetails WHERE bookCategory=? AND user_email=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,category);
            ps.setString(2,email);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                b = new BookDetails();
                b.setBookId(rs.getInt(1));
                b.setBookname(rs.getString(2));
                b.setAuthor(rs.getString(3));
                b.setPrice(rs.getDouble(4));
                b.setBookCategory(rs.getString(5));
                b.setStatus(rs.getString(6));
                b.setPhotoName(rs.getString(7));
                b.setEmail(rs.getString(8));
                list.add(b);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean oldBookDelete(String email, String category, int id) {
        boolean f = false;
        try{
            String sql = "DELETE FROM bookdetails WHERE bookCategory=? AND user_email=? AND bookId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,category);
            ps.setString(2,email);
            ps.setInt(3,id);

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
    public List<BookDetails> getBookBySearch(String ch) {
        List<BookDetails> list = new ArrayList<BookDetails>();
        BookDetails b = null;
        try{
            String sql = "SELECT * FROM bookdetails WHERE bookname LIKE ? OR author LIKE ? OR bookCategory LIKE ? AND status=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,"%"+ch+"%");
            ps.setString(2,"%"+ch+"%");
            ps.setString(3,"%"+ch+"%");
            ps.setString(4,"Active");

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                b = new BookDetails();
                b.setBookId(rs.getInt(1));
                b.setBookname(rs.getString(2));
                b.setAuthor(rs.getString(3));
                b.setPrice(rs.getDouble(4));
                b.setBookCategory(rs.getString(5));
                b.setStatus(rs.getString(6));
                b.setPhotoName(rs.getString(7));
                b.setEmail(rs.getString(8));
                list.add(b);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }



}
