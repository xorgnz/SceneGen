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
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>Quiz game</title>
    
    <c:url var="base_url" value="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />
    <base href="${base_url}" />
    
    <link media="screen" type="text/css" rel="stylesheet" href="${base_url}styles/cga.css" />
    <link media="screen" type="text/css" rel="stylesheet" href="${base_url}styles/quiz.css" />

    <script src="${base_url}scripts/3rdparty/three.min.56.js">;</script>
    <script src="${base_url}scripts/3rdparty/obj_loader.js">;</script>
    <script src="${base_url}scripts/3rdparty/jquery-1.10.2.js">;</script>
    <script src="${base_url}scripts/3rdparty/lodash.js">;</script>
    <script src="${base_url}scripts/3rdparty/jquery.mousewheel.min.js">;</script>

    <script src="${base_url}scripts/utils/DOMUtils.js">;</script>
    <script src="${base_url}scripts/utils/Lang.js">;</script>
    <script src="${base_url}scripts/utils/UI_Panel.js">;</script>

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

    <script src="${base_url}scripts/quiz/Quiz_Communicator.js">;</script>
    <script src="${base_url}scripts/quiz/Quiz_Controller.js">;</script>
    <script src="${base_url}scripts/quiz/Quiz_Lens.js">;</script>
    <script src="${base_url}scripts/quiz/Quiz_Main.js">;</script>
    <script src="${base_url}scripts/quiz/Quiz_QuizMaster.js">;</script>
    <script src="${base_url}scripts/quiz/QuizState_Introduction.js">;</script>
    <script src="${base_url}scripts/quiz/QuizState_Loading.js">;</script>
    <script src="${base_url}scripts/quiz/QuizState_QuestionCorrect.js">;</script>
    <script src="${base_url}scripts/quiz/QuizState_QuestionInit.js">;</script>
    <script src="${base_url}scripts/quiz/QuizState_QuestionLabelSelect.js">;</script>
    <script src="${base_url}scripts/quiz/QuizState_QuestionLabelSelectIncorrect.js">;</script>
    <script src="${base_url}scripts/quiz/QuizState_QuestionLabelSelectShowAnswer.js">;</script>
    <script src="${base_url}scripts/quiz/QuizState_QuestionLabelSelectSubmit.js">;</script>
    <script src="${base_url}scripts/quiz/QuizState_QuestionModelSelect.js">;</script>
    <script src="${base_url}scripts/quiz/QuizState_QuestionModelSelectIncorrect.js">;</script>
    <script src="${base_url}scripts/quiz/QuizState_QuestionModelSelectShowAnswer.js">;</script>
    <script src="${base_url}scripts/quiz/QuizState_QuestionModelSelectSubmit.js">;</script>
    <script src="${base_url}scripts/quiz/QuizState_QuizInit.js">;</script>
    <script src="${base_url}scripts/quiz/QuizState_Summary.js">;</script>

    <script>
    
    
        var cgaObjectDescriptors = 
            [<c:forEach var="scnod" items="${scene.sceneObjectDescriptors}">
            <c:if test="${scnod.asset.objFilename != '' and scnod.visible}">
            new CGA_ObjectDescriptor(
                ${scnod.entity.id},
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
                ${scnod.visible}, { id: ${scnod.entity.id} }),
            </c:if>
            </c:forEach>];
    
        <c:if test="${errorMessage != null}">var errorMessage = "${errorMessage}"</c:if>

         // Globals
        "use strict";

        // Adjust this value to select the number of loaders to use in loading mesh data
        //
        // MeshManager.LOADER_COUNT = 5;

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
                url: "",
            };

            // Configure Quiz
            var quizConfig =
            {
                elements:
                {
                    main_panel: document.getElementById("quiz_panel"),
                },
                quizLength: ${quiz.length},
                name: "${quiz.name}",
                quizItems:
                [
                    <c:forEach var="question" items="${quiz.questions}">
                    { factId: ${question.factId}, id: ${question.id}, name: "${question.name}", p: ${question.weight}},
                    </c:forEach>
                ],
                serverURL: "${base_url}",
                redirectUrl: "${quiz.redirectUrl}",
                studentId: ${quiz.studentId},
                cgaObjectDescriptors: cgaObjectDescriptors,
            };

            // Initialize graphics application
            var cgaApp = new CGA_Main(cgaConfig);
            cgaApp.addController("base", new CGA_BaseController(cgaApp));

            // Load Quiz Application
            var quizApp = new Quiz_Main(quizConfig, cgaApp);
        });

    </script>
</head>
<body>
    <div id="cga_gfx_container">
        <div id="cga_lpp_wrap"><div id="cga_lpp">&#160;</div></div>

        <div id="quiz_panel_wrap"><div id="quiz_panel">&#160;</div></div>

        <div id="cga_help_wrap"><div id="cga_help"><a href="javasript: alert('no');" target="_blank">View full screen</a></div></div>

        <div id="quiz_left_buttons_wrapper">
            <div id="cga_help_button">Help</div>
        </div>
    </div>
</body>
</html>
</jsp:root>