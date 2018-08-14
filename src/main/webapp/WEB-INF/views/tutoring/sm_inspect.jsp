<jsp:root
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns:form="http://www.springframework.org/tags/form"
    xmlns:s="http://www.springframework.org/tags"
    xmlns:fn="http://java.sun.com/jsp/jstl/functions"
    xmlns="http://www.w3.org/1999/xhtml"
    version="2.0">

<jsp:directive.page contentType="text/html" pageEncoding="UTF-8" />
<jsp:output
    doctype-root-element="html"
    doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
    doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" />

<html>
<head>
    <title>${scnd.name}</title>

    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <c:url var="base_url" value="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />
    <base href="${base_url}" />

    <!-- Styles -->
    <link rel="stylesheet" href="${base_url}styles/tutor_inspect.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="${base_url}styles/admin-base.css" type="text/css" media="screen"/>
    <link rel="stylesheet" href="${base_url}styles/admin-shared.css" type="text/css" media="screen" />

    <!-- 3rd party JS -->
    <script src="${base_url}scripts/3rdparty/jquery-1.10.2.js">;</script>
    <script src="${base_url}scripts/3rdparty/lodash.js">;</script>

    <!-- Classes -->
    <script src="${base_url}scripts/utils/DOMUtils.js">;</script>
    <script src="${base_url}scripts/tutoring/inspect.js">;</script>

    <script>
         // Globals
        "use strict";

        var base_url = "${base_url}";

        var studentId = ${student.id};

        var curriculum =
        {
            name: "${curriculum.name}",
            id: ${curriculum.nodeId},
            type: Node.TYPE_CURRICULUM,
            p: ${bayes[curriculum.nodeId].p},
            items:
            [<c:forEach var="ci" items="${curriculum.curriculumItems}">{
                name: "${ci.name}",
                id: ${ci.nodeId},
                type: Node.TYPE_CURRICULUM_ITEM,
                p: ${bayes[ci.nodeId].p},
                items:
                [
                <c:choose><c:when test="${ci.labelKnowledge != null}">{
                    name: "Label Knowledge",
                    id: ${ci.labelKnowledge.nodeId},
                    type: Node.TYPE_LABEL_KNOWLEDGE,
                    p: ${bayes[ci.labelKnowledge.nodeId].p},
                    items:
                    [
                        <c:forEach var="f" items="${ci.labelKnowledge.labelFacts}">{
                            name: "Label: ${f.entityKnowledge.fmaLabel}",
                            id: ${f.nodeId},
                            type: Node.TYPE_LFACT,
                            p: ${bayes[f.nodeId].p},
                        },</c:forEach>
                    ]
                },</c:when></c:choose>
                <c:forEach var="ek" items="${ci.entityKnowledge}">{
                    name: "${ek.fmaLabel} [${ek.fmaId}]",
                    id: ${ek.nodeId},
                    type: Node.TYPE_ENTITY_KNOWLEDGE,
                    p: ${bayes[ek.nodeId].p},
                    items:
                    [
                        <c:choose><c:when test="${ek.labelFact != null}">{
                            name: "Label: ${ek.fmaLabel}",
                            id: ${ek.labelFact.nodeId},
                            type: Node.TYPE_LFACT,
                            p: ${bayes[ek.labelFact.nodeId].p},
                        }</c:when></c:choose>,
                        <c:forEach var="f" items="${ek.relationFacts}">{
                            name: "${f.subject.fmaLabel} -[${f.relation.namespace}:${f.relation.name}]-> ${f.object.fmaLabel}",
                            id: ${f.nodeId},
                            type: Node.TYPE_RFACT,
                            p: ${bayes[f.nodeId].p},
                        },</c:forEach>
                    ]
                },
                </c:forEach><c:forEach var="rk" items="${ci.relationKnowledge}">{
                    name: "${rk.namespace}:${rk.name}",
                    id: ${rk.nodeId},
                    type: Node.TYPE_RELATION_KNOWLEDGE,
                    p: ${bayes[rk.nodeId].p},
                    items:
                    [<c:forEach var="f" items="${rk.relationFacts}">{
                        name: "${f.subject.fmaLabel} -[${f.relation.namespace}:${f.relation.name}]-> ${f.object.fmaLabel}",
                        id: ${f.nodeId},
                        type: Node.TYPE_RFACT,
                        p: ${bayes[f.nodeId].p},
                    },</c:forEach>]
                },</c:forEach>]
            },</c:forEach>]
        };

        $(document).ready(function()
        {
            var div_hierarchy = document.getElementById("hierarchy");
            div_hierarchy.innerHTML = "";
            var root = new Node(curriculum);
            root.express(div_hierarchy);
        });
    </script>
    </head>
    <body>
        <h1>Inspect Curriculum for ${student.firstName} ${student.lastName}</h1>

        <c:if test="${not empty msg_good}">
            <div class="msg_good">${msg_good}</div>
        </c:if>

        <c:if test="${not empty msg_bad}">
            <div class="msg_bad">
                ${msg_bad}
                <c:if test="${not empty exception}"><br/><br/><pre>${exception}</pre></c:if>
            </div>
        </c:if>

        <div id="hierarchy" class="hierarchy">&#160;</div>
    </body>
</html>
</jsp:root>


<!--
<c:if test="${not empty curriculumItems}">
<div class="hierarchy_level">

    <c:forEach var="item" items="${curriculumItems}">
    <div class="hierarchy_element">

        <span class="hierarchy_label">${item.name}</span>

        <div class="hierarchy_level">

            <c:forEach var="ek" items="${item.entityKnowledge}">
            <div class="hierarchy_element">
                <span class="hierarchy_label">Entity: ${ek.fmaLabel} [${ek.fmaId}]</span>
            </div>
            </c:forEach>

            <c:forEach var="rk" items="${item.relationKnowledge}">
            <div class="hierarchy_element">
                <span class="hierarchy_label">Relation: ${rk.namespace}:[${rk.name}]</span>
            </div>
            </c:forEach>

        </div>

    </div>
    </c:forEach>
</div>
</c:if>

</jsp:root> -->