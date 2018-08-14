<jsp:root
 xmlns:jsp="http://java.sun.com/JSP/Page"
 xmlns:c="http://java.sun.com/jsp/jstl/core"
 xmlns="http://www.w3.org/1999/xhtml"
 version="2.0">

<jsp:directive.page contentType="text/html" pageEncoding="UTF-8" />
<jsp:output omit-xml-declaration="true" />
<jsp:output doctype-root-element="HTML" doctype-system="about:legacy-compat" />

<c:url var="base_url" value="/" />

<html>
<head>
    <title>${siteTitle} - Admin</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" href="styles/admin-index.css" type="text/css" media="screen" />
    <base href="${base_url}" />
    <script src="scripts/3rdparty/jquery-1.10.2.js">;</script>
    <script>
        $(document).ready(function ()
        {
            // Distribute hierarchy event to child iframes
            $(document).keydown(function (e)
            {
            	// console.log("admin/index.jsp - dispatch hierarchy event ");
            	// console.log(e);)

            	var iframes = document.getElementsByTagName("iframe");
                for (var i= 0 ; i &lt; iframes.length ; i++)
                {
                    var iframe_cd = iframes[i].contentDocument;
                    if (iframe_cd.receiveHierarchyEvent)
                        iframe_cd.receiveHierarchyEvent(e);
                }
            } );
        });
    </script>
</head>
<body>
    <div id="frame-wrapper">
        <iframe name="frame-nav"  id="frame-nav" src="admin/nav">Admin header and nav go here</iframe>
        <iframe name="frame-main" id="frame-main" src="admin/blank-main">Left panel</iframe>
        <iframe name="frame-side" id="frame-side" src="admin/blank-side">Right panel</iframe>
    </div>
</body>
</html>

</jsp:root>