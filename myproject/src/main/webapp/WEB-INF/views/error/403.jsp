<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%response.setStatus(403);%>
<!doctype html>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>403-禁止访问</title>
		<style>
			html {
				font-size: 625%;
			}
			
			body,
			html {
				width: 100%;
				height: 100%;
			}
			
			body {}
			
			body {
				margin: 0;
				padding: 0;
				font-family: Microsoft YaHei;
				font-size: 18px;
				font-size: .18rem;
				background-size: cover;
				background-attachment: fixed;
			}
			
			.box {
				width: 100%;
				background: #fff;
				width: 100%;
				height: 100%;
				margin: auto;
			}
			
			.bc {
				background: url(/static/img/403.png) no-repeat center 1rem;
				width: 5rem;
				height: 100%;
				margin: auto;
				background-size: 120%;
			}
		</style>
	</head>

	<body>
		<div class="box">
			<div class="bc">

			</div>
		</div>
	</body>

</html>