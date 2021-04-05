package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.constant.PageConstant;

import java.util.HashMap;

/**
 * The class provides moving to registration page
 *
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class ToRegistrationCommand implements Command {

    @Override
    public ResponseContext execute(RequestContext request) {
        return new ResponseContext(new ForwardResponseType(PageConstant.REGISTRATION_PAGE), new HashMap<>(), new HashMap<>());
    }
}
