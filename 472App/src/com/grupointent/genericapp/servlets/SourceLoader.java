/**
 * 
 */

package com.grupointent.genericapp.servlets;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class SourceLoader extends GenericServlet
{

    private static final long serialVersionUID = 0x4021c3e0282bbe78L;
    private String prefix;
    private static final int BUFFER = 1024;
    public static final String BASE = "/net/sourceforge/ajaxtags";

    public SourceLoader()
    {
        prefix = null;
    }

    public void service(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws ServletException, IOException
    {
        String s = httpservletrequest.getRequestURI();
        String s1 = s.substring(httpservletrequest.getContextPath().length());
        if(prefix != null && s1.startsWith(prefix))
        {
            s1 = s1.substring(prefix.length());
        }
        InputStream inputstream = getClass().getResourceAsStream((new StringBuilder()).append("/net/sourceforge/ajaxtags").append(s1).toString());
        if(inputstream == null)
        {
            throw new IOException("resource not found");
        }
        javax.servlet.ServletOutputStream servletoutputstream = httpservletresponse.getOutputStream();
        byte abyte0[] = new byte[1024];
        for(int i = -1; (i = inputstream.read(abyte0)) != -1;)
        {
            servletoutputstream.write(abyte0, 0, i);
        }
        inputstream.close();
        

    }

    public void service(ServletRequest servletrequest, ServletResponse servletresponse)
        throws ServletException, IOException
    {
        service((HttpServletRequest)servletrequest, (HttpServletResponse)servletresponse);
    }

    public void init()
        throws ServletException
    {
        prefix = getInitParameter("prefix");
        if(prefix != null && prefix.trim().length() == 0)
        {
            prefix = null;
        }
    }
}
