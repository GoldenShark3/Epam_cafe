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
import com.epam.jwd.cafe.model.User;
import com.epam.jwd.cafe.model.dto.UserDto;
import com.epam.jwd.cafe.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AddBalanceCommand implements Command, UserCommand {
    private static final Logger LOGGER = LogManager.getLogger(AddBalanceCommand.class);
    private static final Integer ADD_DOLLAR_TO_USER = 50;
    private static final UserService USER_SERVICE = UserService.INSTANCE;

    @Override
    public ResponseContext execute(RequestContext request) {
        Map<String, Object> requestMap = new HashMap<>();
        UserDto userDto = (UserDto) request.getSessionAttributes().get(RequestConstant.USER);
        try {
            Optional<User> userOptional = USER_SERVICE.findById(userDto.getId());
            if (userOptional.isPresent()) {
                User user = retrieveUserWithUpdatedBalance(userOptional.get());
                USER_SERVICE.updateUser(user);
                requestMap.put(RequestConstant.REDIRECT_COMMAND, CommandManager.TO_PROFILE.getCommandName());
                return new ResponseContext(new RestResponseType(), requestMap, new HashMap<>());
            }
        } catch (ServiceException e) {
            LOGGER.error("Failed to add money to balance of user with id:" + userDto.getId());
        }
        return new ResponseContext(new ForwardResponseType(PageConstant.ERROR_PAGE), requestMap, new HashMap<>());
    }

    private User retrieveUserWithUpdatedBalance(User user) {
        return User.builder()
                .withId(user.getId())
                .withFirstName(user.getFirstName())
                .withLastName(user.getLastName())
                .withUsername(user.getUsername())
                .withEmail(user.getEmail())
                .withPassword(user.getPassword())
                .withBalance(user.getBalance().add(BigDecimal.valueOf(ADD_DOLLAR_TO_USER)))
                .withRole(user.getRole())
                .withIsBlocked(user.getIsBlocked())
                .withLoyaltyPoints(user.getLoyaltyPoints())
                .withPhoneNumber(user.getPhoneNumber())
                .build();

    }
}
