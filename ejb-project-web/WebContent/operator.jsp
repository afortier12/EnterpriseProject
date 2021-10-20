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
<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
<script src="js/operatorheader.js" type="text/javascript" defer=""></script>
<title>Tool Life Management</title>

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
<h1>Choose machine to evaluate</h1>
<br/>
<form class="pure-form pure-form-stacked" method="post"
		action="/ejb-project-web/Operator" >
    <fieldset>
        <legend>Machine</legend>
            <div class="pure-u-1 pure-u-md-1-3">
                <label for="method">Choose a Machine</label>
                <select id="method" name="method" class="Choose the modelling method:">
                    <option>1</option>
                    <option>2</option>
                    <option>3</option>
                    <option>4</option>
                    <option>5</option>
                    <option>6</option>
                    <option>7</option>
                    <option>8</option>
                    <option>9</option>
                    <option>10</option>
                    <option>11</option>
                    <option>12</option>
                    <option>13</option>
                    <option>14</option>
                    <option>15</option>
                    <option>16</option>
                    <option>17</option>
                    <option>18</option>
                </select>
            </div>
        </div>

        <button type="submit" class="pure-button pure-button-primary">Submit</button>
    </fieldset>
        
</form>

<div id = "output"></div> 

</body>
</html>

