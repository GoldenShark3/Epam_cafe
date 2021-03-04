package com.epam.jwd.cafe.handler.impl;

import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.handler.AbstractHandler;
import com.epam.jwd.cafe.handler.Handler;
import com.epam.jwd.cafe.util.LocalizationMessage;
import com.mysql.cj.util.StringUtils;
import java.util.HashSet;
import java.util.Set;

public class MatchingPasswordsHandler extends AbstractHandler {

    public MatchingPasswordsHandler(Handler nextHandler) {
        super(nextHandler);
    }

    public MatchingPasswordsHandler() {
    }

    @Override
    public Set<String> handleRequest(RequestContext requestContext) {
        Set<String> errorMessages = new HashSet<>();
        String password = requestContext.getRequestParameters().get(RequestConstant.PASSWORD);
        String repeatPassword = requestContext.getRequestParameters().get(RequestConstant.REPEAT_PASSWORD);

        if (StringUtils.isNullOrEmpty(password) || StringUtils.isNullOrEmpty(repeatPassword)) {
            errorMessages.add(LocalizationMessage.localize(requestContext.getLocale(), "error.password"));
        } else if (!password.equals(repeatPassword)) {
            errorMessages.add(LocalizationMessage.localize(requestContext.getLocale(), "error.repeatPassword"));
        }

        if (nextHandler != null) {
            errorMessages.addAll(nextHandler.handleRequest(requestContext));
        }

        return errorMessages;

    }
}
