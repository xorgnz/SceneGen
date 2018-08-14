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
    <th width="140">Name</th>
    <th width="80">&#160;</th>
</thead>

<c:forEach var="o" items="${objs}">
<tr>
    <td><a href="style/sheet/${o.id}/configure" target="frame-main">${o.name}</a></td>
    <td align="right">
        <a href="style/sheet/${o.id}/edit" target="frame-main">Edit</a>&#160;
        <a href="style/sheet/${o.id}/delete" target="frame-main">Delete</a>
    </td>
</tr>
</c:forEach>    

</table>

</jsp:root>