<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Blank Side</title>
    
    <c:url var="base_url" value="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />

    <base href="${base_url}" />
    
</head>
<body>

<pre>
Side blank
</pre>
</body>
</html>
