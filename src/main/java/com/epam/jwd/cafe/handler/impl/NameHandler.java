package com.epam.jwd.cafe.handler.impl;

import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.handler.AbstractHandler;
import com.epam.jwd.cafe.handler.Handler;
import com.epam.jwd.cafe.util.LocalizationMessage;
import com.mysql.cj.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class NameHandler extends AbstractHandler {
    private static final String NAME_PATTERN = "^[A-Za-zА-Яа-яЁё']{2,20}";

    public NameHandler(Handler nextHandler) {
        super(nextHandler);
    }

    public NameHandler() {
    }

    @Override
    public Set<String> handleRequest(RequestContext requestContext) {
        Set<String> errorMessages = new HashSet<>();
        String firstName = requestContext.getRequestParameters().get(RequestConstant.FIRST_NAME);
        String lastName = requestContext.getRequestParameters().get(RequestConstant.LAST_NAME);

        if (StringUtils.isNullOrEmpty(firstName) || StringUtils.isNullOrEmpty(lastName)
                || !firstName.matches(NAME_PATTERN) || !lastName.matches(NAME_PATTERN)) {
            errorMessages.add(LocalizationMessage.localize(requestContext.getLocale(), "error.name"));
        }

        if (nextHandler != null) {
            errorMessages.addAll(nextHandler.handleRequest(requestContext));
        }

        return errorMessages;
    }
}
