<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:form="http://www.springframework.org/tags/form"
    xmlns:s="http://www.springframework.org/tags"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns="http://www.w3.org/1999/xhtml" 
    version="2.0">

<form:form method="post">

<div class="glue">
    <fieldset>
        <c:forEach var="p" items="${query.parameters}">
        <label>${p.label}</label>
        <c:choose>
        <c:when test="${p.tags.contains('entityName')}">
            <input type="text" name="qparam_${p.variable}" class="fma_text_completion" />
        </c:when>
        <c:otherwise>
            <input type="text" name="qparam_${p.variable}" />
        </c:otherwise>
        </c:choose>
        </c:forEach>
    </fieldset>
</div>

<div class="glue">
    <button type="submit">Submit</button>
</div>

</form:form>

</jsp:root>