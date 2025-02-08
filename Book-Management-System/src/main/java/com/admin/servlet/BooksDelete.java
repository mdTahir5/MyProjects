package com.admin.servlet;

import com.DAO.BookDAOImpl;
import com.DB.DBConnect;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/delete")
public class BooksDelete extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            int id = Integer.parseInt(req.getParameter("id"));
            BookDAOImpl dao = new BookDAOImpl(DBConnect.getConn());
            boolean f = dao.deleteBooks(id);

            HttpSession session = req.getSession();
            if(f){
                session.setAttribute("successMsg" , "Book Deleted Successfully...");
                resp.sendRedirect("admin/all_book.jsp");
            } else{
                session.setAttribute("failedMsg" , "Something Wrong on Server...");
                resp.sendRedirect("admin/all_book.jsp");
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
