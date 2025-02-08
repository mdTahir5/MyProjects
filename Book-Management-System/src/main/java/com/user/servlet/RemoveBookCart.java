package com.user.servlet;

import com.DAO.CartDAOImpl;
import com.DB.DBConnect;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebServlet("/remove_book")
public class RemoveBookCart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int bid = Integer.parseInt(req.getParameter("bid"));
        int uid = Integer.parseInt(req.getParameter("uid"));
        int cid = Integer.parseInt(req.getParameter("cid"));

        CartDAOImpl dao = new CartDAOImpl(DBConnect.getConn());
        boolean f = dao.deleteBook(bid , uid , cid);
        HttpSession session = req.getSession();

        if(f){
            session.setAttribute("successMsg" , "Book Removed from Cart");
            resp.sendRedirect("checkout.jsp");

        } else {
            session.setAttribute("failedMsg" , "Something Wrong on Server");
            resp.sendRedirect("checkout.jsp");
        }

    }
}
