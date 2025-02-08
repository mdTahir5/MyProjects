<%@ page import="com.user.servlet.RegisterServlet" %>

<html>
<head>
<%@include file="components/allcss.jsp" %>
</head>

<body style="background-color: #ecedeb;">
<%@include file="components/navbar.jsp" %>
<div class="container">
    <div class="row mt-3">
        <div class="col-md-4 offset-md-4">
            <div class="card">
                <div class="card-body">
                    <h4 class="text-center"> <i class="fa-solid fa-address-card"></i> Registration</h4>

                    <c:if test="${not empty successMsg}">
                    <p class="text-center text-success">${successMsg}</p>
                    <%session.removeAttribute("successMsg");%>
                    </c:if>

                    <c:if test="${not empty failedMsg}">
                    <p class="text-center text-danger">${failedMsg}</p>
                    <%session.removeAttribute("failedMsg");%>
                    </c:if>

                <form action="register" method="post">
                  <div class="mb-3">
                    <label for="exampleInputEmail1" class="form-label"> <i class="fa-solid fa-user"></i> Name</label>
                    <input type="text" name="name" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" required="required">
                  </div>

                  <div class="mb-3">
                    <label for="exampleInputEmail1" class="form-label"> <i class="fa-solid fa-envelope"></i> Email Address</label>
                    <input type="email" name="email" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" required="required">
                  </div>

                  <div class="mb-3">
                    <label for="exampleInputPassword1" class="form-label"> <i class="fa-solid fa-mobile"></i> Phone No.</label>
                    <input type="number" name="phno" class="form-control" id="exampleInputPassword1" required="required">
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
                  <button type="submit" class="btn btn-outline-primary text-center"> <i class="fa-solid fa-user-plus"></i> Register</button>
                  </div>
                </form>

                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="components/footer.jsp" %>
</body>
</html>