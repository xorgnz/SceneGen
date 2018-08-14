package org.memehazard.servlet.http;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.text.StrBuilder;
import org.memehazard.util.IterableEnumeration;

public class HttpServletUtils
{
    public static String requestToString(HttpServletRequest request)
    {
        StrBuilder sb = new StrBuilder();

        sb.appendln("HTTP Request: " + request.getServletPath());
        
        @SuppressWarnings("unchecked")
        IterableEnumeration<String> itHeaders = new IterableEnumeration<String>(
                (request.getHeaderNames()));
        sb.appendln("  Headers:");
        for (String s : itHeaders)
        {
            sb.appendln(String.format("    %s = %s", s, request.getHeader(s)));
        }
        
        @SuppressWarnings("unchecked")
        IterableEnumeration<String> itAttributes = new IterableEnumeration<String>(
                (request.getAttributeNames()));
        sb.appendln("  Attributes:");
        for (String s : itAttributes)
        {
            sb.appendln(String.format("    %s = %s", s, request.getAttribute(s)));
        }

        @SuppressWarnings("unchecked")
        IterableEnumeration<String> itParameters = new IterableEnumeration<String>(
                (request.getParameterNames()));
        sb.appendln("  Parameters:");
        for (String s : itParameters)
        {
            sb.appendln(String.format("    %s = %s", s, request.getParameter(s)));
        }

        return sb.toString();
    }
}
