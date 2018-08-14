<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:form="http://www.springframework.org/tags/form"
    xmlns:s="http://www.springframework.org/tags"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns="http://www.w3.org/1999/xhtml"
    version="2.0">
    
<p>${message}</p>

<form method="post" action="${url}" id="confirm">
    <input type="hidden" name="confirm" value="y"/>
</form>

<form method="get" action="admin/blank-main" id="cancel">
    <input type="hidden" name="confirm" value="n"/>
</form>

<table class="half">
<tr>
    <td align="center"><button onclick="document.getElementById('confirm').submit()">Confirm</button></td>
    <td align="center"><button onclick="document.getElementById('cancel').submit()" />Cancel</td>
</tr>
</table>

</jsp:root>