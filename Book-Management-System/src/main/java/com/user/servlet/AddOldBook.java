package com.user.servlet;

import com.DAO.BookDAOImpl;
import com.DB.DBConnect;
import com.entity.BookDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;

@WebServlet("/add_old_book")
@MultipartConfig
public class AddOldBook extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try{
            String bookname = req.getParameter("bookname");
            String author = req.getParameter("author");
            Double price = Double.parseDouble(req.getParameter("price"));
            String categories = "Old";
            String status = "Active";
            Part part = req.getPart("bookimg");
            String filename = part.getSubmittedFileName();

            String useremail = req.getParameter("user");

            BookDetails b = new BookDetails(bookname, author, price, categories, status, filename, useremail);

            BookDAOImpl dao = new BookDAOImpl(DBConnect.getConn());

            String path = getServletContext().getRealPath("")+"books";
            File file = new File(path);
            part.write(path + File.separator + filename);


            boolean f = dao.addBooks(b);

            HttpSession session = req.getSession();

            if(f){
                session.setAttribute("successMsg" , "Book Added Successfully");
                resp.sendRedirect("sell_book.jsp");
            } else{
                session.setAttribute("failedMsg" , "Something error on server");
                resp.sendRedirect("sell_book.jsp");
            }


        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
