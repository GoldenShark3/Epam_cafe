package com.epam.jwd.cafe.handler;

import com.epam.jwd.cafe.command.RequestContext;

import java.util.Set;

public interface Handler {
    Set<String> handleRequest(RequestContext requestContext);
}
