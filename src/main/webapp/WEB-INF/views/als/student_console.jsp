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
        <link rel="stylesheet" href="${base_url}styles/als/student_console.css" type="text/css" media="screen" />
        <base href="${base_url}" />
    </head>
    
    
    <body>
    <c:choose>
    <c:when test="${student != null}">
    
        <h1>${student.firstName} ${student.lastName}</h1>
        
        <c:forEach var="ccd" items="${ccds}">
            
            <h2 class="curriculum">${ccd.curriculum.name}</h2>
            
            <div class="curriculumInfo">
                <div class="links">
                    <a href="tutoring/studentModel/${ccd.curriculum.nodeId}/${student.id}/inspect">Review student model</a><br/>
                    <a href="tutoring/event/list/${ccd.curriculum.nodeId}/${student.id}">Review event history</a>
                </div>
                
                <h3 class="exercises">Exercises</h3>
                <table class="exercises">
                <c:forEach var="exercise" items="${ccd.exercises}">
                    <tr>
                        <td class="name"><div><a href="${exercise.playUrl}">${exercise.name}</a></div></td>
                        <td class="recommendation">
                            <div class="priority_${exercise.priority}">
                                <fmt:formatNumber type="number" maxFractionDigits="2" value="${exercise.rating}" />
                            </div>
                        </td>
                        <td class="commands">
                            <div>
                                <a href="${exercise.factsUrl}">Facts</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </table>
            </div>
            
        </c:forEach>
        
    </c:when>
    <c:otherwise>
    
        <h1>Not currently logged in.</h1>
        
    </c:otherwise>
    </c:choose>
    </body>

</html>
</jsp:root>
