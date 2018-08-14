<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:form="http://www.springframework.org/tags/form"
    xmlns:s="http://www.springframework.org/tags"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns="http://www.w3.org/1999/xhtml" 
    version="2.0">

<form:form method="post" modelAttribute="co" enctype="multipart/form-data">

<s:bind path="*">
    <c:if test="${status.error}">
        <div class="msg_bad">Form has errors</div>
    </c:if>
</s:bind>
    
<div class="glue">
    <fieldset>
        <legend>General Info</legend>
        
        <label>Asset Set</label>            
        <input type="text" value="${co.asset.assetSet.name}"     disabled="disabled"/><br/>
        
        <label>Name</label>
        <div>
            <form:input path="asset.name"/><br/>
            <form:errors path="asset.name" cssClass="error" />
        </div>

        <label>Entity ID</label>
        <div>
            <form:input path="asset.entityId"/><br/>
            <form:errors path="asset.entityId" cssClass="error" />
        </div>
        
        <label>File</label>
        <div>
            <input type="file" name="file" /><br/>
            <form:errors path="file" cssClass="error" />
            <c:if test="${not empty co.asset.x3dFilename}">
                <b>Current X3D file:</b> ${co.asset.x3dFilename}<br/>
            </c:if> 
            <c:if test="${not empty co.asset.objFilename}">
                <b>Current OBJ file:</b> ${co.asset.objFilename}<br/>
            </c:if>
        </div>
                
    </fieldset>
</div>

<div class="glue">
    <button type="submit">Submit</button>
</div>

<input type="hidden" name="asset.assetSet.id" value="${co.asset.assetSet.id}" />

</form:form>

</jsp:root>