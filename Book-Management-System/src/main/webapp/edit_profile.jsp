<html>
<head>
<title>Edit Profile</title>
<%@include file="components/allcss.jsp" %>
</head>
<body style="background-color: #f0f1f2;">
<%@include file="components/navbar.jsp" %>

<%
User u = (User)session.getAttribute("userObj");
%>

<div class="container">
    <div class="row">
        <div class="col-md-4 offset-md-4">
            <div class="card mt-3">
                <div class="card-body">
                    <h4 class="text-center text-primary"> <i class="fa-solid fa-pen-to-square"></i> Edit Profile </h4>

                    <c:if test="${not empty failedMsg}" >
                    <h6 class="text-center text-danger">${failedMsg}</h6>
                    <% session.removeAttribute("failedMsg");%>
                    </c:if>

                    <c:if test="${not empty successMsg}" >
                    <h6 class="text-center text-success">${successMsg}</h6>
                    <% session.removeAttribute("successMsg");%>
                    </c:if>

                  <form action="update_profile" method="post">
                    <input type="hidden" value="<%=u.getId()%>" name="id">
                  <div class="mb-3">
                    <label for="exampleInputEmail1" class="form-label"> <i class="fa-solid fa-user"></i> Name</label>
                    <input type="text" name="name" value="<%=u.getName()%>" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" required="required">
                  </div>

                  <div class="mb-3">
                    <label for="exampleInputEmail1" class="form-label"> <i class="fa-solid fa-envelope"></i> Email Address</label>
                    <input type="email" name="email" value="<%=u.getEmail()%>" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" required="required">
                  </div>

                  <div class="mb-3">
                    <label for="exampleInputPassword1" class="form-label"> <i class="fa-solid fa-mobile"></i> Phone No.</label>
                    <input type="number" name="phno" value="<%=u.getPhno()%>" class="form-control" id="exampleInputPassword1" required="required">
                  </div>

                  <div class="mb-3">
                    <label for="exampleInputPassword1" class="form-label"> <i class="fa-solid fa-lock"></i> Password</label>
                    <input type="password" name="password" class="form-control" id="exampleInputPassword1" required="required">
                  </div>

                  <div class="mb-3 form-check">
                    <input type="checkbox" name="check" class="form-check-input" id="exampleCheck1">
                    <label class="form-check-label" for="exampleCheck1">Agree terms & Conditions</label>
                  </div>

                  <div class="text-center">
                  <button type="submit" class="btn btn-outline-primary text-center"> <i class="fa-solid fa-retweet"></i> Update</button>
                  </div>
                </form>


                </div>
            </div>
        </div>
    </div>
</div>

<div style="margin-top:25px;">
<%@include file="components/footer.jsp" %>
</div>

</body>
</html>
