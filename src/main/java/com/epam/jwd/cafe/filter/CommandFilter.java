package com.epam.jwd.cafe.filter;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.command.marker.AdminCommand;
import com.epam.jwd.cafe.command.marker.UserCommand;
import com.epam.jwd.cafe.model.Role;
import com.epam.jwd.cafe.model.dto.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * The class filters {@link Command} and blocked user without access
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */

@WebFilter(filterName = "CommandFilter",
        initParams = {@WebInitParam(name = "command", value = "to_access_blocked")})
public class CommandFilter implements Filter {

    private static final Logger LOGGER = LogManager.getLogger(CommandFilter.class);
    private String accessBlockedCommand;

    @Override
    public void init(FilterConfig filterConfig) {
        accessBlockedCommand = filterConfig.getServletContext().getContextPath()
                + filterConfig.getInitParameter(RequestConstant.COMMAND);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        UserDto user = (UserDto) session.getAttribute(RequestConstant.USER);

        if (isUserHasAccess(request, user)) {
            filterChain.doFilter(request, response);
        } else {
            LOGGER.warn("Access blocked to user");
            response.sendRedirect(request.getServletPath() + "?command=" + accessBlockedCommand);
        }
    }

    @Override
    public void destroy() {
        accessBlockedCommand = null;
    }

    private boolean isUserHasAccess(HttpServletRequest request, UserDto user) {
        boolean isHasAccess = true;
        String commandName = request.getParameter(RequestConstant.COMMAND);

        if (commandName != null) {
            Command command = Command.of(commandName.toUpperCase());
            if (user == null && (command instanceof UserCommand
                    || command instanceof AdminCommand)) {
                isHasAccess = false;
            }
            if (user != null && command instanceof AdminCommand
                    && user.getRole().equals(Role.USER)) {
                isHasAccess = false;
            }
        }
        return isHasAccess;
    }
}
