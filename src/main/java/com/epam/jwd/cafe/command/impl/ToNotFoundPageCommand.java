package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.constant.PageConstant;

import java.util.HashMap;

public class ToNotFoundPageCommand implements Command {
    public final static ToNotFoundPageCommand INSTANCE = new ToNotFoundPageCommand();

    private ToNotFoundPageCommand() {
    }

    @Override
    public ResponseContext execute(RequestContext request) {
        return new ResponseContext(new ForwardResponseType(PageConstant.ERROR_404), new HashMap<>(), new HashMap<>());
    }
}
