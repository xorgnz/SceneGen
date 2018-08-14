<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
    
    <c:url var="base_url" value="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />

    <base href="${base_url}" />
</head>
<body>
<h1>
	Hello world!
</h1>

${request.name}

<P>  The time on the server is ${serverTime}. </P>

<p><a href="admin">Admin pages</a></p>

</body>
</html>
