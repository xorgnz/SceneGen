<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:form="http://www.springframework.org/tags/form"
    xmlns:s="http://www.springframework.org/tags"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns="http://www.w3.org/1999/xhtml" 
    version="2.0">


<p>Your scene '${scene.name}' has been created.</p> 

<p>The next step is to <a href="scene/${scene.id}/build" target="_blank">open the scene builder and configure it</a>.</p> 

</jsp:root>