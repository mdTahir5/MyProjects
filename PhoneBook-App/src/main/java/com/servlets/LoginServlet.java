package com.servlets;

import com.connection.DBconnect;
import com.dao.UserDAO;
import com.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        UserDAO dao = new UserDAO(DBconnect.getConn());
        User u = dao.userLogin(email,password);
        HttpSession session = req.getSession();

        if(u != null){
            session.setAttribute("user",u);
            resp.sendRedirect("navbar.jsp");
            //System.out.println("User is available  " + u);

        } else{
            session.setAttribute("invalidMsg", "Invalid Email & Password");
            resp.sendRedirect("login.jsp");
            //System.out.println("Not available  " + u);
        }

    }
}
