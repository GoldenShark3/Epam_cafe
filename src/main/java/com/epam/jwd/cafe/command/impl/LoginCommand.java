package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.CommandManager;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.RestResponseType;
import com.epam.jwd.cafe.command.constant.PageConstant;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.handler.Handler;
import com.epam.jwd.cafe.handler.impl.PasswordHandler;
import com.epam.jwd.cafe.handler.impl.UsernameHandler;
import com.epam.jwd.cafe.service.UserService;
import com.epam.jwd.cafe.util.LocalizationMessage;
import com.epam.jwd.cafe.util.PasswordEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * The class provides authorized {@link com.epam.jwd.cafe.model.User}
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class LoginCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(LoginCommand.class);
    private static final UserService USER_SERVICE = UserService.INSTANCE;
    private static final Handler LOGIN_HANDLER = new UsernameHandler(new PasswordHandler());


    @Override
    public ResponseContext execute(RequestContext request) {
        Set<String> errorMessages = LOGIN_HANDLER.handleRequest(request);
        ResponseContext responseContext;

        if (errorMessages.isEmpty()) {
            String username = request.getRequestParameters().get(RequestConstant.USERNAME);
            String password = request.getRequestParameters().get(RequestConstant.PASSWORD);
            password = PasswordEncoder.encryptPassword(password);
            try {
                Map<String, Object> responseSession = new HashMap<>();
                Map<String, Object> requestMap = new HashMap<>();
                Optional<String> serverMessage = USER_SERVICE.loginUser(username, password, responseSession);

                if (!serverMessage.isPresent()) {
                    requestMap.put(RequestConstant.REDIRECT_COMMAND, CommandManager.TO_MAIN.getCommandName());
                    responseContext = new ResponseContext(new RestResponseType(), requestMap, responseSession);
                } else {
                    requestMap.put(RequestConstant.SERVER_MESSAGE, LocalizationMessage.localize(request.getLocale(), serverMessage.get()));
                    responseContext = new ResponseContext(new RestResponseType(), requestMap);
                }

            } catch (ServiceException e) {
                LOGGER.error("Failed login user", e);
                responseContext = new ResponseContext(new ForwardResponseType(PageConstant.ERROR_PAGE));
            }

        } else {
            Map<String, Object> map = new HashMap<>();
            map.put(RequestConstant.ERROR_MESSAGE, errorMessages);
            responseContext = new ResponseContext(new RestResponseType(), map);
        }
        return responseContext;
    }
}
