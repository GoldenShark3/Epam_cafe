package com.epam.jwd.cafe.handler;

import com.epam.jwd.cafe.command.RequestContext;

import java.util.Set;

/**
 * Interface of application handler
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public interface Handler {
    Set<String> handleRequest(RequestContext requestContext);
}
