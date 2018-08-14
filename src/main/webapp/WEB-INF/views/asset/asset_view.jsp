<jsp:root
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:form="http://www.springframework.org/tags/form"
    xmlns:s="http://www.springframework.org/tags"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
    xmlns="http://www.w3.org/1999/xhtml"
    version="2.0">

<div class="glue">
<fieldset>
    <legend>General Info</legend>
    <label>Name</label>                 <input type="text" value="${obj.name}"          disabled="disabled" />
    <label>Asset Set</label>            <input type="text" value="${obj.assetSet.name}" disabled="disabled" /><br/>
    <label>ID</label>                   <input type="text" value="${obj.id}"            disabled="disabled" />
    <label>Entity ID</label>            <input type="text" value="${obj.entityId}"      disabled="disabled" /><br/>
    <label>Style Tags</label>           <input type="text" value="${fn:join(obj.styleTags, '-')}" disabled="disabled" class="wide"/>
</fieldset>
</div>


<div align="center">
	<iframe name="webgl-box"  id="webgl-box" src="asset/${obj.id}/view3D" style="width:780px; height:780px;" scrolling="no">WebGL viewer goes here</iframe>
</div>

<div class="glue">
<fieldset>
    <legend>Asset File</legend>
    <label>X3D File</label>             <span><a href="asset/file/${obj.x3dFilename}">Download</a></span><br/>
    <label>OBJ File</label>             <span><a href="asset/file/${obj.objFilename}">Download</a></span><br/>
</fieldset>
</div>

<div class="glue">
<fmt:formatNumber value="${obj.centroid[0]}" maxFractionDigits="3" minFractionDigits="3" var="cent_x" />
<fmt:formatNumber value="${obj.centroid[1]}" maxFractionDigits="3" minFractionDigits="3" var="cent_y" />
<fmt:formatNumber value="${obj.centroid[2]}" maxFractionDigits="3" minFractionDigits="3" var="cent_z" />
<fmt:formatNumber value="${obj.min[0]}"      maxFractionDigits="3" minFractionDigits="3" var="min_x" />
<fmt:formatNumber value="${obj.min[1]}"      maxFractionDigits="3" minFractionDigits="3" var="min_y" />
<fmt:formatNumber value="${obj.min[2]}"      maxFractionDigits="3" minFractionDigits="3" var="min_z" />
<fmt:formatNumber value="${obj.max[0]}"      maxFractionDigits="3" minFractionDigits="3" var="max_x" />
<fmt:formatNumber value="${obj.max[1]}"      maxFractionDigits="3" minFractionDigits="3" var="max_y" />
<fmt:formatNumber value="${obj.max[2]}"      maxFractionDigits="3" minFractionDigits="3" var="max_z" />
<fieldset>
    <legend>Statistics</legend>
    <label>Centroid</label>             <input type="text" value="(${cent_x}, ${cent_y}, ${cent_z})"    disabled="disabled" /><br/>
    <label>Min</label>                  <input type="text" value="(${min_x}, ${min_y}, ${min_z})" disabled="disabled" />
    <label>Max</label>                  <input type="text" value="(${max_x}, ${max_y}, ${max_z})" disabled="disabled" /><br/>
</fieldset>
</div>

</jsp:root>