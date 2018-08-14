<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:form="http://www.springframework.org/tags/form"
    xmlns:s="http://www.springframework.org/tags"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns="http://www.w3.org/1999/xhtml" 
    version="2.0">

<script language="javascript">
    function setParamLabel(id)
    {
        queries = 
        [<c:forEach var="q" items="${queries}">
            {
                id: ${q.id},
                params:
                [<c:forEach var="p" items="${q.parameters}">{
                    label: "${p.label}",
                    variable: "${p.variable}",
                },</c:forEach>],
            },
        </c:forEach>];
        
        // Locate parameters for query with given ID
        var queryParams = null;
        for (var i = 0 ; i &lt; queries.length ; i ++)
        {
            if (queries[i].id == id)
                queryParams = queries[i].params;
        }
        
        // Verify we found some parameters
        if (queryParams == null)
        {
            alert("Parameter label update failed. See console");
            console.log("Failed parameter label update");
            console.log(queries);
            console.log(queryParams);
            console.log("ID given: " + id);
        }
        
        // Create input fields
        else
        {
            div_fields = document.getElementById('paramFields');
            div_fields.innerHTML = "";
            
            for (var i = 0 ; i &lt; queryParams.length ; i ++)
            {
                var label = document.createElement("label")
                label.innerHTML = queryParams[i].label;
                label.style.height = "auto";
                
                var input = document.createElement("input");
                input.type = "text";
                input.name = "qparam_" + queryParams[i].variable + "";
                
                div_fields.appendChild(label);
                div_fields.appendChild(input);
            }
        }
    }
    
    $(document).ready(function()
    {
        var category = document.getElementById("querySelect");
        category.onchange();
    });
</script>

<form:form method="post" modelAttribute="co">

<div class="glue">
    <fieldset>
        <legend>General Info</legend>
        
        <label>Query</label>            
        <select id="querySelect" name="queryId" onChange="setParamLabel(this.value)">
            <c:forEach var="query" items="${queries}">
            <c:choose>
                <c:when test="${query.id == co.queryId}"><option value="${query.id}" selected="selected">${query.name}</option></c:when>
                <c:otherwise><option value="${query.id}">${query.name}</option></c:otherwise>
            </c:choose>
            </c:forEach>
        </select><br/>

        <div id="paramFields" style="margin: 0px;">&#160;</div>

    </fieldset>
</div>

<div class="glue">
    <button type="submit">Submit</button>
</div>
        
</form:form>

</jsp:root>
