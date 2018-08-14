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
    <title>Scene Builder - ${scnd.name}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <c:url var="base_url" value="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />
    <base href="${base_url}" />

    <!-- AVA styles -->
    <link rel="stylesheet" href="${base_url}styles/cga.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="${base_url}styles/sb.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="${base_url}styles/3rdparty/jquery-ui.css" type="text/css" media="screen" />

    <!-- 3rd party JS -->
    <script src="${base_url}scripts/3rdparty/three.min.56.js">;</script>
    <script src="${base_url}scripts/3rdparty/obj_loader.js">;</script>
    <script src="${base_url}scripts/3rdparty/jquery-1.10.2.js">;</script>
    <script src="${base_url}scripts/3rdparty/jquery-ui-1.11.1.js">;</script>
    <script src="${base_url}scripts/3rdparty/jquery.mousewheel.min.js">;</script>
    <script src="${base_url}scripts/3rdparty/TSim.js">;</script>
    <script src="${base_url}scripts/3rdparty/lodash.js">;</script>

    <!-- Utility classes -->
    <script src="${base_url}scripts/utils/DOMUtils.js">;</script>
    <script src="${base_url}scripts/utils/Lang.js">;</script>
    <script src="${base_url}scripts/utils/MultiMap.js">;</script>
    <script src="${base_url}scripts/utils/SG_TextCompletion.js">;</script>
    <script src="${base_url}scripts/utils/TEventDispatcher.js">;</script>
    <script src="${base_url}scripts/utils/UI_Panel.js">;</script>
    
    <!-- CGA Core -->
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
    <script src="${base_url}scripts/cga/CGA_UI_HelpPanel.js">;</script>
    <script src="${base_url}scripts/cga/CGA_UI_LoadProgressPanel.js">;</script>
    
    <!-- Scene Builder -->
    <script src="${base_url}scripts/sb/SB_Builder.js">;</script>
    <script src="${base_url}scripts/sb/SB_Communicator.js">;</script>
    <script src="${base_url}scripts/sb/SB_Config.js">;</script>
    <script src="${base_url}scripts/sb/SB_Controller.js">;</script>
    <script src="${base_url}scripts/sb/SB_Lens.js">;</script>
    <script src="${base_url}scripts/sb/SB_Main.js">;</script>
    <script src="${base_url}scripts/sb/SB_Scene.js">;</script>
    <script src="${base_url}scripts/sb/SB_UI_AddListFragment.js">;</script>
    <script src="${base_url}scripts/sb/SB_UI_AddQueryFragment.js">;</script>
    <script src="${base_url}scripts/sb/SB_UI_EditScene.js">;</script>
    <script src="${base_url}scripts/sb/SB_UI_EditListFragment.js">;</script>
    <script src="${base_url}scripts/sb/SB_UI_EditQueryFragment.js">;</script>
    <script src="${base_url}scripts/sb/SB_UI_DeleteSceneFragment.js">;</script>
    <script src="${base_url}scripts/sb/SB_UI_LinkPanel.js">;</script>
    <script src="${base_url}scripts/sb/SB_UI_ObjectInfo.js">;</script>
    <script src="${base_url}scripts/sb/SB_UI_Overview.js">;</script>
    <script src="${base_url}scripts/sb/SB_UI_WorkingNotification.js">;</script>

    <script>
         // Globals
        "use strict";

        // Initialize page when ready
        $(document).ready(function ()
        {
            // Configure CGA
            var cgaConfig =
            {
                elements:
                {
                    gfx_container: document.getElementById("cga_gfx_container"),
                },
                gfx: {},
                interaction: {
                    pickingOnHoverEnabled: false,
                },
                url: "",
            };
            
            // Configure Scene Builder
            var sbConfig = new SB_Config(
            {
                sceneId: ${sceneId},
                serverURL: "${base_url}",
            });

            // Configure text completion
            SG_TextCompletion.serverURL = sbConfig.serverURL;

            // Load Scene Builder
            var mainApp = new CGA_Main(cgaConfig);
            mainApp.addController("base", new CGA_BaseController(mainApp));
            var builder = new SB_Main(sbConfig, mainApp);
        });
    </script>
</head>
<body>
    <div id="cga_gfx_container">
        <div id="cga_lpp_wrap"><div id="cga_lpp">&#160;</div></div>

        <div id="sb_ui_container">
            <div id="sb_working">&#160;</div>
            <div id="sb_ui_right_panel">
                <div id="sb_overview">&#160;</div>
            </div>
            <div id="sb_ui_left_panel">
                <div id="cga_help_wrap"><div id="cga_help">&#160;</div></div>
                <div id="sb_link_wrap"><div id="sb_link">&#160;</div></div>
                <div id="sb_popup_wrap"><div id="sb_popup">&#160;</div></div>
                <div id="sb_object_info">&#160;</div>
            </div>
        </div>
        <div id="sb_left_buttons_wrapper">
            <div id="cga_help_button">Help</div>
            <div id="sb_link_button">Link to this scene</div>
        </div>
    </div>
</body>
</html>
</jsp:root>

