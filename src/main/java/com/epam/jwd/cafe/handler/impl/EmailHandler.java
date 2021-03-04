package com.epam.jwd.cafe.handler.impl;

import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.handler.AbstractHandler;
import com.epam.jwd.cafe.handler.Handler;
import com.epam.jwd.cafe.util.LocalizationMessage;
import com.mysql.cj.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class EmailHandler extends AbstractHandler {
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+";

    public EmailHandler(Handler nextHandler) {
        super(nextHandler);
    }

    public EmailHandler() {
    }

    @Override
    public Set<String> handleRequest(RequestContext requestContext) {
        Set<String> errorMessages = new HashSet<>();
        String email = requestContext.getRequestParameters().get(RequestConstant.EMAIL);

        if (StringUtils.isNullOrEmpty(email) || !email.matches(EMAIL_PATTERN)) {
            errorMessages.add(LocalizationMessage.localize(requestContext.getLocale(), "error.mail"));
        }

        if (nextHandler != null) {
            errorMessages.addAll(nextHandler.handleRequest(requestContext));
        }

        return errorMessages;
    }
}
