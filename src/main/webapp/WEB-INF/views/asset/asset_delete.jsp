<jsp:root
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:form="http://www.springframework.org/tags/form"
    xmlns:s="http://www.springframework.org/tags"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns="http://www.w3.org/1999/xhtml"
    version="2.0">

<p><strong>Warning</strong> - you are about to delete the following asset.</p>
<p>This will delete all files associated with this asset</p>

<form method="post">

<jsp:include page="/WEB-INF/views/asset/asset_view.jsp" />

<div class="glue">
    <input type="submit" value="Confirm deletion" />
</div>

</form>

</jsp:root>