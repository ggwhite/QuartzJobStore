<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
		<!-- Latest compiled and minified CSS -->
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
		<!-- Optional theme -->
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
		<!-- Latest compiled and minified JavaScript -->
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
		
		<link rel="stylesheet" type="text/css" href="<c:url value="/static/css/main.css" />"/>
		<script src="<c:url value="/static/js/nmcjob-util.js" />"></script>
		<script src="<c:url value="/static/js/quartz-manager-helper.js" />"></script>
		
		<title>Scheduler Job List</title>
	</head>
	
	<body>
		<div class="container-fluid">
			<h1 class="page-header text-center">Scheduler Job List</h1>
			<table id="jobList" class="table table-hover table-striped">
				<thead>
					<tr>
						<th>Name</th>
						<th>Description</th>
						<th>State</th>
						<th>Last Execute Time</th>
						<th>Next Execute Time</th>
						<th><button id="refresh-btn" class="btn btn-info btn-control" action="refresh">Refresh</button></th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
		
		<script src="<c:url value="/static/js/views/quartz-management.js" />"></script>
	</body>
</html>