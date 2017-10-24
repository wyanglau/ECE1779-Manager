<%@page import="com.amazonaws.services.cloudwatch.model.Datapoint"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="ece1779.GlobalValues"%>
<%@page import="ece1779.loadBalance.*"%>
<%@page import="ece1779.commonObjects.*"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Timer"%>
<%@page import="com.amazonaws.services.cloudwatch.model.Datapoint"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js"></script>
<title>Manager View</title>
<style type="text/css">
.deleteAll {
	-moz-box-shadow: inset 0px 1px 0px 0px #f29c93;
	-webkit-box-shadow: inset 0px 1px 0px 0px #f29c93;
	box-shadow: inset 0px 1px 0px 0px #f29c93;
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0.05, #fe1a00
		), color-stop(1, #ce0100));
	background: -moz-linear-gradient(top, #fe1a00 5%, #ce0100 100%);
	background: -webkit-linear-gradient(top, #fe1a00 5%, #ce0100 100%);
	background: -o-linear-gradient(top, #fe1a00 5%, #ce0100 100%);
	background: -ms-linear-gradient(top, #fe1a00 5%, #ce0100 100%);
	background: linear-gradient(to bottom, #fe1a00 5%, #ce0100 100%);
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#fe1a00',
		endColorstr='#ce0100', GradientType=0);
	background-color: #fe1a00;
	-moz-border-radius: 6px;
	-webkit-border-radius: 6px;
	border-radius: 6px;
	border: 1px solid #d83526;
	display: inline-block;
	cursor: pointer;
	color: #ffffff;
	font-family: arial;
	font-size: 15px;
	font-weight: bold;
	padding: 7px 69px;
	text-decoration: none;
	text-shadow: 0px 1px 0px #b23e35;
}

.deleteAll:hover {
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0.05, #ce0100
		), color-stop(1, #fe1a00));
	background: -moz-linear-gradient(top, #ce0100 5%, #fe1a00 100%);
	background: -webkit-linear-gradient(top, #ce0100 5%, #fe1a00 100%);
	background: -o-linear-gradient(top, #ce0100 5%, #fe1a00 100%);
	background: -ms-linear-gradient(top, #ce0100 5%, #fe1a00 100%);
	background: linear-gradient(to bottom, #ce0100 5%, #fe1a00 100%);
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ce0100',
		endColorstr='#fe1a00', GradientType=0);
	background-color: #ce0100;
}

.deleteAll:active {
	position: relative;
	top: 1px;
}

.divSur {
	border-style: solid;
	width: 650px;
}

.reloadbtn {
	background-color: #44c767;
	-moz-border-radius: 4px;
	-webkit-border-radius: 4px;
	border-radius: 4px;
	border: 1px solid #18ab29;
	display: inline-block;
	cursor: pointer;
	color: #ffffff;
	font-family: Trebuchet MS;
	font-size: 17px;
	padding: 3px 51px;
	text-decoration: none;
	text-shadow: 0px 1px 0px #2f6627;
}

.reloadbtn:hover {
	background-color: #5cbf2a;
}

.reloadbtn:active {
	position: relative;
	top: 1px;
}

.logoutBtn {
	background-color: #f50000;
	-moz-border-radius: 4px;
	-webkit-border-radius: 4px;
	border-radius: 4px;
	border: 1px solid #ab1919;
	display: inline-block;
	cursor: pointer;
	color: #ffffff;
	font-family: Trebuchet MS;
	font-size: 17px;
	padding: 3px 51px;
	text-decoration: none;
	text-shadow: 0px 1px 0px #2f6627;
}

.logoutBtn:hover {
	background-color: #bd2a2a;
}

.logoutBtn:active {
	position: relative;
	top: 1px;
}
</style>
</head>

<body>
	<script type="text/javascript">
		function deleteAllData() {
			document.getElementById("deleteAll").disabled = true;
			if (window
					.confirm('Attention: This function allows you to delete all image and user data in S3 and database. Operation is Irreversible.')) {
				$
						.ajax({
							type : "POST",
							url : "../DeleteServlet",
							data : {},
							success : function(result) {
								if (result == "SUCCESS") {
									alert("Data has been deleted!")
								} else {
									alert("Error incured, try again later. Error message :"
											+ result);
								}
								document.getElementById("deleteAll").disabled = false;
							}
						})
				return true;
			} else {
				document.getElementById("deleteAll").disabled = false;
				return false;
			}
		}
		function manualGrow() {
			document.getElementById("growbutton").disabled = true;
			$
					.ajax({
						type : "POST",
						url : "../ManualGrowServlet",
						data : {
							manualGrowRatio : $("#manualGrowRatio").val()
						},
						success : function(result) {
							if (result == "SUCCESS") {
								alert("Successful Operation. Please wait for the startup of instances.")
							} else if (result == "INVALID_PARAMETER") {
								alert("Invalid input of ratio, only integer larger than 1 acceptable.")
							} else {
								alert("AWS Error incured, try again later. Error message :"
										+ result);
							}
							document.getElementById("growbutton").disabled = false;
						}
					})
		}
		function manualShrink() {
			document.getElementById("shrinkbutton").disabled = true;
			$
					.ajax({
						type : "POST",
						url : "../ManualShrinkServlet",
						data : {
							manualShrinkRatio : $("#manualShrinkRatio").val()
						},
						success : function(result) {
							if (result == "SUCCESS") {
								alert("Successful Operation. Please wait for the shutdown of instances.")
							} else if (result == "INVALID_PARAMETER") {
								alert("Invalid input of ratio, only integer larger than 1 acceptable.")
							} else {
								alert("AWS Error incured, try again later. Error message :"
										+ result);
							}
							document.getElementById("shrinkbutton").disabled = false;
						}
					})
		}
		function stopAutoScaling() {
			$.ajax({
				type : "POST",
				url : "../StopAutoScadulingServlet",
				success : function(result) {
					document.getElementById("stopAuto").disabled = true;
					document.getElementById("startAuto").disabled = false;
				}
			})
		}

		function startAutoScaling() {
			document.getElementById("stopAuto").disabled = false;
			document.getElementById("startAuto").disabled = true;
			$
					.ajax({
						type : "POST",
						url : "../StartAutoScadulingServlet",
						data : {
							expandThreshlod : $("#expandThreshlod").val(),
							shrinkThreshlod : $("#shrinkThreshlod").val(),
							growRatio : $("#growRatio").val(),
							shrinkRatio : $("#growRatio").val()
						},
						success : function(result) {

							if (result == "SUCCESS") {
								alert("Auto-Scaling started up.");
							} else if (result == "INVALID_PARAMETER") {
								alert("Invalid parameters.Required Pattern: 100 > Growing-Threshold > Shrinking-Threshold > 0 , Ratios should be integers and > 2")
								document.getElementById("stopAuto").disabled = true;
								document.getElementById("startAuto").disabled = false;
							} else {
								alert("Error incured while starting auto-scaling. Error message :"
										+ result);
								document.getElementById("stopAuto").disabled = true;
								document.getElementById("startAuto").disabled = false;
							}
						}
					})
		}
		function reload() {
			document.getElementById("reloadButton").disabled = true;
			window.location.reload();
		}
		function logout() {
			document.upload.action = getRootPath() + "servlets/LogoutServlet";
			document.upload.submit();

		}
		function getRootPath() {
			var pathName = window.location.pathname.substring(1);
			var webName = pathName == '' ? '' : pathName.substring(0, pathName
					.indexOf('/'));
			return window.location.protocol + '//' + window.location.host + '/'
					+ webName + '/';
		}
	</script>
	<%
		Timer timer = (Timer) session.getAttribute("Timer");

		String stopTag = "";
		String startTag = "";
		if (timer == null) {
			startTag = "";
			stopTag = "disabled=disabled";
		} else {
			stopTag = "";
			startTag = "disabled=disabled";
		}

		CloudWatching cw = (CloudWatching) session
				.getAttribute(GlobalValues.CLOUD_WATCHING);

		List<CloudWatcher> watchers = cw.getCPUUtilization();
	%>
	<div align=center>
		<font size=20px><b>Manager View</b></font> <br>
		<form
			action=<%=response.encodeURL(request.getContextPath()
					+ "/servlets/LogoutServlet")%>
			method=POST>
			<input type=button class="reloadbtn" onclick="reload()"
				id=reloadButton value=Reload>&nbsp;&nbsp;<input type=submit
				class="logoutBtn" value=Logout>
		</form>
		<br>
	</div>
	<div align=center>
		<table>
			<tr>
				<td><b><%=watchers.size()%> of <%=cw.getAllEC2instances().size() - 1%>
						workers are running.</b></td>
			</tr>
		</table>

		<div class=divSur>
			<table>
				<%
					for (CloudWatcher watcher : watchers) {
				%>
				<tr>
					<td><h3>
							<b>Instance ID</b>:
							<%=watcher.getInstanceId()%></h3></td>
				</tr>
				<%
					for (Datapoint datapoint : watcher.getDatapoints()) {
				%>
				<tr>
					<td><b>Time Stamp:</b><%=datapoint.getTimestamp()%></td>
					<td></td>
					<td></td>
					<td><b>CPU Utilization</b>:<%=datapoint.getMaximum()%>%</td>
				</tr>
				<%
					}
					}
				%>
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<tr></tr>
			</table>
		</div>
	</div>
	<form name=display action="" method=POST>
		<table align=center>
			<tr>
				<td><h3>
						<u>Manual Scaling</u>
					</h3></td>
			</tr>
			<tr>

				<td>Shrinking Ratio:</td>
				<td><input type=text value=2 name=manualShrinkRatio
					id=manualShrinkRatio></td>
				<td><input type=button value=shrink id=shrinkbutton
					onclick="manualShrink()"></td>
			</tr>
			<tr>
				<td>Growing Ratio:</td>
				<td><input type=text value=2 name=manualGrowRatio
					id=manualGrowRatio></td>
				<td><input type=button value=grow id=growbutton
					onclick="manualGrow()"></td>
			</tr>

			<tr>
				<td><h3>
						<u>Auto Scaling Configuration</u>
					</h3></td>
			</tr>

			<tr>
				<td>CPU Utilization Threshold:</td>
			</tr>
			<tr>
				<td>Growing Threshold: <input type=text value=60
					name=expandThreshlod id=expandThreshlod>(%)
				</td>
				<td>Ratio: <input type=text value=2 name=growRatio id=growRatio></td>
				<td><input id=startAuto type=button value=start
					onclick="startAutoScaling()" <%=startTag%>></td>
			</tr>
			<tr>
				<td>Shrinking Threshold: <input type=text value=30
					name=shrinkThreshlod id=shrinkThreshlod>(%)
				</td>
				<td>Ratio: <input type=text value=2 name=shrinkRatio
					id=shrinkRatio></td>
				<td><input type="button" id=stopAuto value=stop <%=stopTag%>
					onclick="stopAutoScaling()"></td>
			</tr>
			<tr>
				<td><h3>
						<u>Delete All Data</u>
					</h3></td>
			</tr>
			<tr>
				<td>Delete all images and user data stored in S3 and database.</td>
			</tr>
			<tr>
				<td><input type=button id=deleteAll class=deleteAll
					value="Delete All Data" onclick="deleteAllData()"></td>
			</tr>
		</table>
	</form>


</body>
</html>