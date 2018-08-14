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
    <link rel="stylesheet" href="${base_url}styles/cga.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="${base_url}styles/ava.css" type="text/css" media="screen" />

    <!-- 3rd party JS -->
    <script src="${base_url}scripts/3rdparty/three.min.56.js">;</script>
    <script src="${base_url}scripts/3rdparty/obj_loader.js">;</script>
    <script src="${base_url}scripts/3rdparty/jquery-1.10.2.js">;</script>
    <script src="${base_url}scripts/3rdparty/jquery.mousewheel.min.js">;</script>
    <script src="${base_url}scripts/3rdparty/lodash.js">;</script>

    <!-- Utility Classes -->
    <script src="${base_url}scripts/utils/DOMUtils.js">;</script>
    <script src="${base_url}scripts/utils/Lang.js">;</script>
    <script src="${base_url}scripts/utils/UI_Panel.js">;</script>

    <!-- CGA - Base Classes -->
    <script src="${base_url}scripts/cga/CGA_BaseController.js">;</script>
    <script src="${base_url}scripts/cga/CGA_ExamineCamera.js">;</script>
    <script src="${base_url}scripts/cga/CGA_GeometryObject.js">;</script>
    <script src="${base_url}scripts/cga/CGA_GraphicsEngine.js">;</script>
    <script src="${base_url}scripts/cga/CGA_GroupObject.js">;</script>
    <script src="${base_url}scripts/cga/CGA_InteractionManager.js">;</script>
    <script src="${base_url}scripts/cga/CGA_Main.js">;</script>
    <script src="${base_url}scripts/cga/CGA_MeshLoader.js">;</script>
    <script src="${base_url}scripts/cga/CGA_MeshManager.js">;</script>
    <script src="${base_url}scripts/cga/CGA_ObjectDescriptor.js">;</script>
    <script src="${base_url}scripts/cga/CGA_Scene.js">;</script>
    <script src="${base_url}scripts/cga/CGA_SceneContentLoader.js">;</script>
    <script src="${base_url}scripts/cga/CGA_UI_HelpPanel.js">;</script>
    <script src="${base_url}scripts/cga/CGA_UI_LoadProgressPanel.js">;</script>

    <!-- AVA - Classes -->
    <script src="${base_url}scripts/ava/AVA_ContentsPanel.js">;</script>
    <script src="${base_url}scripts/ava/AVA_Controller.js">;</script>
    <script src="${base_url}scripts/ava/AVA_Main.js">;</script>
    <script src="${base_url}scripts/ava/AVA_PickingPanel.js">;</script>
    

    <script>
         // Globals
        "use strict";

        var cgaObjectDescriptors = 
        [<c:forEach var="scnod" items="${scnd.sceneObjectDescriptors}">
        <c:if test="${scnod.asset.objFilename != '' and scnod.visible}">
        new CGA_ObjectDescriptor(
            ${scnod.id},
            "${scnod.entity.name}",
            "${scnod.asset.objFilename}",            
            {<c:choose><c:when test="${scnod.style != null}"> 
                ambient: "${scnod.style.ambient}", 
                diffuse: "${scnod.style.diffuse}", 
                emissive: "${scnod.style.emissive}", 
                specular: "${scnod.style.specular}", 
                shininess: "${scnod.style.shininess}",
                alpha: "${scnod.style.alpha}",
            </c:when><c:otherwise> 
                ambient: "#808080", 
                diffuse: "#808080", 
                emissive: "#000000", 
                specular: "#ffffff", 
                shininess: 20,
                alpha: 1,
            </c:otherwise></c:choose> 
            },
            ${scnod.visible}, {}),
        </c:if>
        </c:forEach>];

        <c:if test="${errorMessage != null}">var errorMessage = "${errorMessage}"</c:if>

        // Initialize page when ready
        $(document).ready(function ()
        {
            if (typeof errorMessage !== 'undefined')
            {
                var msg = "Errors occurred while preparing this scene:\n\n" + errorMessage

                console.log(msg);
                alert(msg);
            }

            // Configure viewer
            var cgaConfig =
            {
                elements:
                {
                    gfx_container: document.getElementById("cga_gfx_container"),
                },
                gfx: {
                    <c:choose><c:when test="${scnd.viewpoint != null}">
                    camera: { 
                        position: { x: ${scnd.viewpoint.position.x}, y: ${scnd.viewpoint.position.y}, z: ${scnd.viewpoint.position.z} },
                        rotation: { x: ${scnd.viewpoint.rotation.x}, y: ${scnd.viewpoint.rotation.y}, z: ${scnd.viewpoint.rotation.z} },
                        target: { x: ${scnd.viewpoint.target.x}, y: ${scnd.viewpoint.target.y}, z: ${scnd.viewpoint.target.z} },
                        up: { x: ${scnd.viewpoint.up.x}, y: ${scnd.viewpoint.up.y}, z: ${scnd.viewpoint.up.z} }
                    }
                    </c:when>
                    <c:otherwise>
                    camera: null,
                    </c:otherwise></c:choose>
                },
                interaction: {},
                url: "/SIG-SceneGen/",
            };
    
            // Load AVA

            // Load AVA
            var mainApp = new CGA_Main(cgaConfig);
            mainApp.addController("base", new CGA_BaseController(mainApp));
            var ava = new AVA_Main(mainApp, cgaObjectDescriptors);
        });

    </script>
</head>
<body>
    <div id="cga_gfx_container">
        <div id="cga_lpp_wrap"><div id="cga_lpp">&#160;</div></div>

        <div id="cga_help_wrap"><div id="cga_help"><a href="/SIG-SceneGen/${url}" target="_blank">View full screen</a></div></div>

        <div id="ava_ui_container">
            <div id="ava_ui_right_panel"><div id="ava_contents">&#160;</div></div>
            <div id="ava_ui_left_panel">
                <div id="ava_picking">&#160;</div>
                
                <div id="ava_left_buttons_wrapper">
                    <div id="cga_help_button">Help</div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
</jsp:root>