<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page" 
    xmlns:c="http://java.sun.com/jsp/jstl/core"
    xmlns="http://www.w3.org/1999/xhtml" version="2.0">

<jsp:directive.page contentType="text/html" pageEncoding="UTF-8" />
<jsp:output 
    doctype-root-element="html" 
    doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN" 
    doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" />

<html>
<head>
    <title>Admin - ${pageTitle}</title>

    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <c:url var="base_url" value="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />
    <base href="${base_url}" />

    <link rel="stylesheet" href="${base_url}styles/admin-base.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="${base_url}styles/admin-shared.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="${base_url}styles/3rdparty/jquery-ui.css" type="text/css" media="screen" />

    <script src="${base_url}scripts/3rdparty/jquery-1.10.2.js">;</script>
    <script src="${base_url}scripts/3rdparty/jquery-ui-1.11.1.js">;</script>    
    <script src="${base_url}scripts/admin/nav.js">;</script>
    
    <c:if test="${not empty refresh}">
        <script language="javascript">
        function refreshPanes()
        {
            var refresh = parent.document.getElementById("${refresh}");
            if (refresh)
                refresh.contentDocument.location.reload();
        }
        </script>
    </c:if>
    <c:if test="${empty refresh}">
        <script language="javascript">
        function refreshPanes()
        {
        	// Do nothing
        	;
        }
        </script>
    </c:if>
    
    <script language="javascript">
    $(document).ready(function ()
    {
    	refreshPanes();
    	configureHierarchyEvents();
        applyTextCompletion();
    });
    

    // Configure hierarchy events. These are events passed up and down the tree of iframes. This mechanism allows 
    // the application to sidestep problems with events being passed from an iframe to its wrapper.
    function configureHierarchyEvents()
    {
        // Receive hierarchy events
        document.receiveHierarchyEvent = function (e)
        {
            // console.log("admin/base.jsp - received hierarchy event");
            // console.log(e);

            var iframes = document.getElementsByTagName("iframe");
            for (var i= 0 ; i &lt; iframes.length ; i++)
            {
                var iframe_cd = iframes[i].contentDocument;
                if (iframe_cd.receiveHierarchyEvent)
                    iframe_cd.receiveHierarchyEvent(e);
            }
        }
        
        
        // Distribute hierarchy events
        $(document).keydown(function (e) 
        {
            // console.log("admin/base.jsp - dispatch hierarchy event ");
            // console.log(e);
            
            var iframes = document.getElementsByTagName("iframe");
            for (var i= 0 ; i &lt; iframes.length ; i++)
            {
                var iframe_cd = iframes[i].contentDocument;
                if (iframe_cd.receiveHierarchyEvent)
                    iframe_cd.receiveHierarchyEvent(e);
            }
        } );
    }
    
    
    // Add text completion functionality to all marked text inputs.
    // Note - this is in a separate function so that it can be called when the page is modified.
    function applyTextCompletion ()
    {
        // Apply text completion to marked fields
        $( ".fma_text_completion" ).autocomplete({
            source: function( request, response )
            {
                $.ajax({
                    // url: "http://gd.geobytes.com/AutoCompleteCity",
                    url: "textCompletion",
                    dataType: "json",
                    data:
                    {
                        prefix: request.term,
                        limit: 100
                    },
                    success: function( data )
                    {
                        console.log(data.completions);
                        response( data.completions );
                    }
                });
            },
            minLength: 4,
        });
    }
    </script>
    
</head>
<body>
    
    <c:choose>
        <c:when test="${not empty pageTitle}">
            <h1>${pageTitle}</h1>
        </c:when>
    </c:choose>
    
    <c:if test="${not empty msg_good}"> 
        <div class="msg_good">${msg_good}</div>
    </c:if>
    
    <c:if test="${not empty msg_bad}"> 
        <div class="msg_bad">
            ${msg_bad}
            <c:if test="${not empty exception}"><br/><br/><pre>${exception}</pre></c:if>
        </div>
    </c:if>
    
    <c:choose>
        <c:when test="${not empty pageFile}">
            <jsp:include page="/WEB-INF/views/${pageFile}.jsp" />
        </c:when>
        <c:otherwise><p>Admin base JSP: No pageFile submitted</p></c:otherwise>
    </c:choose>
</body>
</html>

</jsp:root>