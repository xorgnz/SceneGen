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
    <th>Name</th>
    <th width="130">&#160;</th>
</thead>

<c:forEach var="o" items="${templates}">
<tr>
    <td><a href="activity/template/${o.id}" target="frame-main">${o.name}</a></td>
    <td align="right">
        <a href="activity/template/${o.id}/edit" target="frame-main">Edit</a>&#160;
        <a href="activity/template/${o.id}/delete" target="frame-main">Delete</a>&#160;
    </td>
</tr>
</c:forEach>    

</table>

</jsp:root>