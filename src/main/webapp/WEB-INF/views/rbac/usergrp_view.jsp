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
    <label>ID</label>               <input type="text" value="${usergrp.id}"           disabled="disabled" />
    <label>Name</label>             <input type="text" value="${usergrp.name}"         disabled="disabled" /><br/>
    <label>Description</label>      <textarea disabled="disabled">${usergrp.description}</textarea>
</fieldset>
</div>

<h3>User Assignments</h3>
<div class="glue">

<!-- Form - Assign to new User -->
<c:choose>
<c:when test="${fn:length(allUsers) > 0}">
<form:form method="post" action="rbac/usergrp_user_link/assign">
    <fieldset>
        <select name="userId">
        <c:forEach var="user" items="${allUsers}">
            <option value="${user.id}">${user.username} (${user.firstName} ${user.lastName})</option>
        </c:forEach>
        </select>
        
        <input type="hidden" name="userGroupId" value="${usergrp.id}" />
        <button type="submit">Assign to this User</button>
    </fieldset>
</form:form>
</c:when>
</c:choose>

<!-- List of assigned users -->
<table class="list full">
<thead>
    <th width="120">Username</th>
    <th width="120">Name</th>
    <th>Email</th>
    <th width="135">&#160;</th>
</thead>
<c:forEach var="user" items="${users}">
<tr>
    <td><a href="rbac/user/${user.id}" target="frame-main">${user.username}</a></td>
    <td>${user.firstName} ${user.lastName}</td>
    <td>${user.email}</td>
    <td>
        <a href="rbac/user/${user.id}/edit" target="frame-main">Edit</a>&#160;
        <a href="rbac/user/${user.id}/delete" target="frame-main">Delete</a>&#160;
        <a href="rbac/usergrp_user_link/deassign/${usergrp.id}/${user.id}">Deassign</a>
    </td>
</tr>
</c:forEach>    

</table>
</div>  

</jsp:root>