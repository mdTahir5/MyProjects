<%@ page import="com.DAO.BookOrderDAOImpl" %>
<%@ page import="com.DB.DBConnect" %>
<%@ page import="com.entity.User" %>
<%@ page import="com.entity.BookOrder" %>
<%@ page import="com.user.servlet.LoginServlet" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<html>
<head>
<title>My Order</title>
<%@include file="components/allcss.jsp" %>
</head>
<body style="background-color: #f0f1f2;">
<%@include file="components/navbar.jsp" %>

<div class="container p-4">
<h3 class="text-center text-primary"> Your Order </h3>

<table class="table table-striped mt-4">
  <thead class="table-success">
    <tr>
      <th scope="col">Order Id</th>
      <th scope="col">Name</th>
      <th scope="col">Book Name</th>
      <th scope="col">Author</th>
      <th scope="col">Price</th>
      <th scope="col">Payment Type</th>
    </tr>
  </thead>
  <tbody>

  <%
  User u = (User)session.getAttribute("userObj");
  BookOrderDAOImpl dao = new BookOrderDAOImpl(DBConnect.getConn());
  List<BookOrder> blist = dao.getBook(u.getEmail());
  for(BookOrder b : blist) {
  %>
   <tr>
      <th scope="row"> <%=b.getOrderId()%> </th>
      <td> <%=b.getUserName()%> </td>
      <td> <%=b.getBookName()%> </td>
      <td> <%=b.getAuthor()%> </td>
      <td> <%=b.getPrice()%> </td>
      <td> <%=b.getPaymentType()%> </td>
   </tr>
   <%
   }
   %>

  </tbody>
</table>



</div>

</body>
</html>
