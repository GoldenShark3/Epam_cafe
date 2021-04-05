package com.epam.jwd.cafe.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * The class defending from js injection
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */

@WebFilter(filterName = "XssAttackFilter")
public class XssAttackFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(new XssWrapper((HttpServletRequest) servletRequest), servletResponse);
    }

}
