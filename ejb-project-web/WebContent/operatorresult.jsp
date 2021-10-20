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
<script src="js/operatorheader.js" type="text/javascript" defer></script>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
<script language = "javascript" type = "text/javascript">
var wsUri = "ws://localhost:8080/ejb-project-web/createsocket";
var output;
var start = 0;
var id=0;
	
function init() {
   	output = document.getElementById("output")
   	fnWebSocket()
   
    id = ${id}

   	callMachine()
}

function callMachine(){

	var xhr = new XMLHttpRequest();

	xhr.addEventListener("readystatechange", function() {
	  var data = parseInt(this.response)
	  if(this.readyState === 4 && !isNaN(data)) {
		  if (data == 2){
	   			writeToScreen("Machining complete!", true)
	   			xhr.abort()
				start = 0
		  } else if (data == 1){
				writeToScreen("Tool warn!")
				writeToScreen("Machining complete!", true)
				xhr.abort()
		  } else{
				writeToScreen("Tool ok! step:" + start/100, false)
				start = start + 100
				callMachine()
		  }
		}
	  
	});

	var url = "http://localhost:8084/predict?id=" + id + "&start=" + start
	xhr.open("POST", url);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.setRequestHeader('Access-Control-Allow-Origin', '*')

	xhr.send();
	         

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
   writeToScreen("Waiting for machine...", true);
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
	
function writeToScreen(message, append) {
   var pre = document.createElement("p"); 
   pre.innerHTML = message; 
   if (append)
   		output.appendChild(pre);
   else{
	   output.firstElementChild.innerHTML = message
   }
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
	<%String name = (String) request.getAttribute("id"); %>
<h1>Realtime tool status from Machine <%= name %></h1>
<br/>

<div id = "output"></div> 

</body>

</html>

