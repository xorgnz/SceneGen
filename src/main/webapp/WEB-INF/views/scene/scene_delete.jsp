<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:form="http://www.springframework.org/tags/form"
    xmlns:s="http://www.springframework.org/tags"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns="http://www.w3.org/1999/xhtml"
    version="2.0">

<p><strong>Warning</strong> - you are about to delete the following scene.</p>

<form method="post">

<div class="glue">
<fieldset>
    <legend>General Info</legend>
    <label>ID</label>                   <input type="text" value="${scene.id}"              disabled="disabled" />
    <label>Name</label>                 <input type="text" value="${scene.name}"            disabled="disabled" /><br/>
</fieldset>
</div>

<h3>Parts</h3>
<c:forEach var="part" items="${fragments}">
<div class="glue">
<fieldset>
    <legend>Scene Fragment</legend>
    <label>ID</label>                   <input type="text" value="${part.id}"               disabled="disabled" />
    <label>Name</label>                 <input type="text" value="${part.name}"             disabled="disabled" /><br/>
    <label>Stylesheet</label>           <input type="text" value="${part.stylesheet.name}"  disabled="disabled" />
    <label>Asset Set</label>            <input type="text" value="${part.assetSet.name}"    disabled="disabled" /><br/>
    <label>Query</label>                <input type="text" value="${part.query.name}"       disabled="disabled" />
    <label>Parameter</label>            <input type="text" value="${part.queryParamString}" disabled="disabled" /><br/>
</fieldset>
</div>
</c:forEach>

<div class="glue">
    <input type="submit" value="Confirm deletion" />
</div>

</form>

</jsp:root>