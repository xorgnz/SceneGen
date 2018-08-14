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
        <input type="text" value="${co.assetSet.name}"     disabled="disabled"/><br/>
        
        <label>File</label>
        <div>
            <input type="file" name="file" /><br/>
            <form:errors path="file" cssClass="error" />
        </div>
                
    </fieldset>
</div>

<div class="glue">
    <button form="co" type="submit">Submit</button>
</div>

</form:form>

</jsp:root>