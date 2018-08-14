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
        <legend>Curriculum Item</legend>
        
        <label>Name</label>
        <div>
            <input type="text" value="${curriculumItem.name}" disabled="disabled" />
        </div>
        
        <label>Weight</label>
        <div>
            <input type="text" value="${curriculumItem.weight}" disabled="disabled" />
        </div>
                
        <label>Description</label>
        <div>
            <textarea disabled="disabled">${curriculumItem.description}</textarea>
        </div>
        <br/>
    </fieldset>
</div>

<form:form method="post" modelAttribute="co">

<div class="glue">
    <fieldset>
        <legend>Select Asset Set</legend>
        
        <label>Asset Set</label>
        <select name="assetSetId">
            <c:forEach var="assetSet" items="${assetSets}">
            <c:choose>
                <c:when test="${assetSet.id == co.assetSetId}"><option value="${assetSet.id}" selected="selected">${assetSet.name}</option></c:when>
                <c:otherwise><option value="${assetSet.id}">${assetSet.name}</option></c:otherwise>
            </c:choose>
            </c:forEach>
        </select>
    </fieldset>
    
    <input name="stage" value="1" type="hidden" />
</div>

<div class="glue">
    <button type="submit">Submit</button>
</div>

</form:form>

</jsp:root>
