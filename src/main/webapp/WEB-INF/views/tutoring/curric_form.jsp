<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:form="http://www.springframework.org/tags/form"
    xmlns:s="http://www.springframework.org/tags"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns="http://www.w3.org/1999/xhtml" 
    version="2.0">


<form:form method="post" commandName="co" cssClass="cleanform">

<s:bind path="*">
    <c:if test="${status.error}">
        <div class="msg_bad">Form has errors</div>
    </c:if>
</s:bind>
    
<div class="glue">
    <fieldset>
        <legend>General Info</legend>
        
        <form:label path="name">Name</form:label>
        <div>
            <form:input path="name"/><br/>
            <form:errors path="name" cssClass="error" />
        </div>
                
        <form:label path="creatorName">Creator</form:label>
        <div>
            <form:input path="creatorName"/><br/>
            <form:errors path="creatorName" cssClass="error" />
        </div>
                        
        <form:label path="description">Description</form:label>
        <div>
            <form:textarea path="description"/><br/>
            <form:errors path="description" cssClass="error" />
        </div>
        <br/>
        
    </fieldset>
</div>

<div class="glue">
    <button type="submit">Submit</button>
</div>

</form:form>

</jsp:root>