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
margin-top:20px;
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


<div style="margin-top:50px;" class="content_container py-4 d-flex flex-column justify-content-center align-items-center" >

    <h2 class="text-dark"> <i class="bi bi-person-vcard"></i> Register </h2>

    <%
    String fieldMsg = (String)session.getAttribute("fieldMsg");
    String successMsg = (String)session.getAttribute("successMsg");
    String errorMsg = (String)session.getAttribute("errorMsg");

    if(fieldMsg != null) {
    %>
    <p class="text-danger text-center"><%=fieldMsg%></p>
    <%
    session.removeAttribute("fieldMsg");
    }
    if(successMsg != null) {
    %>
    <p class="text-success text-center"><%=successMsg%></p>
     <%
     session.removeAttribute("successMsg");
     }
     if(errorMsg != null) {
     %>
     <p class="text-danger text-center"><%=errorMsg%></p>
     <%
     session.removeAttribute("errorMsg");
     }
     %>

    <form action="<%= application.getContextPath() %>/Register" method="post" class="mt-2 text-dark col-6">

      <div class="mb-4">
        <label for="exampleInputEmail1" class="form-label text-dark"> <i class="bi bi-person"></i> Username</label>
        <input name="RegUserName" placeholder="enter username" type="text" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp">
      </div>

      <div class="mb-4">
        <label for="exampleInputPassword1" class="form-label text-dark"><i class="bi bi-envelope-at"></i> Email Address</label>
        <input name="RegEmail" type="email" placeholder="enter email" class="form-control" id="exampleInputPassword1">
      </div>

      <div class="mb-4">
         <label for="exampleInputPassword1" class="form-label text-dark"> <i class="bi bi-key"></i> Password</label>
         <input name="RegPassword" type="password" placeholder="enter password" class="form-control" id="exampleInputPassword1">
      </div>


        <div class="container text-center mt-5 ">
        <button type="submit" class="btn btn-outline-primary col-6 mx-auto"> <i class="bi bi-person-add"></i> Register</button>
        </div>

      </div>
    </form>

    </div>

<div style="margin-top:120px" class="loginfooter">
<%@ include file = "components/footer.jsp"%>
</div>

</body>
</html>