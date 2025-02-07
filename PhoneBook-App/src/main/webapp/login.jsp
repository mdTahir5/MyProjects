<html>
<%@ include file = "/components/allcss.jsp"%>
<head>
<%@ include file = "components/navbarStyle.jsp"%>
<link rel="stylesheet" href="components/style.css">
<style type="text/css">

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

    <h2 style="font-family:Times New Roman,Times,serif" class="text-dark"> <i class="bi bi-person-circle"></i> Login </h2>
    <%
    String invalidMsg = (String)session.getAttribute("invalidMsg");
    if(invalidMsg != null)
    {%>
    <p class="text-danger text-center"><%=invalidMsg %></p>
    <%
    session.removeAttribute("invalidMsg");
    }
    %>

    <%
        String logoutMsg = (String)session.getAttribute("logoutMsg");
        if(logoutMsg != null)
        {%>
        <p class="text-success text-center"><%=logoutMsg %></p>
        <%
        session.removeAttribute("logoutMsg");
        }
        %>

    <form action="<%= application.getContextPath() %>/Login" method="post" class="mt-2 text-dark col-6">

      <div class="mb-4">
        <label style="font-family:Times New Roman,Times,serif" for="exampleInputEmail1" class="form-label text-dark"> <i class="bi bi-envelope-at"></i> Email address</label>
        <input name="email" placeholder="Enter here" type="email" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp">
      </div>

      <div class="mb-4">
        <label style="font-family:Times New Roman,Times,serif" for="exampleInputPassword1" class="form-label text-dark"> <i class="bi bi-key"></i> Password</label>
        <input name="password" type="password" placeholder="Enter Password" class="form-control" id="exampleInputPassword1">
      </div>

      <div class="mb-4">
      <p style="font-family:Times New Roman,Times,serif" > Don't have an account? <a href="register.jsp"> Register </a> </p>
       </div>

        <div class="container text-center mt-5 ">
        <button style="font-family:Times New Roman,Times,serif" type="submit" class="btn btn-outline-success col-6 mx-auto">
        <i class="bi bi-box-arrow-in-right"></i> Login</button>
        </div>

      </div>
    </form>

    </div>

<div style="margin-top:160px" class="loginfooter">
<%@ include file = "components/footer.jsp"%>
</div>

</body>
</html>