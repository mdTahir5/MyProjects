<%@ page import="com.user.servlet.LoginServlet" %>
<%@ page import="com.entity.User" %>
<%@ page import="com.user.servlet.CartServlet" %>



<div class="container-fluid" style="height: 10px; background-color: #8e24aa;"> </div>
<div class="container-fluid p-3">
<div class="row">
<div class="col-md-3">
<h3> <i class="fa-solid fa-book"></i> Book Management </h3>
</div>
<div class="col-md-4">
      <form action="search.jsp" method="post" class="d-flex col-md-15" role="search">
        <input name="ch" class="form-control me-4" type="search" placeholder="Search" aria-label="Search">
        <button class="btn btn-outline-success col-md-3" type="submit"> <i class="fa-solid fa-magnifying-glass"></i> Search</button>
      </form>
</div>

<%
User user = (User)session.getAttribute("userObj");
%>

<%
if(user != null) {
%>
<div class="col-md-3 ms-5">
<a href="checkout.jsp" class="me-4"><i class="fa-solid fa-cart-arrow-down fa-2x"></i></a>
<a class="btn btn-success"> <i class="fa-solid fa-circle-user"></i> <%=user.getName()%> </a>
<a href="logout" class="btn btn-danger ms-2"> <i class="fa-solid fa-right-from-bracket"></i> Logout</a>
</div>
<%
} else {
%>
<div class="col-md-3 ms-5"">
<a href="login.jsp" class="btn btn-success"> <i class="fa-solid fa-right-to-bracket"></i> Login</a>
<a href="register.jsp" class="btn btn-primary ms-2"> <i class="fa-solid fa-user-plus"></i> Register</a>
</div>
<%
}
%>

</div>
</div>

<nav class="navbar navbar-expand-lg navbar-dark bg-custom">
  <div class="container-fluid">

    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <a class="nav-link active" aria-current="page" href="index.jsp"> <i class="fa-solid fa-house"></i> Home</a>
        </li>
        <li class="nav-item">
          <a class="nav-link active" aria-current="page" href="all_recent_books.jsp"> <i class="fa-solid fa-book-open"></i> Recent Book</a>
        </li>
        <li class="nav-item">
          <a class="nav-link active" aria-current="page" href="all_new_books.jsp"> <i class="fa-solid fa-book-open"></i> New Book</a>
        </li>
         <li class="nav-item">
         <a class="nav-link active" aria-current="page" href="all_old_books.jsp"> <i class="fa-solid fa-book-open"></i> Old Book</a>
         </li>
      </ul>

      <form class="d-flex me-5" role="search">
            <a href="setting.jsp" class="btn btn-light my-2 me-2 my-sm-0" type="submit"> <i class="fa-solid fa-gear"></i> Setting</a>
            <a href="helpline.jsp" class="btn btn-light my-2 ms-2 my-sm-0" type="submit"> <i class="fa-solid fa-square-phone"></i> Contact us</a>
      </form>

    </div>
  </div>
</nav>