<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="ece1779.GlobalValues"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome to group14 Project!</title>
<style>
table {
	text-align: center;
}

.divSur {
	border-style: solid;
	border-width:2px;
	width: 350px
}
</style>
</head>
<body>

	<form method="post" action="servlets/LoginServlet">
		<center>
			<br> <br> <br> <br> <br> <br> <br>
			<br> <font size=9px><b>Manager Login</b></font><br><br>
			<div class=divSur>
				<br> <br>
				<table border="0" cellpadding="3">

					<tbody>
						<tr>
							<td><input type="text" name=<%=GlobalValues.USERNAME%>
								value="" placeholder="username" /></td>
						</tr>
						<tr>
							<td><input type="password" name=<%=GlobalValues.PASSWORD%>
								value="" placeholder="password" /></td>
						</tr>
						<tr></tr>
						<tr></tr>
						<tr>
							<td align="center"><input type="submit" value="Login" /></td>
						</tr>
					</tbody>
				</table>
				<br>
			</div>
		</center>
	</form>

	<br>

</body>
</html>