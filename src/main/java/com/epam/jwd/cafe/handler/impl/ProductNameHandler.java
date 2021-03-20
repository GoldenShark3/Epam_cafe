package com.epam.jwd.cafe.handler.impl;

import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.handler.AbstractHandler;
import com.epam.jwd.cafe.handler.Handler;
import com.epam.jwd.cafe.util.LocalizationMessage;
import com.mysql.cj.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class ProductNameHandler extends AbstractHandler {
    private static final String PRODUCT_NAME_PATTERN = "^[A-Za-zа-я-А-я\\s'-]{4,20}?$";

    public ProductNameHandler(Handler nextHandler) {
        super(nextHandler);
    }

    public ProductNameHandler() {
    }

    @Override
    public Set<String> handleRequest(RequestContext requestContext) {
        Set<String> errorMessages = new HashSet<>();
        String name = requestContext.getRequestParameters().get(RequestConstant.PRODUCT_NAME);

        if (StringUtils.isNullOrEmpty(name) || !name.matches(PRODUCT_NAME_PATTERN)) {
            errorMessages.add(LocalizationMessage.localize(requestContext.getLocale(), "serverMessage.productName"));
        }

        if (nextHandler != null) {
            errorMessages.addAll(nextHandler.handleRequest(requestContext));
        }

        return errorMessages;
    }
}
