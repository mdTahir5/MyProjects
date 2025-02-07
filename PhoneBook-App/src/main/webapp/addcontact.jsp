<%@ page import="com.entity.User" %>

<html>
<%@ include file = "/components/allcss.jsp"%>
<head>
<%@ include file = "/components/navbarStyle.jsp"%>
<link rel="stylesheet" href="components/style.css">
<style type="text/css">
body{
font-family:Times New Roman,Times,serif;
}
.loginform{
width:30%;
}
 .content_container{
margin-top:40px;
margin-left:30%;
margin-right:30%;
border:5px solid white;
border-radius:4px;
background:white;
}
.content_container:hover{
background-color:#f0f4c3;
}
form {
   margin:2px;
}
.bg-custom{
background: green;
}

.navbar .nav-item:hover .nav-link{
background-color: white;
color: green;
border-radius: 10px;
}

</style>
</head>
<body>

<%

    if (user == null) {
    session.setAttribute("invalidMsg", "Please Login First");
    response.sendRedirect("login.jsp");
    }
%>


<div style="" class="content_container py-4 d-flex flex-column justify-content-center align-items-center" >

    <h2 class="text-dark"> Add Contact Details </h2>

    <%
        String successMsg = (String)session.getAttribute("successMsg");
        String failedMsg = (String)session.getAttribute("failedMsg");
        if(successMsg != null) {
        %>
        <p class="text-success text-center"><%=successMsg%></p>
         <%
         session.removeAttribute("successMsg");
         }
         if(failedMsg != null) {
         %>
         <p class="text-danger text-center"><%=failedMsg%></p>
         <%
         session.removeAttribute("failedMsg");
         }
         %>

    <form action="<%= application.getContextPath() %>/AddContact" method="post" class="mt-2 text-dark col-6">

    <%
    if(user != null) {
    %>
    <input type="hidden" value="<%= user.getId()%>" name="userId">
    <%}
    %>

      <div class="mb-2">
        <label for="exampleInputEmail1" class="form-label text-dark"> <i class="bi bi-person"></i> Name</label>
        <input name="name" placeholder="enter name" type="text" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp">
      </div>

      <div class="mb-2">
        <label for="exampleInputPassword1" class="form-label text-dark"> <i class="bi bi-envelope-at"></i> Email</label>
        <input name="email" type="text" placeholder="enter email" class="form-control" id="exampleInputPassword1">
      </div>

      <div class="mb-2">
         <label for="exampleInputPassword1" class="form-label text-dark"> <i class="bi bi-telephone"></i> Phone Number</label>
         <input name="phoneNo" type="text" placeholder="Phone No." class="form-control" id="exampleInputPassword1">
      </div>

      <div class="mb-2">
          <label for="validationTextarea" class="form-label"> <i class="bi bi-file-person"></i> About</label>
          <textarea name="about" class="form-control" id="validationTextarea" placeholder="About" required></textarea>
        </div>

        <div class="container text-center mt-3 ">
        <button type="submit" class="btn btn-primary col-6 mx-auto mt-4">Add</button>
        </div>

      </div>
    </form>

    </div>

<div style="margin-top:100px" class="loginfooter">
<%@ include file = "components/footer.jsp"%>
</div>
</body>
</html>