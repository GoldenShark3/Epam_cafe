package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.constant.PageConstant;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.model.User;
import com.epam.jwd.cafe.model.dto.UserDto;
import com.epam.jwd.cafe.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ToProfileCommand implements Command {

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
            //todo: log
        }
        return new ResponseContext(new ForwardResponseType(PageConstant.ERROR_PAGE));
    }
}
