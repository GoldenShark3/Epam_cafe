package com.epam.jwd.cafe.handler.impl;

import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.handler.AbstractHandler;
import com.epam.jwd.cafe.handler.Handler;
import com.epam.jwd.cafe.util.LocalizationMessage;
import com.mysql.cj.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class DescriptionHandler extends AbstractHandler {

    public DescriptionHandler(Handler nextHandler) {
        super(nextHandler);
    }

    public DescriptionHandler() {
    }

    @Override
    public Set<String> handleRequest(RequestContext requestContext) {
        Set<String> errorMessages = new HashSet<>();
        String description = requestContext.getRequestParameters().get(RequestConstant.PRODUCT_DESCRIPTION);

        if (StringUtils.isNullOrEmpty(description) || description.length() < 4 || description.length() > 80) {
            errorMessages.add(LocalizationMessage.localize(requestContext.getLocale(), "serverMessage.description"));
        }

        if (nextHandler != null) {
            errorMessages.addAll(nextHandler.handleRequest(requestContext));
        }
        return errorMessages;
    }
}
