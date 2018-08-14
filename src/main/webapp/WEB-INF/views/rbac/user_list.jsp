<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:form="http://www.springframework.org/tags/form"
    xmlns:s="http://www.springframework.org/tags"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns="http://www.w3.org/1999/xhtml"
    version="2.0">

<a href="rbac/user/add" target="frame-main">Add New User</a><br/><br/>

<table class="list side">

<thead>
    <th>Name</th>
    <th width="80">&#160;</th>
</thead>

<c:forEach var="o" items="${users}">
<tr>
    <td><a href="rbac/user/${o.id}" target="frame-main">${o.username}: ${o.firstName} ${o.lastName}</a></td>
    <td align="right">
        <a href="rbac/user/${o.id}/edit" target="frame-main">Edit</a>&#160;
        <a href="rbac/user/${o.id}/delete" target="frame-main">Delete</a>
    </td>
</tr>
</c:forEach>    

</table>

</jsp:root>