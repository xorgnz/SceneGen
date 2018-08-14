<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:form="http://www.springframework.org/tags/form"
    xmlns:s="http://www.springframework.org/tags"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns="http://www.w3.org/1999/xhtml" 
    version="2.0">

<form:form method="post" modelAttribute="co" onsubmit="submitForm();" target="explorer">

<script>
var setIds = [<c:forEach var="assetSet" items="${assetSets}">${assetSet.id},</c:forEach>];

var setAssetSelector = function ()
{
    // Hide previous select
    for (var i = 0 ; i &lt; setIds.length ; i ++)
        document.getElementById('select_assets' + setIds[i]).style.display = "none";

    // Show current select
    var select_assetSet = document.getElementById('select_assetSet');
    document.getElementById('select_assets' + select_assetSet.value).style.display = "inline";
}

var submitForm = function ()
{
	// Get the currently selected asset
	var select_assetSet = document.getElementById('select_assetSet');
    var assetId = document.getElementById('select_assets' + select_assetSet.value).value;

    // Set the hidden assetId input to the selected asset's id
	var input = document.getElementById('input_assetId');
	input.value = assetId;
	
	console.log("submitting form");
};


$(document).ready(function ()
{
    setAssetSelector();
});

</script>

<div class="glue">
    <fieldset>
        <legend>General Info</legend>
        
        <label>Asset Set</label>
        <select name="assetSetId" onchange="setAssetSelector()" id="select_assetSet">
            <c:forEach var="assetSet" items="${assetSets}">
                <option value="${assetSet.id}">${assetSet.name}</option>
            </c:forEach>
        </select>
        
        <label>Starting Asset</label>
        <c:forEach var="assetSet" items="${assetSets}">
            <select id="select_assets${assetSet.id}" style="display:none">
            <c:forEach var="asset" items="${assetSet.assets}">
                <option value="${asset.id}">${asset.name}</option>    
            </c:forEach>
            </select>
        </c:forEach><br/>

        <label>Stylesheet</label>
        <select name="stylesheetId">
            <c:forEach var="stylesheet" items="${stylesheets}">
            <c:choose>
                <c:when test="${stylesheet.id == co.stylesheetId}"><option value="${stylesheet.id}" selected="selected">${stylesheet.name}</option></c:when>
                <c:otherwise><option value="${stylesheet.id}">${stylesheet.name}</option></c:otherwise>
            </c:choose>
            </c:forEach>
        </select><br/>
        
    </fieldset>
</div>
<input type="hidden" name="assetId" id="input_assetId" value="-1" />

<div class="glue">
    <button type="submit">Submit</button>
</div>
        
</form:form>

</jsp:root>