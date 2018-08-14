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
    <label>ID</label>               <input type="text" value="${curriculum.nodeId}"       disabled="disabled" />
    <label>Name</label>             <input type="text" value="${curriculum.name}"         disabled="disabled" />
    <label>Creator</label>          <input type="text" value="${curriculum.creatorName}"  disabled="disabled" />
    <label>Description</label>      <textarea disabled="disabled">${curriculum.description}</textarea>
</fieldset>
</div>

<h3>Curriculum Items</h3>

<a href="tutoring/curriculum/${curriculum.nodeId}/addCurriculumItem">Add a Curriculum Item</a><br/>
<br/>

<div class="glue">
<table class="list full">

<thead>
    <th width="40">ID</th>
    <th>Title</th>
    <th width="40">Weight</th>
    <th width="100">&#160;</th>
</thead>


<c:forEach var="item" items="${curriculumItems}">
<tr>
    <td>${item.nodeId}</td>
    <td><a href="tutoring/curriculumItem/${item.nodeId}/build" target="frame-main">${item.name}</a></td>
    <td>${item.weight}</td>
    <td>
        <a href="tutoring/curriculumItem/${item.nodeId}/edit" target="frame-main">Edit</a>&#160;
        <a href="tutoring/curriculumItem/${item.nodeId}/delete" target="frame-main">Delete</a>
    </td>
</tr>
</c:forEach>    

</table>
</div>

<h3>Enrolments</h3>
<c:choose>
<c:when test="${fn:length(users) > 0}">
<div class="glue">
<form:form method="post" action="tutoring/studentModel/enrol">
    <fieldset>
        <select name="studentId">
        <c:forEach var="user" items="${users}">
            <option value="${user.id}">${user.firstName} ${user.lastName}</option>
        </c:forEach>
        </select>
        
        <input type="hidden" name="curriculumId" value="${curriculum.nodeId}" />
        
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
    <td>${e.student.id}</td>
    <td>${e.student.firstName} ${e.student.lastName}</td>
    <td style="text-align:center"><a href="tutoring/event/list/${e.curriculumId}/${e.student.id}">${e.eventCount}</a></td>
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