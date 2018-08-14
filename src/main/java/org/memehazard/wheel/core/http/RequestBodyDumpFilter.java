package org.memehazard.wheel.core.http;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestBodyDumpFilter implements Filter
{
    private Logger log = LoggerFactory.getLogger(this.getClass());


    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        log.info("-- Start Request body for " + httpRequest.getServletPath());

        HttpRequestWrapper wrapper = new HttpRequestWrapper(httpRequest);
        Reader r = wrapper.getReader();
        for (String s : IOUtils.readLines(r))
        {
            log.info(":: BODY :: " + s);
        }

        log.info("-- End Request body for " + httpRequest.getServletPath());

        chain.doFilter(req, res);
    }


    public void init(FilterConfig filterConfig)
    {
    }


    public void destroy()
    {
    }


    public static class HttpRequestWrapper extends HttpServletRequestWrapper
    {
        private final String body;


        public HttpRequestWrapper(HttpServletRequest request) throws IOException
        {
            super(request);
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = null;
            try
            {
                InputStream inputStream = request.getInputStream();
                if (inputStream != null)
                {
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    char[] charBuffer = new char[128];
                    int bytesRead = -1;
                    while ((bytesRead = bufferedReader.read(charBuffer)) > 0)
                    {
                        stringBuilder.append(charBuffer, 0, bytesRead);
                    }
                }
                else
                {
                    stringBuilder.append("");
                }
            }
            catch (IOException ex)
            {
                throw ex;
            }
            finally
            {
                if (bufferedReader != null)
                {
                    try
                    {
                        bufferedReader.close();
                    }
                    catch (IOException ex)
                    {
                        throw ex;
                    }
                }
            }
            body = stringBuilder.toString();
        }


        @Override
        public ServletInputStream getInputStream() throws IOException
        {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
            ServletInputStream servletInputStream = new ServletInputStream()
            {
                public int read() throws IOException
                {
                    return byteArrayInputStream.read();
                }
            };
            return servletInputStream;
        }


        @Override
        public BufferedReader getReader() throws IOException
        {
            return new BufferedReader(new InputStreamReader(this.getInputStream()));
        }


        public String getBody()
        {
            return this.body;
        }
    }
}