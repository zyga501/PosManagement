package com.posmanagement.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException{
        HttpServletRequest servletRequest = (HttpServletRequest)request;
        HttpServletResponse servletResponse = (HttpServletResponse)response;
        HttpSession session = servletRequest.getSession();

        String path = servletRequest.getRequestURI();
        if(path.compareTo("/") == 0
                || path.indexOf("/css") > -1
                || path.indexOf("/images") > -1
                || path.indexOf("/js") > -1
                || path.indexOf("/skin") > -1) {
            chain.doFilter(servletRequest, servletResponse);
        }

        if (session.getAttribute("userID") != null) {
            chain.doFilter(servletRequest, servletResponse);
        }
    }

    public void destroy() {
    }
}