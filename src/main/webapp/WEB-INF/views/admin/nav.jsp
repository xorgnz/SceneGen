<jsp:root
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns="http://www.w3.org/1999/xhtml" version="2.0">

<jsp:directive.page contentType="text/html" pageEncoding="UTF-8" />
<jsp:output omit-xml-declaration="true" />
<jsp:output doctype-root-element="HTML" doctype-system="about:legacy-compat" />


<html>
<head>
    <title>${siteTitle} - Admin Navigation</title>

    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <c:url var="base_url" value="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />

    <base href="${base_url}" />

    <link rel="stylesheet" href="${base_url}styles/admin-nav.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="${base_url}styles/admin-shared.css" type="text/css" media="screen" />

    <script src="${base_url}scripts/3rdparty/jquery-1.10.2.js">;</script>
    <script src="${base_url}scripts/admin/nav.js">;</script>
</head>
<body>
    <div id="wrapper">
        <div id="header">
            <div id="header-cell-right" class="header-cell">
                <c:choose>
                    <c:when test="${username != null}">Logged in as ${username}. <a href="j_spring_security_logout" target="_top">Log Out</a></c:when>
                    <c:otherwise>dNot logged in. <a href="javascript: alert('Access Control not implemented')">Log in</a></c:otherwise>
                </c:choose>
            </div>
            <div id="header-cell-left" class="header-cell">
                Server time: ${serverTime}<br/>
                Deployed: ???
            </div>
            <div id="header-cell-center" class="header-cell">
                <span id="header-title">${siteTitle}</span>
            </div>
        </div>

        <br clear="all" />

        <div id="nav-wrapper">
            <ul id="nav">
                <li style="float:right"><a href="javascript:;" onclick="showSubnav(100);">Administration</a></li>
                <li><a href="javascript:;" onclick="showSubnav(1);">Access Control</a></li>
                <li><a href="javascript:;" onclick="showSubnav(2);">Asset Library</a></li>
                <li><a href="javascript:;" onclick="showSubnav(3);">Query Management</a></li>
                <li><a href="javascript:;" onclick="showSubnav(4);">Styles</a></li>
                <li><a href="javascript:;" onclick="showSubnav(5);">Scene Builder</a></li>
                <li><a href="javascript:;" onclick="showSubnav(6);">Tutoring System</a></li>
                <li><a href="javascript:;" onclick="showSubnav(7);">Activity Management</a></li>
                <li><a href="javascript:;" onclick="showSubnav(8);">Applications</a></li>         
                <li><a href="javascript:;" onclick="showSubnav(9);">Frontend Shortcuts</a></li>                             
            </ul>

            <div id="subnav-wrapper">
                <ul id="subnav-1" class="subnav-menu">
                    <li><a href="rbac/usergrp/list" target="frame-side">User Groups</a></li>
                    <li><a href="rbac/user/list" target="frame-side">Users</a></li>
                    <li><a href="rbac/role/list" target="frame-side">Roles</a></li>
                    <li><a href="rbac/permission/list" target="frame-side">Permissions</a></li>
                </ul>
                <ul id="subnav-2" class="subnav-menu">
                    <li><a href="asset/set/list" target="frame-side">List Asset Sets</a></li>
                    <li><a href="asset/set/add" target="frame-main">Add Asset Set</a></li>
                </ul>
                <ul id="subnav-3" class="subnav-menu">
                    <li><a href="query/query/list" target="frame-side">List Queries</a></li>
                    <li><a href="query/query/add" target="frame-main">Add Query</a></li>
                    <li><a href="query/clearCache" target="frame-main">Clear Cache</a></li>
                </ul>
                <ul id="subnav-4" class="subnav-menu">
                    <li><a href="style/sheet/list" target="frame-side">List Stylesheets</a></li>
                    <li><a href="style/sheet/add" target="frame-main">Add Stylesheet</a></li>
                </ul>
                <ul id="subnav-5" class="subnav-menu">
                    <li><a href="scene/list" target="frame-side">List Scenes</a></li>
                    <li><a href="scene/add" target="frame-main">Create new Scene</a></li>
                </ul>
                <ul id="subnav-6" class="subnav-menu">
                    <li><a href="tutoring/curriculum/list" target="frame-side">List Curricula</a></li>
                    <li><a href="tutoring/student/list" target="frame-side">List Students</a></li>
                </ul>
                <ul id="subnav-7" class="subnav-menu">
                    <li><a href="activity/template/list" target="frame-side">List Activity Templates</a></li>
                    <li><a href="activity/template/add" target="frame-main">Add Activity Template</a></li>
                </ul>
                <ul id="subnav-8" class="subnav-menu">
                    <li><a href="explorer" target="frame-main">Anatomy Explorer</a></li>
                    <li><a href="visualizer" target="visualizer">Data Visualizer</a></li>
                </ul>
                <ul id="subnav-9" class="subnav-menu">
                    <li><a href="als/student" target="frame-main">Student Console</a></li>
                    <li><a href="als/educator" target="frame-main">Educator Console</a></li>
                </ul>
                <ul id="subnav-100" class="subnav-menu">
                    <li><a href="admin/initialize-db" target="frame-main">Initialize DB</a></li>
                    <li><a href="admin/background-tasks" target="frame-main">Show background tasks</a></li>
                </ul>
            </div>
        </div>
    </div>

</body>
</html>

</jsp:root>