package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;

import java.util.HashMap;

public class ToRegistrationCommand implements Command {

    @Override
    public ResponseContext execute(RequestContext request) {
        return new ResponseContext(new ForwardResponseType("/WEB-INF/pages/registration.jsp"), new HashMap<>(), new HashMap<>());
    }
}
