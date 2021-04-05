package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.CommandManager;
import com.epam.jwd.cafe.command.RedirectResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.command.marker.UserCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * The class provides log out {@link com.epam.jwd.cafe.model.User} and removes him from {@link javax.servlet.http.HttpSession}
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class LogoutCommand implements Command, UserCommand {

    @Override
    public ResponseContext execute(RequestContext request) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put(RequestConstant.LOGOUT, "logout");
        return new ResponseContext(new RedirectResponseType(CommandManager.TO_MAIN.getCommandName()),
                requestMap, new HashMap<>());
    }
}
