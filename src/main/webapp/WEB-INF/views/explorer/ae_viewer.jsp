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
    <link rel="stylesheet" href="${base_url}styles/ae.css" type="text/css" media="screen" />
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

    <!-- AE - Classes -->
    <script src="${base_url}scripts/ae/AE_CommandPanel.js">;</script>
    <script src="${base_url}scripts/ae/AE_Communicator.js">;</script>
    <script src="${base_url}scripts/ae/AE_Config.js">;</script>
    <script src="${base_url}scripts/ae/AE_Controller.js">;</script>
    <script src="${base_url}scripts/ae/AE_DrillPanel.js">;</script>
    <script src="${base_url}scripts/ae/AE_Entity.js">;</script>
    <script src="${base_url}scripts/ae/AE_EntityRegistry.js">;</script>
    <script src="${base_url}scripts/ae/AE_ErrorPanel.js">;</script>
    <script src="${base_url}scripts/ae/AE_ExplorationPanel.js">;</script>
    <script src="${base_url}scripts/ae/AE_Explorer.js">;</script>
    <script src="${base_url}scripts/ae/AE_Historian.js">;</script>
    <script src="${base_url}scripts/ae/AE_HistoryPanel.js">;</script>
    <script src="${base_url}scripts/ae/AE_Lens.js">;</script>
    <script src="${base_url}scripts/ae/AE_Main.js">;</script>
    <script src="${base_url}scripts/ae/AE_Persistor.js">;</script>    
    <script src="${base_url}scripts/ae/AE_PickingPanel.js">;</script>
    <script src="${base_url}scripts/ae/AE_SavePanel.js">;</script>
    <script src="${base_url}scripts/ae/AE_SceneManager.js">;</script>
    <script src="${base_url}scripts/ae/AE_SettingsPanel.js">;</script>

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
            };

            // Configure Explorer
            var aeConfig = new AE_Config(
            {
                stylesheetId: ${stylesheetId},
                assetSetId: ${assetSetId},
                serverURL: "${base_url}",
            });
            
            // Set starting entity
            var startingEntityIds = [ ${entityId} ];

            // Configure text completion
            SG_TextCompletion.serverURL = aeConfig.serverURL;

            // Load application
            var cgaApp = new CGA_Main(cgaConfig);
            cgaApp.addController("base", new CGA_BaseController(cgaApp));
            var explorer = new AE_Main(aeConfig, cgaApp, startingEntityIds);
        });

    </script>
</head>
<body>
    <div id="cga_gfx_container" tabindex="1">
        <div id="ae_ui_container">
            <div id="ae_ui_right_panel">
                <div id="ae_command">&#160;</div>
                <div id="ae_exploration">&#160;</div>
            </div>
            <div id="ae_ui_left_panel">
                <div id="ae_error">&#160;</div>
                <div id="ae_history_wrap">
                    <div id="ae_history">&#160;</div>
                </div>
                <div id="ae_settings_wrap">
                    <div id="ae_settings">&#160;</div>
                </div>
                <div id="ae_drill_wrap">
                    <div id="ae_drill">&#160;</div>
                </div>
                <div id="ae_save_wrap">
                    <div id="ae_save_panel">&#160;</div>
                </div>
                <div id="ae_picking">&#160;</div>
                <div id="ae_left_buttons_wrapper">
                    <div id="cga_help_button">Help</div>
                    <div id="ae_save_button" class="ae_left_button">Load / Save</div>
                    <div id="ae_clear_button" class="ae_left_button">Clear</div>
                    <div id="ae_history_button" class="ae_left_button">History</div>
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