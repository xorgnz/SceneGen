<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:form="http://www.springframework.org/tags/form"
    xmlns:s="http://www.springframework.org/tags"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns="http://www.w3.org/1999/xhtml"
    version="2.0">

<table class="list side">

<thead>
    <th width="140">Name</th>
    <th width="80">&#160;</th>
</thead>

<c:forEach var="o" items="${objs}">
<tr>
    <td>${o.name}</td>
    <td align="right">
        <a href="scene/${o.id}/build" target="sceneBuilder_${o.id}">Build</a>&#160;
        <a href="scene/${o.id}/delete" target="frame-main">Delete</a>&#160;
        <a href="scene/${o.id}" target="scene_${o.id}">View</a>&#160;
        <a href="scene/${o.id}/zip">Zip</a>&#160;
    </td>
</tr>
</c:forEach>    

</table>

</jsp:root>