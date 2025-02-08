<%@ page import="com.entity.BookDetails" %>
<%@ page import="com.DAO.BookDAOImpl" %>
<%@ page import="com.DB.DBConnect" %>
<%@ page import="com.entity.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<html>
<head>
<%@include file="components/allcss.jsp" %>
</head>

<body style="background-color: #f0f1f2;">
<%@include file="components/navbar.jsp" %>

<%
int bid = Integer.parseInt(request.getParameter("bid"));
BookDAOImpl dao = new BookDAOImpl(DBConnect.getConn());
BookDetails b = dao.getBookById(bid);

%>

<div class="container p-3">
    <div class="row">
        <div class="col-md-6 text-center p-5 border bg-white">
        <img src="books/<%=b.getPhotoName()%>" style="height: 220px; width: 200px"><br>
        <h4 style="font-family: Times New Roman, Times, serif;" class="mt-3"> Book Name : <span class="text-primary"> <%=b.getBookname()%> </span> </h4>
        <h4 style="font-family: Times New Roman, Times, serif;"> Author Name : <span class="text-primary"> <%=b.getAuthor()%> </span> </h4>
        <h4 style="font-family: Times New Roman, Times, serif;"> Category : <span class="text-primary"> <%=b.getBookCategory()%> </span> </h4>
        </div>

        <div class="col-md-6 text-center p-5 border bg-white">
        <h1 style="font-family: Times New Roman, Times, serif;" > <%=b.getBookname()%> </h1>

        <%
        if("Old".equalsIgnoreCase(b.getBookCategory())) {
        %>
        <h5 style="font-family: Times New Roman, Times, serif;" class="text-warning">Contact to Seller</h5>
        <h5 style="font-family: Times New Roman, Times, serif;"> <i class="fa-solid fa-envelope"></i> Email : <%=b.getEmail()%> </h5>
        <% }
        %>

        <div class="row mt-5">

        <div class="col-md-4 text-success text-center p-2">
        <i class="fa-solid fa-money-bill-wave fa-2x"></i>
        <p style="font-family: Times New Roman, Times, serif;" > Cash On Delivery </p>
        </div>

        <div class="col-md-4 text-success text-center p-2">
        <i class="fa-solid fa-rotate-left fa-2x"></i>
        <p style="font-family: Times New Roman, Times, serif;" > Return Available </p>
        </div>

        <div class="col-md-4 text-success text-center p-2">
        <i class="fa-solid fa-truck-moving fa-2x"></i>
        <p style="font-family: Times New Roman, Times, serif;" > Free Shipping </p>
        </div>

        </div>

        <%
        if("Old".equalsIgnoreCase(b.getBookCategory())) {
        %>
        <div class="text-center p-3">
        <a style="font-family: Times New Roman, Times, serif;" class="btn btn-secondary"> <i class="fa-solid fa-indian-rupee-sign"></i> <%=b.getPrice()%> </a>
        <a style="font-family: Times New Roman, Times, serif;" href="index.jsp" class="btn btn-primary"> <i class="fa-solid fa-cart-shopping"></i> Continue shopping </a>
        </div>
        <%
        } else {
        %>
        <div class="text-center p-3">
        <a style="font-family: Times New Roman, Times, serif;" class="btn btn-secondary"> <i class="fa-solid fa-indian-rupee-sign"></i> <%=b.getPrice()%> </a>
        <a style="font-family: Times New Roman, Times, serif;" href="" class="btn btn-primary"> <i class="fa-solid fa-cart-shopping"></i> Add to Cart </a>
        </div>
        <%
        }
        %>
</div>

</body>
</html>