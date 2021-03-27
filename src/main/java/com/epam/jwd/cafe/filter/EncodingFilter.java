package com.epam.jwd.cafe.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;

@WebFilter(filterName = "Encoding", urlPatterns = {"/*"}, initParams = {
        @WebInitParam(name = "encoding", value = "UTF-8", description = "Encoding Param")
})
public class EncodingFilter implements Filter {
    private String encoding;

    @Override
    public void init(FilterConfig filterConfig) {
        encoding = filterConfig.getInitParameter("encoding");

        ServletContext context = filterConfig.getServletContext();

        FilterRegistration reqXssFilter = context.getFilterRegistration("XssAttackFilter");
        reqXssFilter.addMappingForUrlPatterns(null, true, "/*");

        FilterRegistration reqAuthFilter = context.getFilterRegistration("CommandFilter");
        reqAuthFilter.addMappingForUrlPatterns(null, true, "/*");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        String codeRequest = servletRequest.getCharacterEncoding();
        if (codeRequest == null || !codeRequest.equalsIgnoreCase(encoding)) {
            servletRequest.setCharacterEncoding(encoding);
        }
        servletResponse.setCharacterEncoding(encoding);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        encoding = null;
    }
}
