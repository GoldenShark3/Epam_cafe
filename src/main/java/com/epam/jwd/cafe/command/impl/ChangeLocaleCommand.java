package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import java.util.HashMap;
import java.util.Map;

public class ChangeLocaleCommand implements Command {

    @Override
    public ResponseContext execute(RequestContext request) {
        String locale = request.getRequestParameters().get(RequestConstant.LOCALE);
        String currPage = request.getRequestParameters().get(RequestConstant.CURRENT_PAGE);
        Map<String, Object> map = new HashMap<>();
        map.put(RequestConstant.LOCALE, locale);
        return new ResponseContext(new ForwardResponseType(currPage), new HashMap<>(), map);
    }
}
