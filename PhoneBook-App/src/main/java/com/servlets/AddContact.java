package com.servlets;

import com.connection.DBconnect;
import com.dao.ContactDAO;
import com.entity.Contact;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
@WebServlet("/AddContact")
public class AddContact extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int userId = Integer.parseInt(req.getParameter("userId"));
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String phoneNo = req.getParameter("phoneNo");
        String about = req.getParameter("about");

        Contact c = new Contact(name,email,phoneNo,about,userId);

        ContactDAO dao = new ContactDAO(DBconnect.getConn());

        HttpSession session = req.getSession(); // To show message whether contact is saved or not.

        boolean f = dao.saveContact(c);
        if(f){
            session.setAttribute("successMsg" , "Your Contact is Saved Successfully...");
            resp.sendRedirect("addcontact.jsp");
        }
        else{
            session.setAttribute("failedMsg" , "Something Wrong on Server...");
            resp.sendRedirect("addcontact.jsp");
        }




    }
}
