package com.epam.jwd.cafe.handler.impl;

import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.handler.AbstractHandler;
import com.epam.jwd.cafe.handler.Handler;
import com.epam.jwd.cafe.util.LocalizationMessage;
import com.mysql.cj.util.StringUtils;
import java.util.HashSet;
import java.util.Set;

public class PriceHandler extends AbstractHandler {
    private static final String PRICE_REGEX = "^([0-9]{1,3}\\.[0-9]{1,2})$";


    public PriceHandler(Handler nextHandler) {
        super(nextHandler);
    }

    public PriceHandler() {
    }

    @Override
    public Set<String> handleRequest(RequestContext requestContext) {
        Set<String> error_messages = new HashSet<>();
        String price = requestContext.getRequestParameters().get(RequestConstant.PRODUCT_PRICE);

        if (StringUtils.isNullOrEmpty(price) || !price.matches(PRICE_REGEX)) {
            error_messages.add(LocalizationMessage.localize(requestContext.getLocale(), "serverMessage.price"));
        }

        if (nextHandler != null) {
            error_messages.addAll(nextHandler.handleRequest(requestContext));
        }
        return error_messages;
    }
}
