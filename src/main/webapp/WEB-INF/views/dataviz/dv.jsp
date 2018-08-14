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
    <link rel="stylesheet" href="${base_url}styles/dv.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="${base_url}styles/3rdparty/jquery-ui.css" type="text/css" media="screen" />

    <!-- 3rd party JS -->
    <script src="${base_url}scripts/3rdparty/three.min.56.js">;</script>
    <script src="${base_url}scripts/3rdparty/obj_loader.js">;</script>
    <script src="${base_url}scripts/3rdparty/jquery-1.10.2.js">;</script>
    <script src="${base_url}scripts/3rdparty/jquery-ui-1.11.1.js">;</script>
    <script src="${base_url}scripts/3rdparty/jquery.mousewheel.min.js">;</script>
    <script src="${base_url}scripts/3rdparty/lodash.js">;</script>

    <!-- Utility Classes -->
    <script src="${base_url}scripts/utils/DOMUtils.js">;</script>
    <script src="${base_url}scripts/utils/Lang.js">;</script>
    <script src="${base_url}scripts/utils/MultiMap.js">;</script>
    <script src="${base_url}scripts/utils/SG_TextCompletion.js">;</script>
    <script src="${base_url}scripts/utils/TEventDispatcher.js">;</script>
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
    <script src="${base_url}scripts/cga/CGA_UI_HelpPanel.js">;</script>
    <script src="${base_url}scripts/cga/CGA_UI_LoadProgressPanel.js">;</script>

    <!-- DV - Classes -->
    <script src="${base_url}scripts/dv/DV_CommandPanel.js">;</script>
    <script src="${base_url}scripts/dv/DV_Communicator.js">;</script>
    <script src="${base_url}scripts/dv/DV_Config.js">;</script>
    <script src="${base_url}scripts/dv/DV_Controller.js">;</script>
    <script src="${base_url}scripts/dv/DV_InfoPanel.js">;</script>
    <script src="${base_url}scripts/dv/DV_LegendPanel.js">;</script>
    <script src="${base_url}scripts/dv/DV_Lens.js">;</script>
    <script src="${base_url}scripts/dv/DV_Main.js">;</script>
    <script src="${base_url}scripts/dv/DV_Persistor.js">;</script>
    <script src="${base_url}scripts/dv/DV_SavePanel.js">;</script>
    <script src="${base_url}scripts/dv/DV_SceneManager.js">;</script>
    <script src="${base_url}scripts/dv/DV_UI_Region.js">;</script>    
    <script src="${base_url}scripts/dv/DV_Visualizer.js">;</script>

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
                interaction: {},
            };

            // Configure Explorer
            var dvConfig = new DV_Config(
            {
                serverURL: "${base_url}",
            });

            // Configure text completion
            SG_TextCompletion.serverURL = dvConfig.serverURL;

            // Load application
            var cgaApp = new CGA_Main(cgaConfig);
            cgaApp.addController("base", new CGA_BaseController(cgaApp));
            var explorer = new DV_Main(dvConfig, cgaApp);
        });

    </script>
</head>
<body>
    <div id="cga_gfx_container" tabindex="1">
        <div id="dv_ui_container">
            <div id="dv_ui_right_panel">
                <div id="dv_command">&#160;</div>
                <div id="dv_info">&#160;</div>
            </div>
            <div id="dv_ui_left_panel">
                <div id="dv_legend">&#160;</div>
                <div id="dv_left_buttons_wrapper">
                    <div id="cga_help_button">Help</div>
                    <div id="dv_save_button">Save Scene</div>
                </div>
            </div>
            <div id="dv_ui_center_panel">
                <div id="dv_save_wrap">
                    <div id="dv_save_panel">&#160;</div>
                </div>
                <div id="cga_help_wrap">
                    <div id="cga_help">&#160;</div>
                </div>
                <div id="cga_lpp_wrap">
                    <div id="cga_lpp">&#160;</div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
</jsp:root>