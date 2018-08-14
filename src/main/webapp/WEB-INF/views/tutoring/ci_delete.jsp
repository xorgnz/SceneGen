<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:form="http://www.springframework.org/tags/form"
    xmlns:s="http://www.springframework.org/tags"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns="http://www.w3.org/1999/xhtml"
    version="2.0">

<p><strong>Warning</strong> - you are about to delete the following curriculum.</p>
<p>This will delete all curriculum items associated with this set, including all student progress on these items</p> 

<form method="post">

<div class="glue">
    <fieldset>
        <legend>General Info</legend>
        
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
    <input type="submit" value="Confirm deletion" />
</div>

</form>

</jsp:root>