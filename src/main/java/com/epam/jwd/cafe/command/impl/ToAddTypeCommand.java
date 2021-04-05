package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.constant.PageConstant;
import com.epam.jwd.cafe.command.marker.AdminCommand;

import java.util.HashMap;

/**
 * The class provides moving admin to page for add new product type
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class ToAddTypeCommand implements Command, AdminCommand {

    @Override
    public ResponseContext execute(RequestContext request) {
        return new ResponseContext(new ForwardResponseType(PageConstant.ADD_TYPE_PRODUCT), new HashMap<>(), new HashMap<>());
    }
}
