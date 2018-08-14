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
    <label>ID</label>               <input type="text" value="${set.id}"           disabled="disabled" />
    <label>Name</label>             <input type="text" value="${set.name}"         disabled="disabled" />
    <label>Maintainer</label>       <input type="text" value="${set.maintainer}"   disabled="disabled" />
</fieldset>
</div>

<h3>Assets</h3>

<a href="asset/set/${set.id}/xml" target="_blank">View XML summary</a><br/>
<a href="asset/set/${set.id}/addAsset">Add an asset</a><br/>
<a href="asset/set/${set.id}/uploadZip">Add several assets - upload zip file</a><br/>
<a href="asset/set/${set.id}/updateStyleTags">Update style tags</a><br/>
<a href="asset/set/${set.id}/view3D">View as custom scene</a><br/>
<br/>

<div class="glue">
<table class="list full">

<thead>
    <th width="40">ID</th>
    <th>Name</th>
    <th width="60">Entity ID</th>
    <th width="60">Style?</th>
    <th width="80">&#160;</th>
    <th width="80">&#160;</th>
    <th width="100">&#160;</th>
</thead>


<c:forEach var="obj_inner" items="${assets}">
<tr>
    <td>${obj_inner.id}</td>
    <td><a href="asset/asset/${obj_inner.id}" target="frame-main">${obj_inner.name}</a></td>
    <td>${obj_inner.entityId}</td>
    <td><c:if test="${not empty obj_inner.styleTagsAsString}">Y</c:if></td>
    <td><a href="${obj_inner.x3dFilename}">Download X3D</a></td>
    <td><a href="${obj_inner.objFilename}">Download OBJ</a></td>
    <td>
        <a href="asset/asset/${obj_inner.id}/edit" target="frame-main">Edit</a>&#160;
        <a href="asset/asset/${obj_inner.id}/delete" target="frame-main">Delete</a>
    </td>
</tr>
</c:forEach>

</table>
</div>

</jsp:root>