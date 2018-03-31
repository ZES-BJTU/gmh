package com.zes.squad.gmh.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class CrossOriginFilter implements Filter {

    private static final int DEFAULT_MAX_AGE = 60 * 60;//使用CORS预检请求有效期

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        servletResponse.addHeader("Access-Control-Allow-Origin", "http://localhost:80");
        servletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        servletResponse.addHeader("Access-Control-Allow-Headers",
                "X-token,X-Requested-With,Content-Type,Authorization,Accept");
        servletResponse.addHeader("Access-Control-Allow-Methods",
                "head,Head,HEAD,options,Options,OPTIONS,get,Get,GET,patch,Patch,PATCH,post,Post,POST,put,Put,PUT,delete,Delete,DELETE");
        servletResponse.addHeader("Access-Control-Max-Age", String.valueOf(DEFAULT_MAX_AGE));
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}

}
