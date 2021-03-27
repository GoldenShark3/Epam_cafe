package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.constant.PageConstant;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.command.marker.UserCommand;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.model.User;
import com.epam.jwd.cafe.model.dto.UserDto;
import com.epam.jwd.cafe.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ToProfileCommand implements Command, UserCommand {
    private static final Logger LOGGER = LogManager.getLogger(ToProfileCommand.class);

    @Override
    public ResponseContext execute(RequestContext request) {
        int userId = ((UserDto) (request.getSessionAttributes().get(RequestConstant.USER))).getId();
        try {
            Optional<User> userOptional = UserService.INSTANCE.findById(userId);
            if (userOptional.isPresent()) {
                Map<String, Object> requestMap = new HashMap<>();
                requestMap.put(RequestConstant.USER, userOptional.get());
                return new ResponseContext(new ForwardResponseType(PageConstant.PROFILE_PAGE), requestMap, new HashMap<>());
            }
        } catch (ServiceException e) {
            LOGGER.error("Failed move to profile", e);
        }
        return new ResponseContext(new ForwardResponseType(PageConstant.ERROR_PAGE));
    }
}
