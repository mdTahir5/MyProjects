<%@ page import="com.entity.BookDetails" %>
<%@ page import="com.DAO.BookDAOImpl" %>
<%@ page import="com.DB.DBConnect" %>
<%@ page import="com.entity.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
<head>
<title> All Old Books </title>
<%@include file="components/allcss.jsp" %>
</head>

<body>
<%@include file="components/navbar.jsp" %>

<div class="container-fluid">
    <div class="row p-3">

     <%
        BookDAOImpl dao3 = new BookDAOImpl(DBConnect.getConn());
        List<BookDetails> list3 = dao3.getAllOldBooks();
        for(BookDetails b : list3) {
        %>
            <div class="col-md-3">
                <div class="card card-ho mt-2">
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
</div>



</body>
</html>