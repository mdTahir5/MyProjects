<%@ page import="com.entity.BookDetails" %>
<%@ page import="com.DAO.BookDAOImpl" %>
<%@ page import="com.DB.DBConnect" %>
<html>
<head>
<%@ include file="allcss.jsp" %>

<style type="text/css">
body{
background-color: #ecedeb;
}
</style>

</head>
<body>
<%@ include file="navbar.jsp" %>
<div class="container">
    <div class="row">
        <div class="col-md-4 offset-md-4">
            <div class="card mt-2">
                <div class="card-body">
                <h4 class="text-center"> <i class="fa-solid fa-file-pen"></i> Edit Books </h4>

                <%
                int id = Integer.parseInt(request.getParameter("id"));
                BookDAOImpl dao = new BookDAOImpl(DBConnect.getConn());
                BookDetails b = dao.getBookById(id);
                %>


                  <form action="../edit_book" method="post">
                  <input type="hidden" name="id" value="<%=b.getBookId()%>" >
                  <div class="mb-3">
                    <label for="exampleInputEmail1" class="form-label"> <i class="fa-solid fa-book"></i> Book Name</label>
                    <input type="text" name="bookname" class="form-control" value="<%=b.getBookname()%>" id="exampleInputEmail1" aria-describedby="emailHelp" required="required">
                  </div>

                  <div class="mb-3">
                    <label for="exampleInputEmail1" class="form-label"> <i class="fa-solid fa-user"></i> Author Name</label>
                    <input type="text" name="author" class="form-control" value="<%=b.getAuthor()%>" id="exampleInputEmail1" aria-describedby="emailHelp" required="required">
                  </div>

                  <div class="mb-3">
                    <label for="exampleInputPassword1" class="form-label"> <i class="fa-solid fa-indian-rupee-sign"></i> Price</label>
                    <input type="text" name="price" class="form-control" value="<%=b.getPrice()%>" id="exampleInputPassword1" required="required">
                  </div>

                    <div class="mb-3">
                    <label for="validationCustom04" class="form-label"> <i class="fa-solid fa-circle-info"></i> Book Status</label>
                    <select class="form-select" id="validationCustom04" name="status" required>

                    <%
                    if("Active".equals(b.getStatus())) {
                    %>
                    <option value="Active">Active</option>
                    <%
                    } else {
                    %>
                    <option value="Inactive">Inactive</option>
                    <%
                    }
                    %>
                    </select>
                    </div>

                  <div class="text-center">
                  <button type="submit" class="btn btn-outline-primary text-center"> <i class="fa-solid fa-repeat"></i> Update</button>
                  </div>
                </form>

                </div>
            </div>
        </div>
    </div>
</div>

<div style="margin-top: 20px;">
<%@include file="footer.jsp" %>
</div>
</body>
</html>