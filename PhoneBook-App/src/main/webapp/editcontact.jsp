<%@ page import="com.entity.User" %>
<%@ page import="com.dao.ContactDAO" %>
<%@ page import="com.connection.DBconnect" %>
<%@ page import="java.util.List" %>
<%@ page import="com.entity.Contact" %>



<html>
<head>
<%@ include file = "/components/allcss.jsp"%>
<link rel="stylesheet" href="components/style.css">

<style type="text/css">
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
<%@ include file = "/components/navbarStyle.jsp"%>

<%
    if (user == null) {
    session.setAttribute("invalidMsg", "Please Login First");
    response.sendRedirect("login.jsp");
    }
%>

<div style="margin-left: 30%; margin-right: 30%; " class="content_container py-4 d-flex flex-column justify-content-center align-items-center" >

    <h2 class="text-dark"> Edit Contact Details </h2>

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

    <form action="<%=application.getContextPath() %>/update" method="post" class="mt-2 text-dark col-6">

    <%
        int cid = Integer.parseInt((String)request.getParameter("cid"));
        ContactDAO dao = new ContactDAO(DBconnect.getConn());
        Contact c = dao.getContactByID(cid);
    %>

    <input type="hidden" value="<%= cid%>" name="cid" >



      <div class="mb-2">
        <label for="exampleInputEmail1" class="form-label text-dark"> <i class="bi bi-person"></i> Name</label>
        <input value="<%=c.getName()%>" name="name" type="text" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp">
      </div>

      <div class="mb-2">
        <label for="exampleInputPassword1" class="form-label text-dark"> <i class="bi bi-envelope-at"></i> Email</label>
        <input value="<%=c.getEmail()%>" name="email" type="text" class="form-control" id="exampleInputPassword1">
      </div>

      <div class="mb-2">
         <label for="exampleInputPassword1" class="form-label text-dark"> <i class="bi bi-telephone"></i> Phone Number</label>
         <input value="<%=c.getPhoneNo()%>" name="phoneNo" type="text" class="form-control" id="exampleInputPassword1">
      </div>

      <div class="mb-2">
          <label for="validationTextarea" class="form-label"> <i class="bi bi-file-person"></i> About</label>
          <textarea value="<%=c.getAbout()%>" name="about" class="form-control" id="validationTextarea" required></textarea>
        </div>

        <div class="container text-center mt-3 ">
        <button type="submit" class="btn btn-primary col-4 mx-auto mt-3">Update</button>
        </div>

      </div>
    </form>

    </div>

<div style="margin-top:100px" class="loginfooter">
<%@ include file = "components/footer.jsp"%>
</div>

</body>
</html>