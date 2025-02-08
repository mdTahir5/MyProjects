<html>
<head>
<%@include file="components/allcss.jsp" %>
</head>

<body style="background-color: #ecedeb;">
<%@include file="components/navbar.jsp" %>
<div class="container">
    <div class="row mt-5">
        <div class="col-md-4 offset-md-4">
            <div class="card">
                <div class="card-body">
                    <h4 class="text-center"><i class="fa-solid fa-circle-user"></i> Login</h4>

                    <c:if test="${not empty failedMsg}" >
                    <h6 class="text-center text-danger">${failedMsg}</h6>
                    <% session.removeAttribute("failedMsg");%>
                    </c:if>

                    <c:if test="${not empty successMsg}" >
                    <h6 class="text-center text-success">${successMsg}</h6>
                    <% session.removeAttribute("successMsg");%>
                    </c:if>

                <form action="login" method="post">
                  <div class="mb-3">
                    <label for="exampleInputEmail1" class="form-label"> <i class="fa-solid fa-envelope"></i> Email Address</label>
                    <input type="email" name="email" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" required="required">
                  </div>

                  <div class="mb-3">
                    <label for="exampleInputPassword1" class="form-label"> <i class="fa-solid fa-lock"></i> Password</label>
                    <input type="password" name="password" class="form-control" id="exampleInputPassword1" required="required">
                  </div>

                  <div class="text-center">
                  <button type="submit" class="btn btn-outline-success text-center"> <i class="fa-solid fa-arrow-right-to-bracket"></i> Login</button>
                  <p class="mt-2">Don't have an Account <a href="register.jsp">click here</a> </p>
                  </div>
                </form>

                </div>
            </div>
        </div>
    </div>
</div>
<div style="margin-top: 150px;">
<%@include file="components/footer.jsp" %>
</div>
</body>
</html>