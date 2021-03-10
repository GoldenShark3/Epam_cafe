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
import com.epam.jwd.cafe.handler.impl.EmailHandler;
import com.epam.jwd.cafe.handler.impl.MatchingPasswordsHandler;
import com.epam.jwd.cafe.handler.impl.NameHandler;
import com.epam.jwd.cafe.handler.impl.PasswordHandler;
import com.epam.jwd.cafe.handler.impl.PhoneNumberHandler;
import com.epam.jwd.cafe.handler.impl.UsernameHandler;
import com.epam.jwd.cafe.model.Role;
import com.epam.jwd.cafe.model.User;
import com.epam.jwd.cafe.service.UserService;
import com.epam.jwd.cafe.util.LocalizationMessage;
import com.epam.jwd.cafe.util.PasswordEncoder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class RegistrationCommand implements Command {
    private static final UserService USER_SERVICE = UserService.INSTANCE;
    private static final Handler REGISTRATION_HANDLER = new NameHandler(new PasswordHandler(
            new MatchingPasswordsHandler(
                    new EmailHandler(new PhoneNumberHandler(new UsernameHandler())))
    ));


    @Override
    public ResponseContext execute(RequestContext request) {
        Set<String> errorMessages = REGISTRATION_HANDLER.handleRequest(request);
        ResponseContext responseContext;

        if (errorMessages.isEmpty()) {
            String firstName = request.getRequestParameters().get(RequestConstant.FIRST_NAME);
            String lastName = request.getRequestParameters().get(RequestConstant.LAST_NAME);
            String username = request.getRequestParameters().get(RequestConstant.USERNAME);
            String email = request.getRequestParameters().get(RequestConstant.EMAIL);
            String phoneNumber = request.getRequestParameters().get(RequestConstant.PHONE_NUMBER);
            String password = request.getRequestParameters().get(RequestConstant.PASSWORD);

            User user = User.builder()
                    .withFirstName(firstName)
                    .withLastName(lastName)
                    .withUsername(username)
                    .withEmail(email)
                    .withPhoneNumber(phoneNumber)
                    .withPassword(PasswordEncoder.encryptPassword(password))
                    .withBalance(BigDecimal.ZERO)
                    .withRole(Role.USER)
                    .withIsBlocked(false)
                    .withLoyaltyPoints(0)
                    .build();
            try {
                Optional<String> serverMessage = USER_SERVICE.registerUser(user);
                Map<String, Object> requestMap = new HashMap<>();

                if (!serverMessage.isPresent()) {
                    Map<String, Object> sessionMap = new HashMap<>();
                    sessionMap.put("user", user);
                    requestMap.put(RequestConstant.REDIRECT_COMMAND, CommandManager.TO_MAIN.getCommandName());
                    responseContext = new ResponseContext(new RestResponseType(), requestMap, sessionMap);
                } else {
                    requestMap.put(RequestConstant.SERVER_MESSAGE, LocalizationMessage.localize(request.getLocale(), serverMessage.get()));
                    responseContext = new ResponseContext(new RestResponseType(), requestMap);
                }
            } catch (ServiceException e) {
//                todo: log.error("Registration failed" + e);
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
