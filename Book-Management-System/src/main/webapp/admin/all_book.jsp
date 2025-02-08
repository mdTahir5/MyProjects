<%@ page import="com.entity.BookDetails" %>
<%@ page import="com.DAO.BookDAOImpl" %>
<%@ page import="com.DB.DBConnect" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<html>
<head>
<%@ include file="allcss.jsp" %>

</head>
<body>
<%@ include file="navbar.jsp" %>
<%
    if (session.getAttribute("userObj") == null) {
        response.sendRedirect("../login.jsp");
    }
%>

<h3 class="text-center mt-3"> All Books </h3>

                <c:if test="${not empty successMsg}">

                <div class="text-center"> <h7 class="text-center text-success">${successMsg}</h7> </div>
                <%session.removeAttribute("successMsg");%>
                </c:if>
                <c:if test="${not empty failedMsg}">
                <div class="text-center"> <h7 class="text-center text-danger">${failedMsg}</h7> </div>
                <%session.removeAttribute("failedMsg");%>

                </c:if>

<table class="table table-striped mt-3">
    <thead>
    <tr>
        <th scope="col">ID</th>
        <th scope="col">Image</th>
        <th scope="col">Book Name</th>
        <th scope="col">Author</th>
        <th scope="col">Price</th>
        <th scope="col">Categories</th>
        <th scope="col">Status</th>
        <th scope="col">Action</th>
    </tr>
    </thead>
    <tbody>

    <%
    BookDAOImpl dao = new BookDAOImpl(DBConnect.getConn());
    List<BookDetails> list = dao.getAllBooks();
    for(BookDetails b : list) {
    %>
    <tr>
        <td><%=b.getBookId()%></td>
        <td><img src="../books/<%=b.getPhotoName()%>" style="width: 50px; height: 50px;"></td>
        <td><%=b.getBookname()%></td>
        <td><%=b.getAuthor()%></td>
        <td><%=b.getPrice()%></td>
        <td><%=b.getBookCategory()%></td>
        <td><%=b.getStatus()%></td>
        <td>
        <a href="edit_book.jsp?id=<%=b.getBookId()%>" class="btn btn-sm btn-primary"> <i class="fa-solid fa-pen-to-square"></i> Edit </a>
        <a href="../delete?id=<%=b.getBookId()%>" class="btn btn-sm btn-danger"> <i class="fa-solid fa-trash"></i> Delete </a>
        </td>
    </tr>
    <%
    }
    %>
    </tbody>

</table>


</body>
</html>