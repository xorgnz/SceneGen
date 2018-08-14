<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:form="http://www.springframework.org/tags/form"
    xmlns:s="http://www.springframework.org/tags"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns="http://www.w3.org/1999/xhtml"
    version="2.0">

<models>
<c:forEach var="obj2" items="${assets}">
<model>
    <name>${obj2.name}</name>
    <entityId>${obj2.entityId}</entityId>
    <file-x3d>${url_prefix}${obj2.x3dFilename}</file-x3d>
    <file-obj>${url_prefix}${obj2.objFilename}</file-obj>
    <pt_centroid>${obj2.stats.centroid_x} ${obj2.stats.centroid_y} ${obj2.stats.centroid_z}</pt_centroid>
    <pt_min>${obj2.stats.min_x} ${obj2.stats.min_y} ${obj2.stats.min_z}</pt_min>
    <pt_max>${obj2.stats.max_x} ${obj2.stats.max_y} ${obj2.stats.max_z}</pt_max>
</model>
</c:forEach>
</models>
</jsp:root>