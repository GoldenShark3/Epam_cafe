package com.epam.jwd.cafe.handler.impl;

import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.handler.AbstractHandler;
import com.epam.jwd.cafe.handler.Handler;
import com.epam.jwd.cafe.util.LocalizationMessage;
import com.mysql.cj.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class PasswordHandler extends AbstractHandler {
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,}";

    public PasswordHandler(Handler nextHandler) {
        super(nextHandler);
    }

    public PasswordHandler() {
    }

    @Override
    public Set<String> handleRequest(RequestContext requestContext) {
        Set<String> error_messages = new HashSet<>();
        String password = requestContext.getRequestParameters().get(RequestConstant.PASSWORD);

        if (StringUtils.isNullOrEmpty(password) || !password.matches(PASSWORD_PATTERN)) {
            error_messages.add(LocalizationMessage.localize(requestContext.getLocale(), "error.password"));
        }

        if (nextHandler != null) {
            error_messages.addAll(nextHandler.handleRequest(requestContext));
        }

        return error_messages;
    }
}
