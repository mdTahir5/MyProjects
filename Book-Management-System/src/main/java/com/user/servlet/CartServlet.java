package com.user.servlet;

import com.DAO.BookDAOImpl;
import com.DAO.CartDAOImpl;
import com.DB.DBConnect;
import com.entity.BookDetails;
import com.entity.Cart;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.beans.Introspector;
import java.io.IOException;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.concurrent.*;


@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try{
            int bookId = Integer.parseInt(req.getParameter("bookId"));
            int userId = Integer.parseInt(req.getParameter("userId"));

            BookDAOImpl dao = new BookDAOImpl(DBConnect.getConn());
            BookDetails b = dao.getBookById(bookId);

            Cart c = new Cart();
            c.setBookId(bookId);
            c.setUserId(userId);
            c.setBookName(b.getBookname());
            c.setAuthor(b.getAuthor());
            c.setPrice(b.getPrice());
            c.setTotalPrice(b.getPrice());

            CartDAOImpl dao2 = new CartDAOImpl(DBConnect.getConn());
            boolean f = dao2.addCart(c);
            HttpSession session = req.getSession();

            if(f){
                session.setAttribute("addCart" , "Book Added to Cart");

                ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                scheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        session.removeAttribute("addCart"); // Remove the session attribute after 3 seconds
                    }
                }, 3, TimeUnit.SECONDS); // 3-second delay
                resp.setContentType("text/html");

                resp.sendRedirect("all_new_books.jsp");

            } else{
                session.setAttribute("failed" , "Something Wrong on Server");
                resp.sendRedirect("all_new_books.jsp");
            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
