package com.epam.jwd.cafe.handler.impl;

import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.handler.AbstractHandler;
import com.epam.jwd.cafe.handler.Handler;
import com.epam.jwd.cafe.util.LocalizationMessage;
import java.util.HashSet;
import java.util.Set;

public class NumberHandler extends AbstractHandler {
    private final String number;

    public NumberHandler(String number) {
        this.number = number;
    }

    public NumberHandler(Handler nextHandler, String number) {
        super(nextHandler);
        this.number = number;
    }

    @Override
    public Set<String> handleRequest(RequestContext requestContext) {
        Set<String> errorMessages = new HashSet<>();
        try {
            Integer.parseInt(number);
            if (Integer.parseInt(number) < 0) {
                errorMessages.add(
                        LocalizationMessage.localize(requestContext.getLocale(), "error.incorrectNumber"));
            }
        } catch (NumberFormatException e) {
            errorMessages.add(
                    LocalizationMessage.localize(requestContext.getLocale(), "error.incorrectNumber"));
        }

        if (nextHandler != null) {
            errorMessages.addAll(nextHandler.handleRequest(requestContext));
        }

        return errorMessages;
    }

}
