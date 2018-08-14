<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page" 
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
    xmlns="http://www.w3.org/1999/xhtml" version="2.0">

<jsp:directive.page contentType="text/html" pageEncoding="UTF-8" />
<jsp:output 
    doctype-root-element="html" 
    doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN" 
    doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" />
<html>

    <head>
        <title>Home</title>
        <c:url var="base_url" value="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />
        <link rel="stylesheet" href="${base_url}styles/front-base.css" type="text/css" media="screen" />
        <link rel="stylesheet" href="${base_url}styles/als/educator_console.css" type="text/css" media="screen" />
        <base href="${base_url}" />
    </head>
    
    
    <body>
    
        <h1>Education Console</h1>
        
        <c:forEach var="ccd" items="${ccds}">
            
            <h2 class="curriculum">${ccd.name}</h2>
            
            <div class="curriculumInfo">
                <div class="links">
                    <a href="tutoring/curriculum/${ccd.id}/edit">Edit Curriculum Metadata</a><br/>
                    <a href="tutoring/curriculum/${ccd.id}">Configure Curriculum</a>
                </div>
                
                <h3 class="students">Students</h3>
                <table class="students">
                <c:forEach var="student" items="${ccd.students}">
                    <tr>
                        <td class="name"><div>${student.name}</div></td>
                        <td class="rating">
                            <div class="achievement_${student.achievement}">
                                <fmt:formatNumber type="number" maxFractionDigits="2" value="${student.rating}" />
                            </div>
                        </td>
                        <td class="commands">
                            <div>
                                <a href="tutoring/studentModel/${ccd.id}/${student.id}/inspect">Inspect</a>&#160;
                                <a href="tutoring/event/list/${ccd.id}/${student.id}">Events</a>&#160;
                                <a href="tutoring/studentModel/${ccd.id}/${student.id}/unenrol">Unenrol</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </table>
            </div>
            
        </c:forEach>
        
    </body>

</html>
</jsp:root>
