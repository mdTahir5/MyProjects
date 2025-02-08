<%@ page import="com.entity.User" %>
<%@ page import="com.user.servlet.LoginServlet" %>

<html>
<head>
<title>Settings</title>
<%@include file="components/allcss.jsp" %>
<style type="text/css">
a{
text-decoration: none;
color :black;
}
a:hover {
text-decoration: none;
}

.card:hover{
background-color: #eceff1;
}

</style>
</head>
<body style="background-color: #f0f1f2;">

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
<%@include file="components/navbar.jsp" %>

<div class="container">
<h3 class="text-center mt-2"> Welcome, <%=u.getName()%> </h3>
    <div class="row p-4">

        <div class="col-md-4 mt-4">
        <a href="sell_book.jsp">
            <div class="card">
                <div class="card-body text-center">
                <i class="fa-solid fa-book-open fa-3x text-primary"></i>
                <h3> Sell Old Books </h3>
                </div>
            </div>
        </a>
        </div>

<div class="col-md-4 mt-4">
        <a href="old_book.jsp">
            <div class="card">
                <div class="card-body text-center">
                <i class="fa-solid fa-book-open fa-3x text-info"></i>
                <h3> Old Books </h3>
                </div>
            </div>
        </a>
        </div>

<div class="col-md-4 mt-4">
        <a href="edit_profile.jsp">
            <div class="card">
                <div class="card-body text-center">
                <i class="fa-solid fa-pen-to-square fa-3x text-primary"></i>
                <h3> (Edit Profile) </h3>
                </div>
            </div>
        </a>
        </div>

<div class="col-md-6 mt-4">
        <a href="myorder.jsp">
            <div class="card">
                <div class="card-body text-center">
                <i class="fa-solid fa-box-open fa-3x text-success"></i>
                <h3> My Orders </h3>
                <p> Track your Order </p>
                </div>
            </div>
        </a>
        </div>

<div class="col-md-6 mt-4">
        <a href="helpline.jsp">
            <div class="card">
                <div class="card-body text-center">
                <i class="fa-solid fa-circle-user fa-3x text-warning"></i>
                <h3> Help Center </h3>
                <p> 24 x 7 Service </p>
                </div>
            </div>
        </a>
        </div>




    </div>
</div>



</body>
</html>