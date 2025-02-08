<%@ page import="com.entity.BookOrder" %>
<%@ page import="java.util.List" %>
<%@ page import="com.DB.DBConnect" %>
<%@ page import="com.DAO.BookOrderDAOImpl" %>
<%@ page import="com.user.servlet.LoginServlet" %>

<html>
<head>
<%@ include file="allcss.jsp" %>

</head>
<body>
<%
    if (session.getAttribute("userObj") == null) {
        response.sendRedirect("../login.jsp");
    }
%>

<%@ include file="navbar.jsp" %>
<h2 class="text-center text-primary mt-3"> All Orders </h2>

<table class="table table-striped">
    <thead class="table-info">
    <tr>
        <th scope="col">Order Id</th>
        <th scope="col">Name</th>
        <th scope="col">Email</th>
        <th scope="col">Address</th>
        <th scope="col">Phone No</th>
        <th scope="col">Book Name</th>
        <th scope="col">Author</th>
        <th scope="col">Price</th>
        <th scope="col">Payment Mode</th>
    </tr>
    </thead>
    <tbody>
    <%
    BookOrderDAOImpl dao = new BookOrderDAOImpl(DBConnect.getConn());
    List<BookOrder> blist = dao.getAllOrders();
    for(BookOrder b : blist) {
    %>
    <tr>
        <th scope="row"> <%=b.getOrderId()%> </th>
        <td> <%=b.getUserName()%> </td>
        <td> <%=b.getEmail()%> </td>
        <td> <%=b.getFullAddress()%> </td>
        <td> <%=b.getPhno()%> </td>
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


</body>
</html>