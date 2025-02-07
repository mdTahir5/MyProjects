package com.dao;

import com.entity.Contact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ContactDAO {
    private Connection conn;

    public ContactDAO(Connection conn) {
        this.conn = conn;
    }

    // This Method is Used to SAVE or INSERT Contact into Database.
    public boolean saveContact(Contact c){
        boolean f = false;

        try{
            String insertContact = "INSERT INTO contact(name,email,phoneNo,about,userId) VALUES(?,?,?,?,?)" ;
            PreparedStatement ps = conn.prepareStatement(insertContact);

            ps.setString(1,c.getName());
            ps.setString(2,c.getEmail());
            ps.setString(3,c.getPhoneNo());
            ps.setString(4,c.getAbout());
            ps.setInt(5,c.getUserId());

            int i = ps.executeUpdate();
            if(i == 1){
                f = true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return f;
    }

    // This Method is Used to DISPLAY or SHOW Contact in Browser From Database.
    public List<Contact> getAllContact(int uId){

        List<Contact> contactList = new ArrayList<Contact>();
        Contact c = null;

        try{
            String showContact = "SELECT * FROM contact WHERE userId=?";
            PreparedStatement ps = conn.prepareStatement(showContact);
            ps.setInt(1,uId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                c = new Contact();
                c.setId(rs.getInt(1));
                c.setName(rs.getString(2));
                c.setEmail(rs.getString(3));
                c.setPhoneNo(rs.getString(4));
                c.setAbout(rs.getString(5));
                contactList.add(c);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return contactList;
    }

    // This Method is Used to EDIT Particular Contact in Database.
    public Contact getContactByID(int cid){
        Contact c = new Contact();

        try{
            String editcontact = "SELECT * FROM contact WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(editcontact);
            ps.setInt(1,cid);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                c.setId(rs.getInt(1));
                c.setName(rs.getString(2));
                c.setEmail(rs.getString(3));
                c.setPhoneNo(rs.getString(4));
                c.setAbout(rs.getString(5));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return c;
    }

    // This Method is Used to UPDATE Particular Contact in Database.
    public boolean updateContact(Contact c){

        boolean f = false;

        try{
            String updateQuery = "UPDATE contact SET name=?,email=?,phoneNo=?,about=? WHERE id=?" ;
            PreparedStatement ps = conn.prepareStatement(updateQuery);

            ps.setString(1,c.getName());
            ps.setString(2,c.getEmail());
            ps.setString(3,c.getPhoneNo());
            ps.setString(4,c.getAbout());
            ps.setInt(5,c.getId());

            int i = ps.executeUpdate();
            if(i == 1){
                f = true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return f;
    }

    // This Method is Used to DELETE Particular Contact in Database.
    public boolean deleteContactByID(int id){
        boolean f = false;

        try{

            String deleteQuery = "DELETE FROM contact WHERE id=?" ;
            PreparedStatement ps = conn.prepareStatement(deleteQuery);
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
}
