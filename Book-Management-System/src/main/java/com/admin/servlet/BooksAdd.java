package com.admin.servlet;

import com.DAO.BookDAOImpl;
import com.DB.DBConnect;
import com.entity.BookDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;

@WebServlet("/add_book")
@MultipartConfig
public class BooksAdd extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try{
            String bookname = req.getParameter("bookname");
            String author = req.getParameter("author");
            Double price = Double.parseDouble(req.getParameter("price"));
            String categories = req.getParameter("categories");
            String status = req.getParameter("status");
            Part part = req.getPart("bookimg");
            String filename = part.getSubmittedFileName();

            BookDetails b = new BookDetails(bookname, author, price, categories, status, filename,"admin");

            BookDAOImpl dao = new BookDAOImpl(DBConnect.getConn());

            String path = getServletContext().getRealPath("")+"books";
            File file = new File(path);
            part.write(path + File.separator + filename);


            boolean f = dao.addBooks(b);

            HttpSession session = req.getSession();

            if(f){
                session.setAttribute("successMsg" , "Book Added Successfully");
                resp.sendRedirect("admin/add_book.jsp");
            } else{
                session.setAttribute("failedMsg" , "Something error on server");
                resp.sendRedirect("admin/add_book.jsp");
            }


        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
