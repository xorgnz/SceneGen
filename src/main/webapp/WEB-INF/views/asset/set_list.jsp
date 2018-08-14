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
    <th width="200">Name</th>
    <th width="20">#A</th>
    <th width="65">&#160;</th>
</thead>

<c:forEach var="o" items="${assetSets}">
<tr>
    <td><a href="asset/set/${o.id}" target="frame-main">${o.name}</a></td>
    <td>${o.assetCount}</td>
    <td align="right">
        <a href="asset/set/${o.id}/edit" target="frame-main">Edit</a>&#160;
        <a href="asset/set/${o.id}/delete" target="frame-main">Delete</a>
    </td>
</tr>
</c:forEach>

</table>

</jsp:root>