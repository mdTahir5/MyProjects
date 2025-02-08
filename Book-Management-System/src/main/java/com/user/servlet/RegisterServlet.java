package com.user.servlet;

import com.DAO.UserDAOImpl;
import com.DB.DBConnect;
import com.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try{
            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String phno = req.getParameter("phno");
            String password = req.getParameter("password");
            String check = req.getParameter("check");

            System.out.println(name+" "+email+" "+phno+" "+password+" "+check);

            User us = new User();
            us.setName(name);
            us.setEmail(email);
            us.setPhno(phno);
            us.setPassword(password);

            HttpSession session = req.getSession();

            if(check != null){

                UserDAOImpl dao = new UserDAOImpl(DBConnect.getConn());

                boolean f2 = dao.checkUser(email);
                if(f2){

                    boolean f = dao.userRegister(us);
                    if(f){
                        session.setAttribute("successMsg" , "Registration Successfully...");
                        resp.sendRedirect("register.jsp");
                    } else{
                        session.setAttribute("failedMsg" , "Something Wrong on Server...");
                        resp.sendRedirect("register.jsp");
                    }

                } else {
                    session.setAttribute("failedMsg" , "User Already Exist Try Another Email Address");
                    resp.sendRedirect("register.jsp");
                }

            }
            else{
//
                session.setAttribute("failedMsg" , "Please Check Terms & Conditions");
                resp.sendRedirect("register.jsp");
            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
