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
    <label>ID</label>               <input type="text" value="${user.id}"           disabled="disabled" /><br/>
    <label>Username</label>         <input type="text" value="${user.username}"     disabled="disabled" /><br/>
    <label>Email</label>            <input type="text" value="${user.email}"        disabled="disabled" /><br/>
    <label>First Name</label>       <input type="text" value="${user.firstName}"    disabled="disabled" />
    <label>Last Name</label>        <input type="text" value="${user.lastName}"     disabled="disabled" /><br/>
</fieldset>
</div>

<h3>Role Assignments</h3>
<div class="glue">

<!-- Form - Assign to new Role -->
<c:choose>
<c:when test="${fn:length(allRoles) > 0}">
<form:form method="post" action="rbac/user_role_link/assign">
    <fieldset>
        <select name="roleId">
        <c:forEach var="role" items="${allRoles}">
            <option value="${role.id}">${role.name}</option>
        </c:forEach>
        </select>
        
        <input type="hidden" name="userId" value="${user.id}" />
        <button type="submit">Assign this Role</button>
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
        <a href="rbac/user_role_link/deassign/${user.id}/${role.id}">Deassign</a>
    </td>
</tr>
</c:forEach>    

</table>
</div>

<h3>User Group Assignments</h3>
<div class="glue">

<!-- Form - Assign to new User Group -->
<c:choose>
<c:when test="${fn:length(allGroups) > 0}">
<form:form method="post" action="rbac/usergrp_user_link/assign">
    <fieldset>
        <select name="userGroupId">
        <c:forEach var="usergrp" items="${allGroups}">
            <option value="${usergrp.id}">${usergrp.name}</option>
        </c:forEach>
        </select>
        
        <input type="hidden" name="userId" value="${user.id}" />
        <button type="submit">Assign to this Group</button>
    </fieldset>
</form:form>
</c:when>
</c:choose>

<!-- List of assigned groups -->
<table class="list full">

<thead>
    <th width="200">Name</th>
    <th>Description</th>
    <th width="135">&#160;</th>
</thead>

<c:forEach var="usergrp" items="${groups}">
<tr>
    <td><a href="rbac/usergrp/${usergrp.id}" target="frame-main">${usergrp.name}</a></td>
    <td>${usergrp.description}</td>
    <td>
        <a href="rbac/usergrp/${usergrp.id}/edit" target="frame-main">Edit</a>&#160;
        <a href="rbac/usergrp/${usergrp.id}/delete" target="frame-main">Delete</a>&#160;
        <a href="rbac/usergrp_user_link/deassign/${usergrp.id}/${user.id}">Deassign</a>
    </td>
</tr>
</c:forEach>    

</table>
</div>

</jsp:root>