<jsp:root
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:form="http://www.springframework.org/tags/form"
    xmlns:s="http://www.springframework.org/tags"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns="http://www.w3.org/1999/xhtml"
    version="2.0">


<script language="javascript">

    function selectAll()
    {
        var form = document.getElementById("co");
        for (var i = 0 ; i &lt; form.elements.length ; i++)
        {
            if (form.elements[i].type == 'checkbox')
                form.elements[i].checked = true;

        }
    }

    function deselectAll()
    {
        var form = document.getElementById("co");
        for (var i = 0 ; i &lt; form.elements.length ; i++)
        {
            if (form.elements[i].type == 'checkbox')
                form.elements[i].checked = false;

        }
    }
</script>

<form:form method="post" modelAttribute="co">

<div class="glue">
    <fieldset>
        <legend>Curriculum Item</legend>

        <label>Name</label>
        <div>
            <input type="text" value="${curriculumItem.name}" disabled="disabled" />
        </div>

        <label>Weight</label>
        <div>
            <input type="text" value="${curriculumItem.weight}" disabled="disabled" />
        </div>

        <label>Description</label>
        <div>
            <textarea disabled="disabled">${curriculumItem.description}</textarea>
        </div>
        <br/>
    </fieldset>
</div>

<div class="glue">
    <table class="list full">
        <thead>
            <th width="40">&#160;</th>
            <th width="40">ID</th>
            <th>Name</th>
            <th width="60">FMAID</th>
        </thead>

        <c:forEach var="asset" items="${assets}">
            <tr>
                <td><form:checkbox path="assetIds" value="${asset.id}" /></td>
                <td>${asset.id}</td>
                <td>${asset.name}</td>
                <td>${asset.entityId}</td>
            </tr>
        </c:forEach>
    </table>
    <button form="not_a_form" onclick="selectAll();">Select All</button>
    <button form="not_a_form" onclick="deselectAll();">Deselect All</button>
</div>


<div class="glue">
    <button form="co" type="submit">Add to Curriculum</button>
</div>

<input type="hidden" name="assetSetId" value="${assetSet.id}" />
<input type="hidden" name="stage" value="2" />

</form:form>

</jsp:root>