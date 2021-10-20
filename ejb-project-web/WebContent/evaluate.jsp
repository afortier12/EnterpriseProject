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
<script language = "javascript" type = "text/javascript">
var wsUri = "ws://localhost:8080/ejb-project-web/createsocket";
var output;
	
function init() {
   output = document.getElementById("output");
   fnWebSocket();
}
	
function fnWebSocket() {
   websocket = new WebSocket(wsUri);
		
   websocket.onopen = function(evt) {
      onOpen(evt)
   };	
   websocket.onmessage = function(evt) {
      onMessage(evt)
   };
   websocket.onerror = function(evt) {
      onError(evt)
   };
}
	
function onOpen(evt) {

}

function onMessage(evt) {
	if (evt.data == "evaluate"){

	} else {
		writeToScreen(evt.data);
	}
}

function onError(evt) {
   writeToScreen(evt.data);
}
	
function writeToScreen(message) {
   var pre = document.createElement("p"); 
   pre.innerHTML = message; 
   output.appendChild(pre);
}
	
window.addEventListener("load", init, false);
	
</script>

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
<h1>Tool Management - Evaluations</h1>
<br/>
<form class="pure-form pure-form-stacked" method="get"
		action="/ejb-project-web/rest/evaluate" >
    <fieldset>
        <legend>Method</legend>
            <div class="pure-u-1 pure-u-md-1-3">
                <label for="method">Choose a Method</label>
                <select id="method" name="method" class="Choose the modelling method:">
                    <option>Support Vector Machine (SVM)</option>
                    <option>K-Nearest Neighbours (kNN)</option>
                    <option>Decision Tree</option>
                </select>
            </div>
        </div>

        <button type="submit" class="pure-button pure-button-primary">Submit</button>
    </fieldset>
        
</form>

<div id = "output"></div> 

</body>
</html>

