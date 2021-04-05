package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.constant.PageConstant;

import java.util.HashMap;

/**
 * The class provides moving to 'not found' page, when page is not found
 *
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class ToNotFoundPageCommand implements Command {
    public final static ToNotFoundPageCommand INSTANCE = new ToNotFoundPageCommand();

    private ToNotFoundPageCommand() {
    }

    @Override
    public ResponseContext execute(RequestContext request) {
        return new ResponseContext(new ForwardResponseType(PageConstant.ERROR_404), new HashMap<>(), new HashMap<>());
    }
}
