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

.crd-ho:hover{
background-color:#f0f4c3;
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
<%@ include file = "/components/navbarStyle.jsp"%>

<%
    if (user == null) {
    session.setAttribute("invalidMsg", "Please Login First");
    response.sendRedirect("login.jsp");
    }
%>

<%
    String successMsg = (String)session.getAttribute("successMsg");
    String failedMsg = (String)session.getAttribute("failedMsg");
    if(successMsg != null) {
%>
    <div class="alert alert-success text-center" role="alert"> <%=successMsg%> </div>
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




<div class="container">
    <div class="row p-4">

        <%
        if(user != null) {

        ContactDAO dao = new ContactDAO(DBconnect.getConn());
        List<Contact> contact = dao.getAllContact(user.getId());

        for(Contact c : contact) {
        %>
        <div class="col-md-3">
            <div class="card crd-ho">
                <div class="card-body">
                <h5>Name: <%= c.getName() %></h5>
                <p>Phone No: <%= c.getEmail() %></p>
                <p>Email: <%= c.getPhoneNo() %></p>
                <p>About: <%= c.getAbout() %></p>

                <div class="text-center">
                    <a href="editcontact.jsp?cid=<%=c.getId() %>" class="btn btn-primary me-1"> <i class="bi bi-pencil-square"></i> Edit</a>
                    <a href="delete?cid=<%=c.getId()%>" class="btn btn-danger ms-1"> <i class="bi bi-trash3"></i> Delete</a>
                </div>

                </div>
            </div>
          </div>
            <%
            }
            }
            %>

    </div>
</div>


</body>
</html>