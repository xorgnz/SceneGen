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
    <label>ID</label>               <input type="text" value="${student.id}"           disabled="disabled" /><br/>
    <label>First Name</label>       <input type="text" value="${student.firstName}"    disabled="disabled" />
    <label>Last Name</label>        <input type="text" value="${student.lastName}"     disabled="disabled" /><br/>
    <label>Username</label>         <input type="text" value="${student.username}"     disabled="disabled" /><br/>
    <label>Email</label>            <input type="text" value="${student.email}"        disabled="disabled" /><br/>
</fieldset>
</div>

<h3>Enrolments</h3>
${not empty filteredCurriculaMap}
<c:choose>
<c:when test="${not empty filteredCurriculaMap}">
<div class="glue">
<form:form method="post" action="tutoring/studentModel/enrol">
    <fieldset>
        <select name="curriculumId">
        <c:forEach var="item" items="${filteredCurriculaMap}">
            <option value="${item.key}">${item.value.name}</option>
        </c:forEach>
        </select>

        <input type="hidden" name="studentId" value="${student.id}" />        
        <button type="submit">Enrol this user</button>
    </fieldset>
</form:form>
</div>
</c:when>
</c:choose>

<div class="glue">
<table class="list full">

<thead>
    <th width="40">ID</th>
    <th>Name</th>
    <th width="50">#Events</th>
    <th width="150">&#160;</th>
</thead>


<c:forEach var="e" items="${enrolments}">
<tr>
    <td>${e.curriculumId}</td>
    <td>${allCurriculaMap[e.curriculumId].name}</td>
    <td style="text-align:center">${e.eventCount}</td>
    <td>
        <a href="tutoring/studentModel/${e.curriculumId}/${e.student.id}/inspect" target="frame-main">Inspect</a>&#160;
        <a href="tutoring/studentModel/${e.curriculumId}/${e.student.id}/console" target="frame-main">Console</a>&#160;
        <a href="tutoring/studentModel/${e.curriculumId}/${e.student.id}/unenrol" target="frame-main">Unenrol</a>
    </td>
</tr>
</c:forEach>    

</table>
</div>


</jsp:root>