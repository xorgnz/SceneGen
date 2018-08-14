<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
    
    <c:url var="base_url" value="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />

    <base href="${base_url}" />
</head>
<body>
<div style="background-color:#f00; color:#fff">
<h1 style="">Access Denied</h1>
</div>

${request.name}

</body>
</html>
