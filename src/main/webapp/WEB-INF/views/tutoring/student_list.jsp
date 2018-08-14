<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:form="http://www.springframework.org/tags/form"
    xmlns:s="http://www.springframework.org/tags"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns="http://www.w3.org/1999/xhtml"
    version="2.0">

<table class="list side">

<thead>
    <th>Name</th>
    <th># Enrolments</th>
</thead>

<c:forEach var="o" items="${students}">
<tr>
    <td><a href="tutoring/student/${o.user.id}" target="frame-main">${o.user.firstName} ${o.user.lastName}</a></td>
    <td>${fn:length(o.enrolments)}</td>
</tr>
</c:forEach>    

</table>

</jsp:root>