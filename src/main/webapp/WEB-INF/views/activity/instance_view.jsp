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
    <label>ID</label>               <input type="text" value="${instance.id}"                disabled="disabled" />
    <label>Name</label>             <input type="text" value="${instance.name}"              disabled="disabled" />
    <label>Description</label>      <textarea disabled="disabled">${instance.description}</textarea>
    <label>Template</label>         <input type="text" value="${instance.template.name}"     disabled="disabled" />
</fieldset>
</div>


<div class="glue half">
    <h2 style="text-align:left; margin-top: 0px">Parameters</h2>

    <table class="list half">
    <thead>
        <th>Label</th>
        <th>Value</th>
    </thead>
    <c:forEach var="p" items="${instance.parameterValues}">
    <tr>
        <td width="130">${p.variable}</td>
        <td width="250">${p.value}</td>
    </tr>
    </c:forEach>
    </table>
</div>


<div class="glue" style="text-align: left">
    <h2 style="text-align: left; margin: 0px 0px 4px 0px;">Exercises</h2>
    
    <c:choose>
    <c:when test="${fn:length(curricula) > 0}">
    <form:form method="post" action="activity/exercise/add" style="margin: 0px 0px 8px 0px;">
        <fieldset style="padding: 6px">
            <select name="curriculumId">
            <c:forEach var="c" items="${curricula}">
                <option value="${c.nodeId}">${c.name}</option>
            </c:forEach>
            </select>
            
            <input type="hidden" name="instanceId" value="${instance.id}" />
            
            <button type="submit">Create Exercise</button>
        </fieldset>
    </form:form>
    </c:when>
    </c:choose>
    
    <table class="list full">
    <thead>
        <th>Curriculum</th>
        <th width="80">&#160;</th>
    </thead>
    <c:forEach var="obj_inner" items="${exercises}">
    <tr>
        <td><a href="tutoring/curriculum/${obj_inner.curriculum.nodeId}" target="frame-main">${obj_inner.curriculum.name}</a></td>
        <td>
            <a href="activity/exercise/${obj_inner.id}/delete" target="frame-main">Delete</a>&#160;
        </td>
    </tr>
    </c:forEach>
    </table>
</div>

</jsp:root>