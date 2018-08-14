<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:form="http://www.springframework.org/tags/form"
    xmlns:s="http://www.springframework.org/tags"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns="http://www.w3.org/1999/xhtml" 
    version="2.0">

<script src="scripts/3rdparty/highlight.xml.js">;</script>
<script language="javascript">
$(document).ready(function() 
{
    if (document.createStyleSheet)
        document.createStyleSheet('styles/highlight-idea.css]');
    else
        $("head").append("&lt;link rel='stylesheet' href='styles/highlight-idea.css' type='text/css' media='screen' /&gt;");

    $('pre').each(function(i, e) {hljs.highlightBlock(e)});
});
</script>

<div class="glue">
    <fieldset>
        <c:forEach var="p" items="${params}">
        <label>${p.key}</label>
        <input type="text" value="${p.value}" disabled="disabled" />
        </c:forEach>
    </fieldset>
</div>

<pre class="xml" style="width: 758px"><c:out value="${response}" /></pre>

</jsp:root>