<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ page import="java.io.*,java.util.*,org.springframework.web.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
if(exception == null) {
	exception = (Exception) request.getAttribute("org.springframework.web.servlet.DispatcherServlet.EXCEPTION");
}
Throwable e = exception;
if(e instanceof org.springframework.web.util.NestedServletException) {
	e = ((org.springframework.web.util.NestedServletException) e).getRootCause();
}
if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
%>
<jsp:include page="/WEB-INF/views/error/data_integrity_violation_exception.jsp" />
<%
} else if(e instanceof org.apache.shiro.authz.UnauthorizedException) {
%>
<jsp:include page="/WEB-INF/views/error/403.jsp" />
<%
} else if(e instanceof com.course.core.support.DeleteException) {
%>
<jsp:include page="/WEB-INF/views/error/delete_exception.jsp" />
<%
} else if(e instanceof com.course.core.support.CmsException) {
%>
<jsp:include page="/WEB-INF/views/error/cms_exception.jsp" />
<%
} else {
%>
<!doctype html>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>500-服务器内部错误</title>
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
				background: url(/static/img/500.png) no-repeat center 1rem;
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

		<div id="excptions" class="exceptions" style="display:none;">
			<pre class="exception"><%
		//exception.printStackTrace();
		//ByteArrayOutputStream ostr = new ByteArrayOutputStream();
		//exception.printStackTrace(new PrintStream(ostr));
		//out.print(ostr);
		
		ByteArrayOutputStream ostr = new ByteArrayOutputStream();
		PrintStream s = new PrintStream(ostr);
		if(exception != null) {
			s.println(HtmlUtils.htmlEscape(exception.toString()));
	    StackTraceElement[] trace = exception.getStackTrace();
	    for (int i=0; i < trace.length; i++) {
	        s.println("\tat " + HtmlUtils.htmlEscape(trace[i].toString()));
	    }
	    Throwable ourCause = exception.getCause();
	    while(ourCause!=null) {
	    	s.println("Caused by: " + HtmlUtils.htmlEscape(ourCause.toString()));
	    	trace = ourCause.getStackTrace();
	      for (int i=0; i < trace.length; i++) {
	          s.println("\tat " + HtmlUtils.htmlEscape(trace[i].toString()));
	      }
	    	ourCause = ourCause.getCause();
	    }			
		} else {
			s.println(HtmlUtils.htmlEscape((String) request.getAttribute("javax.servlet.error.message")));
		}
    out.print(ostr);
	
	
	%></pre>
		</div>
	</body>

</html>

<%
}
%>