<%@ page import="com.entity.User" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-success">
  <div class="container-fluid">
    <a style="font-family:Times New Roman,Times,serif"; class="navbar-brand" href="#"> <i class="bi bi-journal-bookmark-fill"></i> Phonebook</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
    aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <a style="font-family:Times New Roman,Times,serif" class="nav-link active" aria-current="page" href="index.jsp"> <i class="bi bi-house-fill"></i> Home</a>
        </li>
        <li class="nav-item">
           <a style="font-family:Times New Roman,Times,serif" class="nav-link active"  href="addcontact.jsp"> <i class="bi bi-database-fill-add"></i> Add Contact</a>
        </li>
        <li class="nav-item">
           <a style="font-family:Times New Roman,Times,serif" class="nav-link active" aria-current="page" href="viewcontact.jsp"> <i class="bi bi-eye-fill"></i> View Contact</a>
        </li>
      </ul>

      <% User user=(User)session.getAttribute("user");
      if(user == null) {
      %>
      <form class="d-flex" role="search">
         <a style="font-family:Times New Roman,Times,serif" href="login.jsp" class="btn btn-outline-light me-5 mt-3 ps-4 pe-4" > <i class="bi bi-box-arrow-in-right"></i> Login</a>
      </form>
      <%
      } else{
      %>
      <form class="d-flex" role="search">
        <h5 style="font-family:Times New Roman,Times,serif" class="mt-3 me-3 text-white"> <i class="bi bi-person-circle"></i> <%= user.getUsername() %> </h5>

        <a data-bs-toggle="modal" data-bs-target="#exampleModal" class="btn btn-danger ms-2 mt-2"> <i class="bi bi-box-arrow-left"></i> Logout </a>
      </form>
      <%
      }
      %>

      <!-- Logout Popup -->



      <!-- Modal -->
      <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h1 class="modal-title fs-5" id="exampleModalLabel"></h1>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body text-center">

              <h5> Do you want to Logout.. </h5>
             <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
             <a href="Logout" class="btn btn-primary">Logout</a>

            </div>
            <div class="modal-footer">

            </div>
          </div>
        </div>
      </div>

      <!-- Logout Popup -->


    </div>
  </div>
</nav>