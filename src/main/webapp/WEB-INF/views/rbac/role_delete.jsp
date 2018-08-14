<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:form="http://www.springframework.org/tags/form"
    xmlns:s="http://www.springframework.org/tags"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns="http://www.w3.org/1999/xhtml"
    version="2.0">

<p><strong>Warning</strong> - you are about to delete the following role.</p>
<p>This will remove the role from all users who have it assigned to them. Note that any permissions assigned to a role are not deleted and can be re-assigned to different roles.</p> 

<form method="post">

<jsp:include page="/WEB-INF/views/rbac/role_view.jsp" />

<div class="glue">
    <input type="submit" value="Confirm deletion" />
</div>

</form>

</jsp:root>