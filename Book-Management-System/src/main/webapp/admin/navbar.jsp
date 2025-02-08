<%@ page import="com.entity.User" %>
<%@ page import="com.user.servlet.LoginServlet" %>

<div class="container-fluid" style="height: 10px; background-color: #8e24aa;"> </div>

<div class="container-fluid p-3">
<div class="row">
<div class="col-md-3">
<h3> <i class="fa-solid fa-book"></i> Book Management </h3>
</div>

<div style="margin-left:680px" class="col-md-3">
<c:if test="${not empty userObj }">
<a class="btn btn-success ms-5"> <i class="fa-solid fa-user"></i> ${userObj.name}</a>
<a data-bs-toggle="modal" data-bs-target="#exampleModal" class="btn btn-danger"> <i class="fa-solid fa-right-from-bracket"></i> Logout</a>
</c:if>

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

<nav class="navbar navbar-expand-lg navbar-dark bg-custom">
  <div class="container-fluid">

    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <a class="nav-link active" aria-current="page" href="home.jsp"> <i class="fa-solid fa-house"></i> Home</a>
        </li>
      </ul>

    </div>
  </div>
</nav>