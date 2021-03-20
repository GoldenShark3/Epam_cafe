package com.epam.jwd.cafe.handler.impl;

import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.handler.AbstractHandler;
import com.epam.jwd.cafe.handler.Handler;
import com.epam.jwd.cafe.util.LocalizationMessage;
import com.mysql.cj.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class ReviewHandler extends AbstractHandler {

    public ReviewHandler(Handler nextHandler) {
        super(nextHandler);
    }

    public ReviewHandler() {
    }

    @Override
    public Set<String> handleRequest(RequestContext requestContext) {
        Set<String> errorMessages = new HashSet<>();
        String review = requestContext.getRequestParameters().get(RequestConstant.REVIEW);
        if (StringUtils.isNullOrEmpty(review) || review.length() > 2048) {
            errorMessages.add(LocalizationMessage.localize(requestContext.getLocale(), "serverMessage.review"));
        }

        if (nextHandler != null) {
            errorMessages.addAll(nextHandler.handleRequest(requestContext));
        }
        return errorMessages;
    }
}
