<html>
<head>
<title>Edit Address</title>
<%@include file="components/allcss.jsp" %>
</head>
<body style="background-color: #f0f1f2;">
<%@include file="components/navbar.jsp" %>

<div class="container">
    <div class="row p-3">
        <div class="col-md-6 offset-md-3">
            <div class="card mt-3">
                <div class="card-body">
                    <h4 class="text-center text-success"> <i class="fa-solid fa-map-location-dot"></i> Add Address </h4>

                <form action="register" method="post">

            <div class="row g-6 mt-4">
                  <div class="form-group col-md-6">
                  <label for="inputEmail4" class="form-label"> <i class="fa-solid fa-location-dot"></i> Address</label>
                  <input type="number" value="" readonly="readonly" class="form-control" id="inputEmail4">
                  </div>
                  <div class="form-group col-md-6">
                  <label for="inputPassword4" class="form-label"> <i class="fa-solid fa-landmark-flag"></i> Landmark</label>
                  <input type="text" value="" readonly="readonly" class="form-control" id="inputPassword4">
                  </div>
            </div>

            <div class="row g-6 mt-4">
                  <div class="form-group col-md-4">
                  <label for="inputEmail4" class="form-label"> <i class="fa-solid fa-city"></i> City</label>
                  <input type="number" value="" readonly="readonly" class="form-control" id="inputEmail4">
                  </div>
                  <div class="form-group col-md-4">
                  <label for="inputPassword4" class="form-label"> <i class="fa-solid fa-map"></i> State</label>
                  <input type="text" value="" readonly="readonly" class="form-control" id="inputPassword4">
                  </div>
                  <div class="form-group col-md-4">
                  <label for="inputPassword4" class="form-label"> <i class="fa-solid fa-list-ol"></i> Pincode</label>
                  <input type="text" value="" readonly="readonly" class="form-control" id="inputPassword4">
                  </div>
            </div>

            <div class="text-center mt-5">
            <a href="" class="btn btn-warning"> Add Address </a>
            </div>


                </form>


                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
