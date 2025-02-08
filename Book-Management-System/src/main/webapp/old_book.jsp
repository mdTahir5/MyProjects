<%@ page import="com.user.servlet.LoginServlet" %>
<%@ page import="com.entity.BookDetails" %>
<%@ page import="com.DAO.BookDAOImpl" %>
<%@ page import="com.DB.DBConnect" %>
<%@ page import="com.entity.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>


<html>
<head>
<title>User : Old Book</title>
<%@include file="components/allcss.jsp" %>
<style type="text/css">

</style>
</head>
<body style="background-color: #f0f1f2;">
<%@include file="components/navbar.jsp" %>

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

<div class="container p-5">
<table class="table table-striped mt-4">
  <thead class="table-primary">
    <tr>
      <th scope="col">Book Name</th>
      <th scope="col">Author</th>
      <th scope="col">Price</th>
      <th scope="col">Category</th>
      <th scope="col">Action</th>
    </tr>
  </thead>
  <tbody>

<%
User u = (User)session.getAttribute("userObj");
%>
<%
String email = u.getEmail();
BookDAOImpl dao = new BookDAOImpl(DBConnect.getConn());
List<BookDetails> list = dao.getBookByOld(email, "Old");
for(BookDetails b : list) {
%>
    <tr>
      <td> <%=b.getBookname()%> </td>
      <td> <%=b.getAuthor()%> </td>
      <td> <%=b.getPrice()%> </td>
      <td> <%=b.getBookCategory()%> </td>
      <td><a href="delete_old_book?em=<%=email%>&&id=<%=b.getBookId()%>"
      class="btn btn-sm btn-danger"> <i class="fa-solid fa-trash-can"></i> Delete</a></td>
    </tr>
<%
}
%>

  </tbody>
</table>
</div>

</body>
</html>