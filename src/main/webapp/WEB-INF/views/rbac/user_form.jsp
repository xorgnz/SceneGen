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
        
        <form:label path="user.username">User Name</form:label>
        <div>
            <form:input path="user.username"/><br/>
            <form:errors path="user.username" cssClass="error" />
            <s:bind path="co.user">
                <c:forEach items="${status.errorCodes}" var="code" varStatus="varStatus">
                    <c:if test="${code == 'UniqueUsername'}"><span class="error">${status.errorMessages[varStatus.index]}</span></c:if>
                </c:forEach>
            </s:bind>
        </div>
        <hr />
                
        <form:label path="passwords.password">Password</form:label>
        <div>
            <form:input type="password" path="passwords.password"/><br/>
            <form:errors path="passwords.password" cssClass="error" />
            <form:errors path="passwords" cssClass="error" />
        </div>
        
        <form:label path="passwords.passwordConfirm">Confirm Password</form:label>
        <div>
            <form:input type="password" path="passwords.passwordConfirm"/><br/>
            <form:errors path="passwords.passwordConfirm" cssClass="error" />
        </div>
        <hr />

        <form:label path="user.email">Email</form:label>
        <div>
            <form:input path="user.email"/><br/>
            <form:errors path="user.email" cssClass="error" />
            <s:bind path="co.user">
                <c:forEach items="${status.errorCodes}" var="code" varStatus="varStatus">
                    <c:if test="${code == 'UniqueUserEmail'}"><span class="error">${status.errorMessages[varStatus.index]}</span></c:if>
                </c:forEach>
            </s:bind>
        </div>
        
        <form:label path="user.firstName">First Name</form:label>
        <div>
            <form:input path="user.firstName"/><br/>
            <form:errors path="user.firstName" cssClass="error" />
        </div>
                
        <form:label path="user.lastName">Last Name</form:label>
        <div>
            <form:input path="user.lastName"/><br/>
            <form:errors path="user.lastName" cssClass="error" />
        </div>
    </fieldset>
</div>

<div class="glue">
    <button type="submit">Submit</button>
</div>

</form:form>

</jsp:root>