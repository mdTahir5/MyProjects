<%@ page import="com.entity.BookDetails" %>
<%@ page import="com.DAO.BookDAOImpl" %>
<%@ page import="com.DB.DBConnect" %>
<%@ page import="com.entity.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="com.user.servlet.LoginServlet" %>


<html>
<head>
<%@include file="components/allcss.jsp" %>
<style type="text/css">
.back-img{
background: url("img/background2_img.jpg");
height: 60vh;
width: 100%;
background-repeat: no-repeat;
background-size: cover;
}

.card-ho:hover{
background-color: #eceff1;
}
body{
background-color: #f0f1f2;
}

</style>
</head>
<body>

<%
User u = (User)session.getAttribute("userObj");
%>

<%@include file="components/navbar.jsp" %>

<div class="container-fluid back-img">
</div>

<!-- Start Recent Book-->

<div class="container">
<h3 class="text-center mt-2"> Recent Book </h3>
<div class="row">
<%
BookDAOImpl dao2 = new BookDAOImpl(DBConnect.getConn());
List<BookDetails> list2 = dao2.getRecentBooks();
for(BookDetails b : list2) {
%>
        <div class="col-md-3">
            <div class="card card-ho">
                <div class="card-body text-center">
                <img style="width: 160px; height: 200px;" class="img-thumbnail" alt="" src="books/<%=b.getPhotoName()%>">
                <p> Book Name : <%=b.getBookname()%> </p>
                <p> Author : <%=b.getAuthor()%> </p>
                <p> Price : <i class="fa-solid fa-indian-rupee-sign"></i> <%=b.getPrice()%></p>
                <p> Book Category : <%=b.getBookCategory()%> </p>

                <%
                if(b.getBookCategory().equalsIgnoreCase("Old")) {
                %>
                    <div class="col">
                    <a href="view_books.jsp?bid=<%=b.getBookId() %>" class="btn btn-primary btn-sm"> <i class="fa-solid fa-eye"></i> View Details</a>
                    </div>
                <%
                } else {
                %>
                    <div class="col">
                    <%
                    if(u == null) {
                    %>
                    <a href="login.jsp" class="btn btn-success btn-sm"> <i class="fa-solid fa-cart-shopping"></i> Add to Cart</a>
                    <%
                    } else {
                    %>
                    <a href="cart?bookId=<%=b.getBookId()%>&&userId=<%=u.getId()%>" class="btn btn-success btn-sm"> <i class="fa-solid fa-cart-shopping"></i> Add to Cart</a>
                    <%
                    }
                    %>
                    <a href="view_books.jsp?bid=<%=b.getBookId() %>" class="btn btn-primary btn-sm"> <i class="fa-solid fa-eye"></i> View Details</a>
                    </div>
                <%
                }
                %>
                </div>
            </div>
        </div>
<%
}
%>
    </div>
    <div class="mt-2 text-center">
    <a href="all_recent_books.jsp" class="btn btn-secondary text-white btn-sm">View All </a>
    </div>
</div>
<!-- End Recent Book-->
<hr>

<!-- Start New Book-->

<div class="container">
<h3 class="text-center"> New Book </h3>
    <div class="row">

        <%
        BookDAOImpl dao = new BookDAOImpl(DBConnect.getConn());
        List<BookDetails> list = dao.getNewBooks();
        for(BookDetails b : list) {
        %>
                <div class="col-md-3">
                <div class="card card-ho">
                <div class="card-body text-center">
                <img style="width: 160px; height: 200px;" class="img-thumbnail" alt="" src="books/<%=b.getPhotoName()%>">
               <p> Book Name : <%=b.getBookname()%> </p>
               <p> Author : <%=b.getAuthor()%> </p>
               <p> Price : <i class="fa-solid fa-indian-rupee-sign"></i> <%=b.getPrice()%></p>
               <p> Book Category : <%=b.getBookCategory()%> </p>
                    <div class="col">
                    <%
                    if(u == null) {
                    %>
                    <a href="login.jsp" class="btn btn-success btn-sm"> <i class="fa-solid fa-cart-shopping"></i> Add to Cart</a>
                    <%
                    } else {
                    %>
                    <a href="cart?bookId=<%=b.getBookId()%>&&userId=<%=u.getId()%>" class="btn btn-success btn-sm"> <i class="fa-solid fa-cart-shopping"></i> Add to Cart</a>
                    <%
                    }
                    %>
                    <a href="view_books.jsp?bid=<%=b.getBookId() %>" class="btn btn-primary btn-sm"> <i class="fa-solid fa-eye"></i> View Details</a>
                    </div>
                </div>
            </div>
        </div>
            <%
            }
            %>
    </div>
    <div class="mt-2 text-center">
    <a href="all_new_books.jsp" class="btn btn-secondary btn-sm">View All </a>
    </div>
</div>
<!-- End New Book-->
<hr>

<!-- Start Old Book-->

<div class="container">
<h3 class="text-center"> Old Book </h3>
    <div class="row">

    <%
    BookDAOImpl dao3 = new BookDAOImpl(DBConnect.getConn());
    List<BookDetails> list3 = dao3.getOldBooks();
    for(BookDetails b : list3) {
    %>
        <div class="col-md-3">
            <div class="card card-ho">
                <div class="card-body text-center">
                <img style="width: 160px; height: 200px;" class="img-thumbnail" alt="" src="books/<%=b.getPhotoName()%>">
                <p> Book Name : <%=b.getBookname()%> </p>
                <p> Author : <%=b.getAuthor()%> </p>
                <p> Price : <i class="fa-solid fa-indian-rupee-sign"></i> <%=b.getPrice()%> </p>
                <p> Book Category : <%=b.getBookCategory()%> </p>
                    <div class="col">
                    <a href="view_books.jsp?bid=<%=b.getBookId() %>" class="btn btn-primary btn-sm"> <i class="fa-solid fa-eye"></i> View Details</a>
                    </div>
                </div>
            </div>
        </div>
        <%
        }
        %>
    </div>
    <div class="mt-2 text-center">
    <a href="all_old_books.jsp" class="btn btn-secondary btn-sm">View All </a>
    </div>
</div>
<!-- End Old Book-->

<%@include file="components/footer.jsp" %>
</body>
</html>