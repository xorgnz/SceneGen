<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:form="http://www.springframework.org/tags/form"
    xmlns:s="http://www.springframework.org/tags"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns="http://www.w3.org/1999/xhtml" 
    version="2.0">
    
    

<script src="${base_url}scripts/utils/DOMUtils.js">;</script>

<script language="javascript">
function removeStyle(anchor)
{
    // Grab source elements
    var div_src = anchor.parentElement;
    var td_src = div_src.parentElement;
    var tr_src = td_src.parentElement;
    var tbody = tr_src.parentElement;

    // Prevent removal of the last style
    if (tbody.childNodes.length == 1)
        alert("A stylesheet must contain at least one style");
    else
    {
        tbody.removeChild(tr_src);
        
        // Note that a change has been made
        markChanged();   
    }
}
function addStyle(anchor)
{
    // Grab source elements
    var div_src = anchor.parentElement;
    var td_src = div_src.parentElement;
    var tr_src = td_src.parentElement;
    var tbody = tr_src.parentElement;

    // Create new row
    var cells = [];
    var styles = [];
    
    cells.push(DOMUtils.input("tag", "New Style", "text", "width: 225px"));
    cells.push(DOMUtils.input("ambient", "#000000", "color", "width:62px"));
    cells.push(DOMUtils.input("diffuse", "#00ff00", "color", "width:62px"));
    cells.push(DOMUtils.input("emissive", "#000000", "color", "width:62px"));
    cells.push(DOMUtils.input("specular", "#ffffff", "color", "width:62px"));
    cells.push(DOMUtils.input("shininess", "10", "text", "width:62px"));

    // Colum - Alpha
    var div_alpha = DOMUtils.div();
    div_alpha.appendChild(DOMUtils.div("rangeLabel_left", null, "0"));
    div_alpha.appendChild(DOMUtils.input("alpha", 1, "range", "width:62px", { min: "0", max: "1", step: "0.01" }));
    div_alpha.appendChild(DOMUtils.div("rangeLabel_right", null, "1"));
    cells.push(div_alpha);
    styles[6] = "padding-top: 3px;";
    
    // Column - Controls
    var div_controls = DOMUtils.div();
    div_controls.appendChild(DOMUtils.link("javascript:;", "+", null, "addStyle(this);"));
    div_controls.appendChild(document.createTextNode("&#160;"));
    div_controls.appendChild(DOMUtils.link("javascript:;", "-", null, "removeStyle(this);"));
    div_controls.appendChild(document.createTextNode("&#160;"));
    div_controls.appendChild(DOMUtils.link("javascript:;", "u", null, "moveUpStyle(this);"));
    div_controls.appendChild(document.createTextNode("&#160;"));
    div_controls.appendChild(DOMUtils.link("javascript:;", "d", null, "moveDownStyle(this);")); 
    div_controls.appendChild(DOMUtils.input("styleId", "-1", "hidden", null));
    cells.push(div_controls);
    styles.push("padding-top: 8px; text-align:right;");

    // Insert new row
    if (tr_src.nextSibling == null)
        tbody.appendChild(DOMUtils.table_row(cells, styles));
    else
        tbody.insertBefore(DOMUtils.table_row(cells, styles), tr_src.nextSibling);
    
    // Note that a change has been made
    markChanged();
}


function moveUpStyle(anchor)
{
    // Grab source elements
    var div_src = anchor.parentElement;
    var td_src = div_src.parentElement;
    var tr_src = td_src.parentElement;
    var tbody = tr_src.parentElement;

    // Find previous element
    var previous = tr_src.previousSibling;
    
    // If current row has one before it, move it before that 
    if (previous != null)
    {
        tbody.removeChild(tr_src);
        tbody.insertBefore(tr_src, previous);
        
        // Note that a change has been made
        markChanged();
    }
}
function moveDownStyle(anchor)
{
    // Grab source elements
    var div_src = anchor.parentElement;
    var td_src = div_src.parentElement;
    var tr_src = td_src.parentElement;
    var tbody = tr_src.parentElement;

    // If current row has one after it
    if (tr_src.nextSibling != null)
    {
        // Grab it
        var next = tr_src.nextSibling;
        
        // Remove current row
        tbody.removeChild(tr_src);
        
        // Move current row
        if (next.nextSibling == null)
            tbody.appendChild(tr_src);
        else
            tbody.insertBefore(tr_src, next.nextSibling);
        
        // Note that a change has been made
        markChanged();
    }
}

var changed = false;
function markChanged()
{
	if (!changed)
        window.onbeforeunload = function() 
        {
            return "You have made changes to the styles in this stylesheet that have not been saved. Are you sure you wish to leave this page?";
        }
	
	changed = true;
}

</script>

<form:form method="post" commandName="co" cssClass="cleanform">

<s:bind path="*">
    <c:if test="${status.error}">
        <div class="msg_bad">Form has errors</div>
    </c:if>
</s:bind>

<div class="glue">
    <table class="list full">
        <thead>
            <th>Rule</th>
            <th width="70" style="text-align: center">Ambient</th>
            <th width="70" style="text-align: center">Diffuse</th>
            <th width="70" style="text-align: center">Emissive</th>
            <th width="70" style="text-align: center">Specular</th>
            <th width="70" style="text-align: center">Shininess</th>
            <th width="106" style="text-align: center">Alpha</th>            
            <th width="60">&#160;</th>
        </thead>
    
        <c:choose>
        <c:when test="${fn:length(co.styleId) > 0}">
        <c:forEach var="i" begin="0" end="${fn:length(co.styleId) - 1}">
        
        <tr>
            <td>
                <form:input type="text" path="tag" value="${co.tag[i]}" style="width:225px"/>
                <form:input type="hidden" path="styleId" value="${co.styleId[i]}"/>
            </td>
            <td><form:input type="color" path="ambient" value="${co.ambient[i]}" style="width:62px"/></td>
            <td><form:input type="color" path="diffuse" value="${co.diffuse[i]}" style="width:62px"/></td>
            <td><form:input type="color" path="emissive" value="${co.emissive[i]}" style="width:62px"/></td>
            <td><form:input type="color" path="specular" value="${co.specular[i]}" style="width:62px"/></td>
            <td><form:input type="text" path="shininess" value="${co.shininess[i]}" style="width:62px"/></td>
            <td width="106" style="padding-top: 3px;">
                <div class="rangeLabel_left">0</div>
                <form:input type="range" path="alpha" value="1" style="width:62px" min="0" max="1" step="0.01"/>
                <div class="rangeLabel_right">1</div>
            </td>
            <td style="padding-top: 8px; text-align:right">
                <div><a href="javascript:;" onclick="addStyle(this);">+</a>&#160;<a href="javascript:;" onclick="removeStyle(this);">-</a>&#160;<a href="javascript:;" onclick="moveUpStyle(this);">u</a>&#160;<a href="javascript:;" onclick="moveDownStyle(this);">d</a></div>
            </td>
        </tr>    
    
        </c:forEach>
        </c:when>
        
        <c:otherwise>
        <tr>
            <td>
                <form:input type="text" path="tag" value="New Style" style="width:225px"/>
                <form:input type="hidden" path="styleId" value="-1"/>
            </td>
            <td><form:input type="color" path="ambient" value="#a0a0a0" style="width:62px"/></td>
            <td><form:input type="color" path="diffuse" value="#a0a0a0" style="width:62px"/></td>
            <td><form:input type="color" path="emissive" value="#a0a0a0" style="width:62px"/></td>
            <td><form:input type="color" path="specular" value="#a0a0a0" style="width:62px"/></td>
            <td><form:input type="text" path="shininess" value="128" style="width:62px"/></td>
            <td width="106" style="padding-top: 3px;">
                <div class="rangeLabel_left">0</div>
                <form:input type="range" path="alpha" value="1" style="width:62px" min="0" max="1" step="0.01"/>
                <div class="rangeLabel_right">1</div>
            </td>
            <td style="padding-top: 8px; text-align:right">
                <div><a href="javascript:;" onclick="addStyle(this);">+</a>&#160;<a href="javascript:;" onclick="removeStyle(this);">-</a>&#160;<a href="javascript:;" onclick="moveUpStyle(this);">u</a>&#160;<a href="javascript:;" onclick="moveDownStyle(this);">d</a></div>
            </td>
        </tr>    
        </c:otherwise>
        
        </c:choose>
        
    </table>
</div>

<div class="glue">
    <button type="submit" onclick="window.onbeforeunload = null;">Configure Style Sheet</button>
</div>

</form:form>

</jsp:root>