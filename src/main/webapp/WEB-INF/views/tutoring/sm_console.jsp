 <jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:form="http://www.springframework.org/tags/form"
    xmlns:s="http://www.springframework.org/tags"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns="http://www.w3.org/1999/xhtml"
    version="2.0">

 
<h1>Curriculum: ${curriculum.name} + [${curriculum.nodeId}]</h1>

<c:forEach var="ci" items="${curriculum.curriculumItems}">
    <h2>CurriculumItem: ${ci.name} + [${ci.nodeId}]</h2>
    
    <c:forEach var="rk" items="${ci.relationKnowledge}">
        <h3>RelationKnowledge: ${rk.namespace} - ${rk.name}</h3>
        
        <table cellspacing="0">
        <c:forEach var="f" items="${rk.relationFacts}">
            <tr>
            <td><a href="tutoring/studentModel/fact/${f.nodeId}/b/${studentId}/true">True</a></td>
            <td><a href="tutoring/studentModel/fact/${f.nodeId}/b/${studentId}/false">False</a></td>
            <td>Fact: ${f.subject.fmaLabel} -- ${f.relation.namespace}: ${f.relation.name} -> ${f.object.fmaLabel}</td>
            </tr>
        </c:forEach>
        </table>
    </c:forEach>
</c:forEach>
</jsp:root>