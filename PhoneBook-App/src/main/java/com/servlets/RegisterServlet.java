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
@WebServlet("/Register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Fine");
        String RegUserName = req.getParameter("RegUserName");
        String RegEmail = req.getParameter("RegEmail");
        String RegPassword = req.getParameter("RegPassword");

        HttpSession ss = req.getSession();
        if(RegUserName.isEmpty() || (RegEmail.isEmpty() || RegPassword.isEmpty())) {
            ss.setAttribute("fieldMsg" , "please filled completely");
            resp.sendRedirect("register.jsp");
        }
        else {

            User u = new User(RegUserName, RegEmail, RegPassword);

            UserDAO dao = new UserDAO(DBconnect.getConn());

            boolean f = dao.userRegister(u);
            HttpSession session = req.getSession();
            if (f) {
                //System.out.println("User Register Successfully");
                session.setAttribute("successMsg", "User Register Successfully...");
                resp.sendRedirect("register.jsp");
            } else {
                //System.out.println("something wrong");
                session.setAttribute("errorMsg", "Something Wrong on Server...");
                resp.sendRedirect("register.jsp");
            }

        }


    }
}
