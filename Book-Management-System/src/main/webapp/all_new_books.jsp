<%@ page import="com.entity.BookDetails" %>
<%@ page import="com.DAO.BookDAOImpl" %>
<%@ page import="com.DB.DBConnect" %>
<%@ page import="com.entity.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="com.user.servlet.CartServlet" %>

<%@ page import="java.util.concurrent.*" %>
<%@ page session="true" %>

<!DOCTYPE html>
<html>
<head>
<title> All New Books </title>
<%@include file="components/allcss.jsp" %>

<style type="text/css">

/* Style for the message */
.message {
  background-color: #28a745; /* Green color */
  color: white;
  padding: 10px 20px;
  border-radius: 5px;
  font-size: 18px;
  font-weight: bold;
  opacity: 0;
  animation: showMessage 3.5s ease-in-out forwards;
}

/* Animation to show the message */
@keyframes showMessage {
  0% {
    opacity: 0;
    transform: translateY(-20px);
  }
  50% {
    opacity: 1;
    transform: translateY(0);
  }
  100% {
    opacity: 0;
    transform: translateY(20px);
  }
}



</style>

</head>

<body>

<%
User u = (User)session.getAttribute("userObj");
%>

<%@include file="components/navbar.jsp" %>

<c:if test="${not empty addCart}">
<%
String addCart = (String) session.getAttribute("addCart");
if(addCart != null) {
%>
<div class="message text-center">${addCart}</div>
<%
}
%>
</c:if>

<div class="container-fluid">
    <div class="row p-3">

     <%
     BookDAOImpl dao = new BookDAOImpl(DBConnect.getConn());
     List<BookDetails> list = dao.getAllNewBooks();
     for(BookDetails b : list) {
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
</div>


</body>
</html>