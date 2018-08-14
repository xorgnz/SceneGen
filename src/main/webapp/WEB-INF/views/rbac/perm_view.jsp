<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:form="http://www.springframework.org/tags/form"
    xmlns:s="http://www.springframework.org/tags"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns="http://www.w3.org/1999/xhtml"
    version="2.0">

<div class="glue">
<fieldset>
    <legend>General Info</legend>
    <label>ID</label>               <input type="text" value="${permission.id}"           disabled="disabled" />
    <label>Name</label>             <input type="text" value="${permission.name}"         disabled="disabled" /><br/>
    <label>Description</label>      <textarea disabled="disabled">${permission.description}</textarea>
</fieldset>
</div>

<h3>Role Assignments</h3>
<div class="glue">

<!-- Form - Assign to new Role -->
<c:choose>
<c:when test="${fn:length(allRoles) > 0}">
<form:form method="post" action="rbac/role_perm_link/assign">
    <fieldset>
        <select name="roleId">
        <c:forEach var="role" items="${allRoles}">
            <option value="${role.id}">${role.name}</option>
        </c:forEach>
        </select>
        
        <input type="hidden" name="permId" value="${permission.id}" />
        <button type="submit">Assign to this Role</button>
    </fieldset>
</form:form>
</c:when>
</c:choose>

<!-- List of assigned roles -->
<table class="list full">

<thead>
    <th width="200">Name</th>
    <th>Description</th>
    <th width="135">&#160;</th>
</thead>

<c:forEach var="role" items="${roles}">
<tr>
    <td><a href="rbac/role/${role.id}" target="frame-main">${role.name}</a></td>
    <td>${role.description}</td>
    <td>
        <a href="rbac/role/${role.id}/edit" target="frame-main">Edit</a>&#160;
        <a href="rbac/role/${role.id}/delete" target="frame-main">Delete</a>&#160;
        <a href="rbac/role_perm_link/deassign/${role.id}/${permission.id}">Deassign</a>
    </td>
</tr>
</c:forEach>    

</table>
</div>

</jsp:root>