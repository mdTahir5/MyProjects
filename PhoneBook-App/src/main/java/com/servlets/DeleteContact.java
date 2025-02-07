package com.servlets;

import com.connection.DBconnect;
import com.dao.ContactDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/delete")
public class DeleteContact extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int cid = Integer.parseInt(req.getParameter("cid"));

        ContactDAO dao = new ContactDAO(DBconnect.getConn());

        boolean f = dao.deleteContactByID(cid);
        HttpSession session = req.getSession();
        if(f){
            session.setAttribute("successMsg" , "Contact Deleted successfully");
            resp.sendRedirect("viewcontact.jsp");

        } else{
            session.setAttribute("failedMsg" , "Something wrong on server");
            resp.sendRedirect("viewcontact.jsp");
        }
    }
}
