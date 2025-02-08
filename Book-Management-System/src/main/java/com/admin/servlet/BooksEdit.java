package com.admin.servlet;

import com.DAO.BookDAOImpl;
import com.DB.DBConnect;
import com.entity.BookDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/edit_book")
public class BooksEdit extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String bookname = req.getParameter("bookname");
            String author = req.getParameter("author");
            Double price = Double.parseDouble(req.getParameter("price"));
            String status = req.getParameter("status");

            BookDetails b = new BookDetails();
            b.setBookId(id);
            b.setBookname(bookname);
            b.setAuthor(author);
            b.setPrice(price);
            b.setStatus(status);

            BookDAOImpl dao = new BookDAOImpl(DBConnect.getConn());
            boolean f = dao.updateEditBooks(b);

            HttpSession session = req.getSession();
            if(f){
                session.setAttribute("successMsg" , "Book Updated Successfully...");
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
