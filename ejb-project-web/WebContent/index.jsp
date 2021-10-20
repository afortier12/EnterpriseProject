<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" import="java.util.Iterator"
	import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet"
	href="https://unpkg.com/purecss@2.0.3/build/pure-min.css"
	integrity="sha384-cg6SkqEOCV1NbJoCu11+bm0NvBRc8IYLRGXkmNrqUBfTjmMYwNKPWBTIKyw9mHNJ"
	crossorigin="anonymous">
<style type="text/css">
form {
	margin-top: 10px;
	margin-left: 10px;
}

h1 {
	margin-top: 10px;
	margin-left: 10px;
}

table.users {
	table-layout: auto;
	width: 100%;
}

p {
	text-align: center
}
</style>
<script src="js/header.js" type="text/javascript" defer=""></script>
<title>Tool Management - Administration</title>
</head>
<body>
	<%
		//allow access only if session exists
		String user = null;
		if (session.getAttribute("user") == null) {
			response.sendRedirect("login.html");
		} else
			user = (String) session.getAttribute("user");
		String userName = null;
		String sessionID = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("user"))
					userName = cookie.getValue();
				if (cookie.getName().equals("JSESSIONID"))
					sessionID = cookie.getValue();
			}
		}
	%>
	<h3>
		Hi
		<%=userName%>, Login successful. </h3>
	<br>
	<h1>Tool Monitoring - Administrative Tools</h1>
	<form class="pure-form pure-form-stacked" method="get"
		action="SelectModel" enctype="multipart/form-data">
		<fieldset>
			<legend>Data Modeling Tools</legend>
			<div class="pure-g">
				<p></p>
				<div class="pure-u-1-2">Create data model</div>
				<div class="pure-u-1-2">
					<button type="submit" class="pure-button pure-button-primary">Select</button>
				</div>
			</div>
			<p></p>
			<div></div>
		</fieldset>
	</form>
	
	<form class="pure-form pure-form-stacked" method="get"
		action="EvaluateModel" enctype="multipart/form-data">
		<fieldset>
			<br>
			<div class="pure-g">
				<div class="pure-u-1-2">Evaluate data models</div>
				<div class="pure-u-1-2">
					<button type="submit" class="pure-button pure-button-primary">Select</button>
				</div>
			</div>
			<br>
		</fieldset>
	</form>
	
	<form class="pure-form pure-form-stacked" method="get" action="Users"
		enctype="multipart/form-data">
		<fieldset>
			<legend>User Tools</legend>
			<br>
			<div class="pure-g">
				<p></p>
				<div class="pure-u-1-2">Users</div>
				<div class="pure-u-1-2">
					<button type="submit" class="pure-button pure-button-primary">Select</button>
				</div>
			</div>
			<p></p>
			<div></div>
		</fieldset>
	</form>


</body>
</html>