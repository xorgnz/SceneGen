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
    <label>ID</label>               <input type="text" value="${template.id}"                disabled="disabled" />
    <label>Name</label>             <input type="text" value="${template.name}"              disabled="disabled" />
    <label>Description</label>      <textarea disabled="disabled">${template.description}</textarea>
    <label>Facts URL</label>        <input type="text" value="${template.factsUrl}"          disabled="disabled" />
    <label>Play URL</label>         <input type="text" value="${template.playUrl}"           disabled="disabled" />
</fieldset>
</div>


<div class="glue half">
    <h2 style="text-align:left; margin-top: 0px">Parameters</h2>

    <table class="list half">
    <thead>
        <th>Variable Name</th>
        <th>Label</th>
        <th>Type</th>
    </thead>
    <c:forEach var="p" items="${template.parameters}">
    <tr>
        <td width="130">${p.variable}</td>
        <td width="250">${p.label}</td>
        <td width="250">${p.type}</td>
    </tr>
    </c:forEach>
    </table>
</div>


<div class="glue" style="text-align: left">
    <h2 style="text-align: left; margin: 0px 0px 4px 0px;">Instances</h2>
    
    <a href="activity/instance/add/${template.id}">Create instance</a><br/>
    <br/>

    <table class="list full">
    <thead>
        <th>Name</th>
        <th width="80">&#160;</th>
    </thead>
    <c:forEach var="obj_inner" items="${instances}">
    <tr>
        <td><a href="activity/instance/${obj_inner.id}" target="frame-main">${obj_inner.name}</a></td>
        <td>
            <a href="activity/instance/${obj_inner.id}/edit" target="frame-main">Edit</a>&#160;
            <a href="activity/instance/${obj_inner.id}/delete" target="frame-main">Delete</a>&#160;
        </td>
    </tr>
    </c:forEach>
    </table>
</div>

</jsp:root>