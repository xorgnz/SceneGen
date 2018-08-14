<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:form="http://www.springframework.org/tags/form"
    xmlns:s="http://www.springframework.org/tags"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
    xmlns="http://www.w3.org/1999/xhtml"
    version="2.0">

<div class="glue">
<table class="list full">

<thead>
    <th>Source</th>
    <th>Description</th>
    <th>Subject</th>
    <th>Relation</th>
    <th>Object</th>
    <th>&#916; P</th>
</thead>

<c:forEach var="o" items="${events}">
<tr>
    <td>${o.source}</td>
    <td>${o.description}</td>
    <td>${o.subjectEntityId}</td>
    <td>${o.relation}</td>
    <td>${o.objectEntityId}</td>
    <td><fmt:formatNumber type="number" value="${o.deltaP}" maxFractionDigits="4" /></td>
</tr>
</c:forEach>

</table>
</div>

</jsp:root>