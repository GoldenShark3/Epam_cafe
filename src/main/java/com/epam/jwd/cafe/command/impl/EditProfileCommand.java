package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.CommandManager;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.RestResponseType;
import com.epam.jwd.cafe.command.constant.PageConstant;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.command.marker.UserCommand;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.handler.Handler;
import com.epam.jwd.cafe.handler.impl.NameHandler;
import com.epam.jwd.cafe.handler.impl.PhoneNumberHandler;
import com.epam.jwd.cafe.model.User;
import com.epam.jwd.cafe.model.dto.UserDto;
import com.epam.jwd.cafe.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class EditProfileCommand implements Command, UserCommand {
    private static final Logger LOGGER = LogManager.getLogger(EditProfileCommand.class);
    private static final UserService USER_SERVICE = UserService.INSTANCE;
    private static final Handler EDIT_HANDLER = new NameHandler(new PhoneNumberHandler());

    @Override
    public ResponseContext execute(RequestContext request) {
        Set<String> errorMessages = EDIT_HANDLER.handleRequest(request);
        Map<String, Object> requestMap = new HashMap<>();

        if (errorMessages.isEmpty()) {
            try {
                UserDto userDto = (UserDto) request.getSessionAttributes().get(RequestConstant.USER);
                Optional<User> userOptional = USER_SERVICE.findById(userDto.getId());

                if (userOptional.isPresent()) {

                    User user = User.builder()
                            .withId(userDto.getId())
                            .withFirstName(request.getRequestParameters().get(RequestConstant.FIRST_NAME))
                            .withLastName(request.getRequestParameters().get(RequestConstant.LAST_NAME))
                            .withPhoneNumber(request.getRequestParameters().get(RequestConstant.PHONE_NUMBER))
                            .withPassword(userOptional.get().getPassword())
                            .withUsername(userOptional.get().getUsername())
                            .withRole(userOptional.get().getRole())
                            .withEmail(userOptional.get().getEmail())
                            .withBalance(userOptional.get().getBalance())
                            .withLoyaltyPoints(userOptional.get().getLoyaltyPoints())
                            .build();

                    USER_SERVICE.updateUser(user);

                    requestMap.put(RequestConstant.REDIRECT_COMMAND, CommandManager.TO_PROFILE.getCommandName());
                    return new ResponseContext(new RestResponseType(), requestMap, new HashMap<>());
                }
            } catch (ServiceException e) {
                LOGGER.error("Failed to edit profile", e);
                return new ResponseContext(new ForwardResponseType(PageConstant.ERROR_PAGE));
            }
        }
        requestMap.put(RequestConstant.ERROR_MESSAGE, errorMessages);
        return new ResponseContext(new RestResponseType(), requestMap, new HashMap<>());
    }
}

