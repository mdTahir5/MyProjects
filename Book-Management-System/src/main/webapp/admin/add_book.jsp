<%@ page import="com.admin.servlet.BooksAdd" %>
<html>
<head>
<%@ include file="allcss.jsp" %>

<style type="text/css">
body{
background-color: #f0f1f2;
}
</style>

</head>
<body>
<%@ include file="navbar.jsp" %>

<%
    if (session.getAttribute("userObj") == null) {
        response.sendRedirect("../login.jsp");
    }
%>

<div class="container">
    <div class="row">
        <div class="col-md-4 offset-md-4">
            <div class="card mt-2">
                <div class="card-body">
                <h4 class="text-center"> Add Books </h4>

                <c:if test="${not empty successMsg}">
                <p class="text-center text-success">${successMsg}</p>
                <%session.removeAttribute("successMsg");%>
                </c:if>
                <c:if test="${not empty failedMsg}">
                <p class="text-center text-danger">${failedMsg}</p>
                <%session.removeAttribute("failedMsg");%>
                </c:if>

                  <form action="../add_book" method="post" enctype="multipart/form-data">
                  <div class="mb-3">
                    <label for="exampleInputEmail1" class="form-label"> <i class="fa-solid fa-book"></i> Book Name</label>
                    <input type="text" name="bookname" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" required="required">
                  </div>

                  <div class="mb-3">
                    <label for="exampleInputEmail1" class="form-label"> <i class="fa-solid fa-user"></i> Author Name</label>
                    <input type="text" name="author" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" required="required">
                  </div>

                  <div class="mb-3">
                    <label for="exampleInputPassword1" class="form-label"> <i class="fa-solid fa-indian-rupee-sign"></i> Price</label>
                    <input type="text" name="price" class="form-control" id="exampleInputPassword1" required="required">
                  </div>
                    <div class="mb-3">
                      <label for="validationCustom04" class="form-label"> <i class="fa-solid fa-list"></i> Book Categories</label>
                      <select class="form-select" id="validationCustom04" name="categories" required>
                        <option selected disabled value="">----- select -----</option>
                        <option value="New">New Book</option>
                      </select>
                      </div>

                    <div class="mb-3">
                    <label for="validationCustom04" class="form-label"> <i class="fa-solid fa-circle-info"></i> Book Status</label>
                    <select class="form-select" id="validationCustom04" name="status" required>
                    <option selected disabled value="">----- select -----</option>
                    <option value="Active">Active</option>
                    <option value="Inactive">Inactive</option>
                    </select>
                    </div>

                    <div class="input-group mb-3">
                      <input type="file" name="bookimg" class="form-control" id="inputGroupFile02">
                      <label class="input-group-text" for="inputGroupFile02">Upload</label>
                    </div>

                  <div class="text-center">
                  <button type="submit" class="btn btn-outline-primary text-center">Add Book</button>
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