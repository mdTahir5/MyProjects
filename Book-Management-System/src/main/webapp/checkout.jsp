<%@ page import="com.DAO.CartDAOImpl" %>
<%@ page import="com.DAO.BookDAOImpl" %>
<%@ page import="com.DB.DBConnect" %>
<%@ page import="com.entity.User" %>
<%@ page import="com.user.servlet.CartServlet" %>
<%@ page import="com.entity.Cart" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>



<html>
<head>
<title>My Cart</title>
<%@include file="components/allcss.jsp" %>
</head>
<body style="background-color: #f0f1f2;">
<%@include file="components/navbar.jsp" %>

<%
User u = (User)session.getAttribute("userObj");
%>

<%
if(u == null) {
response.sendRedirect("login.jsp");
%>
<%
}
%>

<%
String successMsg = (String) session.getAttribute("successMsg");
String failedMsg = (String) session.getAttribute("failedMsg");
if(successMsg != null) {
%>
<div class="alert alert-success text-center" role="alert"> <%=successMsg%> </div>
<%session.removeAttribute("successMsg");%>
<%
} if(failedMsg != null) {
%>
<div class="alert alert-danger text-center" role="alert"> <%=failedMsg%> </div>
<%session.removeAttribute("failedMsg");%>
<%
}
%>

<div class="container">
    <div class="row p-3">
        <div class="col-md-6">
            <div class="card bg-white">
                <div class="card-body">
                <h3 class="text-center text-success"> Your Selected Items </h3>
                <table class="table table-striped">
                          <thead class="table-primary">
                            <tr>
                              <th scope="col">Book Name</th>
                              <th scope="col">Author</th>
                              <th scope="col">Price</th>
                              <th scope="col">Action</th>
                            </tr>
                          </thead>
                          <tbody>

                          <%

                          CartDAOImpl dao = new CartDAOImpl(DBConnect.getConn());
                          List<Cart> cart = dao.getBookByUser(u.getId());
                          double totalPrice = 0.00;
                          for(Cart c : cart) {
                          totalPrice = c.getTotalPrice();
                          %>
                            <tr>
                              <th scope="row"><%=c.getBookName()%></th>
                              <td><%=c.getAuthor()%></td>
                              <td> <i class="fa-solid fa-indian-rupee-sign"></i> <%=c.getPrice()%></td>
                              <td>
                              <a href="remove_book?bid=<%=c.getBookId()%>&&uid=<%=c.getUserId()%>&&cid=<%=c.getCartId()%>"
                              class="btn btn-sm btn-danger"> <i class="fa-solid fa-trash-can"></i> Remove</a>
                              </td>
                            </tr>
                            <%
                            }
                            %>
                            <tr>
                            <td> Total Price </td>
                            <td></td>
                            <td></td>
                            <td> <i class="fa-solid fa-indian-rupee-sign"></i> <%=totalPrice%> </td>
                            </tr>

                          </tbody>
                        </table>

                </div>
            </div>
        </div>


        <div class="col-md-6">
            <div class="card">
                <div class="card-body">
                <h3 class="text-center text-success"> Your Details for Orders </h3>
                <form action="order" method="post" class="row g-3">
                    <input type="hidden" name="id" value="<%=u.getId()%>" >

                  <div class="col-md-6">
                  <label for="inputEmail4" class="form-label">Name</label>
                  <input type="text" name="username" value="<%=u.getName()%>" required class="form-control" id="inputEmail4">
                  </div>
                  <div class="col-md-6">
                  <label for="inputPassword4" class="form-label">Email</label>
                  <input type="email" name="email" value="<%=u.getEmail()%>" required class="form-control" id="inputPassword4">
                  </div>

                  <div class="col-md-6">
                  <label for="inputEmail4" class="form-label">Phone Number</label>
                  <input type="number" name="phno" value="<%=u.getPhno()%>" required class="form-control" id="inputEmail4">
                  </div>
                  <div class="col-md-6">
                  <label for="inputPassword4" class="form-label">Address</label>
                  <input type="text" name="address" class="form-control" required id="inputPassword4">
                  </div>

                  <div class="col-md-6">
                  <label for="inputEmail4" class="form-label">Landmark</label>
                  <input type="text" name="landmark" class="form-control" required id="inputEmail4">
                  </div>
                  <div class="col-md-6">
                  <label for="inputPassword4" class="form-label">City</label>
                  <input type="text" name="city" class="form-control" required id="inputPassword4">
                  </div>

                  <div class="col-md-6">
                  <label for="inputEmail4" class="form-label">State</label>
                  <input type="text" name="state" class="form-control" required id="inputEmail4">
                  </div>
                  <div class="col-md-6">
                  <label for="inputPassword4" class="form-label">Pincode</label>
                  <input type="text" name="pincode" class="form-control" required id="inputPassword4">
                  </div>

                  <div class="form-group">
                  <label>Payment Mode</label>
                  <select name="payment" class="form-control">
                  <option value="noselect"> Select </option>
                  <option value="COD"> Cash On Delivery </option>
                  </select>
                  </div>

                  <div class="text-center">
                  <button class="btn btn-success m-2"> <i class="fa-regular fa-circle-check"></i> Order Now </button>
                  <a href="index.jsp" class="btn btn-info text-white"> <i class="fa-solid fa-cart-shopping"></i> Continue Shopping </a>
                  </div>

                </form>


                </div>
            </div>
        </div>




    </div>
</div>




</body>
</html>