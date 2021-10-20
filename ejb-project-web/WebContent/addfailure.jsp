<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="java.util.Iterator"
    import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" href="https://unpkg.com/purecss@2.0.3/build/pure-min.css">
 <style>
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
	p { text-align: center }
}
	
</style>
<script src="js/header.js" type="text/javascript" defer></script>
<title>Tool Management - Administration</title>

</head>
<body> 
<h1>Tool Monitoring - User Administration</h1>
<br/>
<p><font color=red>Failed to add user.  Please try again.</font></p>
<br/>
<% 
String error = (String) request.getAttribute("error"); 
if(error != null) {%>
    <p>${error}</p>
<%}%>
<p><a href="javascript:history.back()">Go Back</a></p>
</body>
</html>

