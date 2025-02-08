<%@ page import="com.entity.User" %>
<%@ page import="com.user.servlet.LoginServlet" %>
<%@ page import="com.user.servlet.LogoutServlet" %>

<html>
<head>
<%@ include file="allcss.jsp" %>
<style type="text/css">
a{
text-decoration: none;
color: black;
}
a:hover{
text-decoration: none;
color: black;
}

.card:hover{
background-color: #eceff1;
}


</style>
</head>
<body style="background-color: #f0f1f2;">
<%@ include file="navbar.jsp" %>
<%
    if ((User)session.getAttribute("userObj") == null) {
        response.sendRedirect("login.jsp");
    }
%>



<div class="container">
<h2 class="text-center mt-3 text-success"> Hello, Admin </h2>
    <div class="row p-5">
        <div class="col-md-3">
            <a href="add_book.jsp">
            <div class="card">
                <div class="card-body text-center">
                <i class="fa-solid fa-square-plus fa-3x"></i>
                <h4>Add Books</h4>
                </div>
            </div>
            </a>
        </div>

        <div class="col-md-3">
            <a href="all_book.jsp">
             <div class="card">
                    <div class="card-body text-center">
                    <i class="fa-solid fa-book-open fa-3x text-success"></i>
                    <h4>All Books</h4>
                   </div>
             </div>
             </a>
         </div>

       <div class="col-md-3">
           <a href="orders.jsp">
           <div class="card">
               <div class="card-body text-center">
               <i class="fa-solid fa-cart-shopping fa-3x text-primary"></i>
                <h4>Orders</h4>
                 </div>
            </div>
            </a>
       </div>

       <div class="col-md-3">
           <a data-bs-toggle="modal" data-bs-target="#exampleModal">
          <div class="card">
              <div class="card-body text-center">
              <i class="fa-solid fa-right-from-bracket fa-3x text-danger"></i>
              <h4>Logout</h4>
              </div>
           </div>
           </a>
       </div>
    </div>
</div>

<!-- Logout Modal-->

<!-- Modal -->
<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <div class="text-center">
        <h4> Do you want to logout </h4>
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
        <a href="../logout" type="button" class="btn btn-primary">Logout</a>
        </div>
      </div>
      <div class="modal-footer">

      </div>
    </div>
  </div>
</div>
<!-- End Logout Modal-->

<div style="margin-top: 280px;">
<%@include file="footer.jsp" %>
</div>

</body>
</html>