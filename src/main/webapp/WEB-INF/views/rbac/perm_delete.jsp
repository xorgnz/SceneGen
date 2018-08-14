<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:form="http://www.springframework.org/tags/form"
    xmlns:s="http://www.springframework.org/tags"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns="http://www.w3.org/1999/xhtml"
    version="2.0">

<p><strong>Warning</strong> - you are about to delete the following permission.</p>
<p>This will delete the permission from all roles and users who have been granted it. Note that deletion of a permission may render some portions of the system inaccessible until the permission is re-created and reassigned.</p> 

<form method="post">

<jsp:include page="/WEB-INF/views/rbac/perm_view.jsp" />

<div class="glue">
    <input type="submit" value="Confirm deletion" />
</div>

</form>

</jsp:root>