package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RedirectResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.model.Product;

import java.util.HashMap;
import java.util.Map;

/**
 * The class provides changes {@link com.epam.jwd.cafe.model.User} locale
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class ChangeLocaleCommand implements Command {

    @Override
    public ResponseContext execute(RequestContext request) {
        String locale = request.getRequestParameters().get(RequestConstant.LOCALE);
        String currUrl = request.getRequestParameters().get(RequestConstant.CURRENT_URL);
        Map<String, Object> map = new HashMap<>();
        map.put(RequestConstant.LOCALE, locale);
        return new ResponseContext(new RedirectResponseType(currUrl), new HashMap<>(), map);
    }
}