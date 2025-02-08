<%@ page import="com.entity.User" %>
<%@ page import="com.user.servlet.LoginServlet" %>

<html>
<head>
<title>Sell Old Book</title>
<%@include file="components/allcss.jsp" %>
</head>

<body style="background-color: #f0f1f2;">
<%@include file="components/navbar.jsp" %>

<%
User us = (User)session.getAttribute("userObj");
%>
<%
if(us == null) {
response.sendRedirect("login.jsp");
%>
<%
}
%>

<div class="container">
    <div class="row">
        <div class="col-md-4 offset-md-4">
            <div class="card mt-3">
                <div class="card-body">
                <h4 class="text-center p-2"> Sell Old Books </h4>

                <c:if test="${not empty successMsg}">
                <p class="text-center text-success">${successMsg}</p>
                <%session.removeAttribute("successMsg");%>
                </c:if>
                <c:if test="${not empty failedMsg}">
                <p class="text-center text-danger">${failedMsg}</p>
                <%session.removeAttribute("failedMsg");%>
                </c:if>

                  <form action="add_old_book" method="post" enctype="multipart/form-data">
                  <input type="hidden" value="<%=us.getEmail()%>" name="user">

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

                    <div class="input-group mb-3">
                      <input type="file" name="bookimg" class="form-control" id="inputGroupFile02">
                      <label class="input-group-text" for="inputGroupFile02">Upload</label>
                    </div>

                  <div class="text-center">
                  <button type="submit" class="btn btn-primary text-center d-grid col-4 mx-auto">Sell</button>
                  </div>
                </form>




                </div>
            </div>
        </div>
    </div>
</div>

<div style="margin-top:65px;">
<%@include file="components/footer.jsp" %>
</div>

</body>
</html>