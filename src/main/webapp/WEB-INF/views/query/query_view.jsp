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
    <label>ID</label>               <input type="text" value="${query.id}"                disabled="disabled" />
    <label>Query ID</label>         <input type="text" value="${query.queryId}"           disabled="disabled" />
    <label>Name</label>             <input type="text" value="${query.name}"              disabled="disabled" />
    <label>Tags</label>             <input type="text" value="${query.tags}"              disabled="disabled" />
    <label>Description</label>     <textarea disabled="disabled">${query.description}</textarea>
</fieldset>
</div>

<br/>
<hr/>

<h3>Parameters</h3>
<div class="glue half">
    <table class="list half">
    <thead>
        <th>Variable Name</th>
        <th>Label</th>
    </thead>
    <c:forEach var="p" items="${query.parameters}">
    <tr>
        <td width="130">${p.variable}</td>
        <td width="250">${p.label}</td>
    </tr>
    </c:forEach>
    </table>
</div>

<br/>
<hr/>
<h3>Cache Lines</h3>
<div class="glue">
    <table class="list full">
    <thead>
        <th>Param</th>
        <th>Retrieved</th>
        <th width="80">&#160;</th>
    </thead>
    <c:forEach var="obj_inner" items="${query.cacheLines}">
    <tr>
        <td>${obj_inner.parameterValueString}</td>
        <td>${obj_inner.retrieved}</td>
        <td>
            <a href="query/cacheLine/${obj_inner.id}" target="_blank">View</a>&#160;
            <a href="query/cacheLine/${obj_inner.id}/delete" target="frame-main">Delete</a>&#160;
        </td>
    </tr>
    </c:forEach>
    </table>
</div>
<a href="query/clearCache/${query.id}">Clear cache for this query</a><br/>



</jsp:root>