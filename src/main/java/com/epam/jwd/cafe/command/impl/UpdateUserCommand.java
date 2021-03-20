package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.RestResponseType;
import com.epam.jwd.cafe.command.constant.PageConstant;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.handler.Handler;
import com.epam.jwd.cafe.handler.impl.NumberHandler;
import com.epam.jwd.cafe.model.User;
import com.epam.jwd.cafe.service.UserService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class UpdateUserCommand implements Command {
    private static final UserService USER_SERVICE = UserService.INSTANCE;

    @Override
    public ResponseContext execute(RequestContext request) {
        Set<String> errorMessages = new NumberHandler(request.getRequestParameters()
                .get(RequestConstant.LOYALTY_POINTS)).handleRequest(request);

        if (errorMessages.isEmpty()) {
            try {
                int id = Integer.parseInt(request.getRequestParameters().get(RequestConstant.ID));
                int loyaltyPoints = Integer.parseInt(request.getRequestParameters().get(RequestConstant.LOYALTY_POINTS));
                boolean isUserBlocked = Boolean.getBoolean(request.getRequestParameters().get(RequestConstant.IS_BLOCKED));
                Optional<User> userOptional = USER_SERVICE.findById(id);

                if (userOptional.isPresent()) {
                    User user = User.builder()
                            .withId(id)
                            .withFirstName(userOptional.get().getFirstName())
                            .withLastName(userOptional.get().getLastName())
                            .withPhoneNumber(userOptional.get().getPhoneNumber())
                            .withPassword(userOptional.get().getPassword())
                            .withUsername(userOptional.get().getUsername())
                            .withRole(userOptional.get().getRole())
                            .withEmail(userOptional.get().getEmail())
                            .withBalance(userOptional.get().getBalance())
                            .withLoyaltyPoints(loyaltyPoints)
                            .withIsBlocked(isUserBlocked)
                            .build();
                    USER_SERVICE.updateUser(user);
                    return new ResponseContext(new RestResponseType(), new HashMap<>(), new HashMap<>());
                } else {
                    return new ResponseContext(new ForwardResponseType(PageConstant.ERROR_PAGE));
                }
            } catch (ServiceException e) {
                //todo: log
            }
        }
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put(RequestConstant.ERROR_MESSAGE, errorMessages);
        return new ResponseContext(new RestResponseType(), requestMap, new HashMap<>());
    }
}
