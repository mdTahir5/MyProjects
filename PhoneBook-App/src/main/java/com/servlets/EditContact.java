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

@WebServlet("/update")
public class EditContact extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Running update ......");

        int cid = Integer.parseInt(req.getParameter("cid"));
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String phoneNo = req.getParameter("phoneNo");
        String about = req.getParameter("about");

        Contact c = new Contact();
        c.setId(cid);
        c.setName(name);
        c.setEmail(email);
        c.setPhoneNo(phoneNo);
        c.setAbout(about);

        ContactDAO dao = new ContactDAO(DBconnect.getConn());
        HttpSession session = req.getSession(); // To show message whether contact is update or not.
        boolean f = dao.updateContact(c);
        if(f){
            session.setAttribute("successMsg" , "Your Contact Updated Successfully...");
            resp.sendRedirect("viewcontact.jsp");
        }
        else{
            session.setAttribute("failedMsg" , "Something Wrong on Server...");
            resp.sendRedirect("editcontact.jsp?cid="+cid);
        }

    }
}
