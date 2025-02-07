<html>
<%@ include file = "/components/allcss.jsp"%>
<head>
<link rel="stylesheet" href="components/style.css">
<div>
<%@ include file = "/components/navbarStyle.jsp"%>
</div>

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

<div style="margin-top:105px;" class="nav-text-container text-center">
<h2 style="color:#5d4037; font-family: Times New Roman, Times, serif" ; font-size: 45px; "> Save Contact of your Friend's, Relative's and more...</h2>
</div>

<div style="display: flex; justify-content: center; align-items: center;" class="img-container mt-2">
    <img style="width: 300px; height: 300px; border-radius: 50%;" class="image mt-4" src="components/img/img1.jpg" alt="img">
</div>


<div style="margin-top:180px" class="loginfooter">
<%@ include file = "components/footer.jsp"%>
</div>
</body>

</html>

