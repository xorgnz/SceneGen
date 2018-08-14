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
    <label>ID</label>               <input type="text" value="${obj.id}"           disabled="disabled"/>
    <label>Name</label>             <input type="text" value="${obj.name}"         disabled="disabled" />
    <label>Tags</label>             <input type="text" value="${obj.tags}"         disabled="disabled" />
    <label>Description</label>     <textarea disabled="disabled">${query.description}</textarea>
</fieldset>
</div>

</jsp:root>