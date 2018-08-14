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
    <th width="35">QID</th>
    <th width="140">Name</th>
    <th width="95">&#160;</th>
</thead>

<c:forEach var="o" items="${objs}">
<tr>
    <td><a href="http://xiphoid.biostr.washington.edu:8080/QueryManager/QueryManager.html#qid=${o.queryId}" target="_blank">${o.queryId}</a></td>
    <td><a href="query/query/${o.id}" target="frame-main">${o.name}</a></td>
    <td align="right">
        <a href="query/query/${o.id}/edit" target="frame-main">Edit</a>&#160;
        <a href="query/query/${o.id}/delete" target="frame-main">Delete</a>&#160;
        <a href="query/query/${o.id}/run" target="frame-main">Run</a>
    </td>
</tr>
</c:forEach>

</table>

</jsp:root>