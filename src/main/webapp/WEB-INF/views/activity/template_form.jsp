<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:form="http://www.springframework.org/tags/form"
    xmlns:s="http://www.springframework.org/tags"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns="http://www.w3.org/1999/xhtml" 
    version="2.0">

<script src="${base_url}scripts/utils/DOMUtils.js">;</script>
<script src="${base_url}scripts/3rdparty/lodash.js">;</script>
<script src="${base_url}scripts/activity/template_form.js">;</script>

<script language="javascript">

// Pre-existing parameters
var parameters = 
[
    <c:if test="${fn:length(co.parameters) > 0}">
    <c:forEach var="i" begin="0" end="${fn:length(co.parameters) - 1}">
    <c:if test="${co.parameters[i].variable != ''}">
    {
        variable: "${co.parameters[i].variable}",
        label: "${co.parameters[i].label}",
        type: "${co.parameters[i].type}"
    },
     </c:if></c:forEach></c:if>
];

// On document load, create fields for pre-existing parameters
$(document).ready(function ()
{
    for (var i = 0 ; i &lt; parameters.length ; i++)
    {
        var param = parameters[i];
        createParameterLine(param);
    }
});

</script>

<form:form method="post" commandName="co" cssClass="cleanform">

<s:bind path="*">
    <c:if test="${status.error}">
        <div class="msg_bad">Form has errors</div>
    </c:if>
</s:bind>
    
<div class="glue">
    <fieldset>
        <legend>General Info</legend>
        
        <form:label path="name">Name</form:label>
        <div>
            <form:input path="name"/><br/>
            <form:errors path="name" cssClass="error" />
        </div>
        <form:label path="description">Description</form:label>
        <div>
            <form:textarea path="description" />
            <form:errors path="description" cssClass="error" />            
        </div>
        <form:label path="factsUrl">Facts URL</form:label>
        <div>
            <form:input path="factsUrl"/><br/>
            <form:errors path="factsUrl" cssClass="error" />
        </div>
        <form:label path="playUrl">Play URL</form:label>
        <div>
            <form:input path="playUrl"/><br/>
            <form:errors path="playUrl" cssClass="error" />
        </div>
    </fieldset>
</div>

<h3>Parameters</h3>
<div class="glue">
    <table class="list">
    <thead>
        <th width="200">Variable Name</th>
        <th width="200">Label</th>
        <th width="200">Type</th>        
        <th width="62"></th>
    </thead>
    <tbody id="tbody-params">
    <tr>
        <td colspan="4"><input type="button" onclick="addParameter()" value="Add parameter"/></td>
    </tr>
    </tbody>
    </table>
</div>
<br/>
<br/>

<div class="glue">
    <button type="submit" onclick="submitForm();">Submit</button>
</div>

</form:form>

</jsp:root>