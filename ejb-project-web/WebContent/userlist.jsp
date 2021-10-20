<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="java.util.Iterator"
    import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" href="https://unpkg.com/purecss@2.0.3/build/pure-min.css"
      integrity="sha384-cg6SkqEOCV1NbJoCu11+bm0NvBRc8IYLRGXkmNrqUBfTjmMYwNKPWBTIKyw9mHNJ" 
      crossorigin="anonymous">
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
	hidden>div {
		display:none;
	}

	visible>div {
		display:block;
	}
	input:out-of-range{
	   background: red;
	   color:#fff;
	}
}
	
</style>
<script src="js/header.js" type="text/javascript" defer></script>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
<script type="text/javascript">
$(function() {
    $('#addBtn').click(function(e) {
    	e.preventDefault();
    	$("#addForm").toggle();
    	$("#delForm").hide();
	});
    $('#delBtn').click(function(e) {
    	e.preventDefault();
    	$("#delForm").toggle();
    	$("#addForm").hide();
	});
});
</script>
<title>Tool Management - Administration</title>

</head>
<body> 
<h1>Tool Monitoring - User Administration</h1>
<br/>
<table class="pure-table users">
    <thead>
        <tr>
            <th>ID</th>
            <th>username</th>
            <th>role</th>
        </tr>
    </thead>

<%Iterator itr;%>
<%int count = 0;%>
<% List users= (List)request.getAttribute("users");
for (itr=users.iterator(); itr.hasNext(); )
{
	count++;%>
	<tr>
		<td><%=count%></td>
		<td><%=itr.next()%></td>
		<td><%=itr.next()%></td>
	</tr>
<%}%>  
</table>
<br/>
<%if (users.size() == 0){ %>
	<p>No users added</p>
<%}%> 
<br/>
<form class="pure-form pure-form-stacked m-lg">
    <fieldset>
        <div class="pure-g indent-sm">
    		<div class="pure-u-1-5">Add User</div>
	        <div class="pure-u-1-2"><button id="addBtn" class="pure-button pure-button-primary">Select</button></div>
    	</div>
    </fieldset> 
</form>
<form class="pure-form pure-form-stacked m-lg" >
    <fieldset>
        <div class="pure-g indent-sm">
    		<div class="pure-u-1-5">Remove User</div>
	        <div class="pure-u-1-2"><button id="delBtn" class="pure-button pure-button-primary">Select</button></div>
    	</div>
    </fieldset> 
</form>

<form id="addForm"  style="display:none" class="pure-form" method="post" action="AddUser">
	<legend>Enter username, password and role (1-administrator, 2-operator)</legend>
    <fieldset>
        <input type="text" name="username" placeholder="User name" />
        <input type="password" name="password" placeholder="Password" />
        <input type="number"  min="0" max="50" name="role" placeholder="Role" />
        <button type="submit" class="pure-button pure-button-primary">Add</button>
    </fieldset>
</form>


<form id="delForm" style="display:none" class="pure-form"  method="post" action="RemoveUser">
	<legend>Enter ID</legend>
    <fieldset>
        <input type="text" name="id" placeholder="ID" />
        <button type="submit" class="pure-button pure-button-primary">Delete</button>
    </fieldset>
</form>

</body>
</html>

