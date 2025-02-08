package com.user.servlet;

import com.DAO.BookOrderDAOImpl;
import com.DAO.CartDAOImpl;
import com.DB.DBConnect;
import com.entity.BookDetails;
import com.entity.BookOrder;
import com.entity.Cart;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.awt.print.Book;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            HttpSession session = req.getSession();

            int id = Integer.parseInt(req.getParameter("id"));
            String name = req.getParameter("username");
            String email = req.getParameter("email");
            String phno = req.getParameter("phno");
            String address = req.getParameter("address");
            String landmark = req.getParameter("landmark");
            String city = req.getParameter("city");
            String state = req.getParameter("city");
            String pincode = req.getParameter("pincode");
            String paymentType = req.getParameter("payment");

            String fulladdress = address+", "+landmark+", "+city+", "+state+", "+pincode;

            CartDAOImpl dao = new CartDAOImpl(DBConnect.getConn());

            List<Cart> blist = dao.getBookByUser(id);

            if(blist.isEmpty()) {
                resp.sendRedirect("checkout.jsp");
                session.setAttribute("failedMsg" , "Please Add Items in Cart");

            } else {
                BookOrderDAOImpl dao2 = new BookOrderDAOImpl(DBConnect.getConn());
                BookOrder o = null;
                ArrayList<BookOrder> orderList = new ArrayList<BookOrder>();
                Random r = new Random();
                for(Cart c : blist){
                    o = new BookOrder();
                    o.setOrderId("BOOK-ORD.00" + r.nextInt(1000));
                    o.setUserName(name);
                    o.setEmail(email);
                    o.setPhno(phno);
                    o.setFullAddress(fulladdress);
                    o.setBookName(c.getBookName());
                    o.setAuthor(c.getAuthor());
                    o.setPrice(c.getPrice()+"");
                    o.setPaymentType(paymentType);
                    orderList.add(o);
                }
                if("noselect".equalsIgnoreCase(paymentType)) {
                    session.setAttribute("failedMsg" , "Choose Payment Method");
                    resp.sendRedirect("checkout.jsp");
                } else {
                    boolean f = dao2.saveOrder(orderList);

                    if(f){
                        resp.sendRedirect("order_success.jsp");

                    } else {
                        session.setAttribute("failedMsg" , "Your Order Failed");
                        resp.sendRedirect("checkout.jsp");
                    }
                }

            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
