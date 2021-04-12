package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.constant.PageConstant;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.command.marker.AdminCommand;
import com.epam.jwd.cafe.exception.DaoException;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.model.User;
import com.epam.jwd.cafe.service.UserService;
import com.epam.jwd.cafe.util.PaginationContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class provides moving an admin to page with all users data
 *
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class ToUsersCommand implements Command, AdminCommand {
    private static final Logger LOGGER = LogManager.getLogger(ToUsersCommand.class);
    private static final UserService USER_SERVICE = UserService.INSTANCE;

    @Override
    public ResponseContext execute(RequestContext request) {
        ResponseContext responseContext;
        Map<String, Object> requestMap = new HashMap<>();
        int page = Integer.parseInt(request.getRequestParameters().get(RequestConstant.CURRENT_PAGE));
        try {
            List<User> userList = USER_SERVICE.findAllUsers();
            if (userList.size() > 0) {
                PaginationContext<User> paginationContext = new PaginationContext<>(userList, page);
                requestMap.put(RequestConstant.PAGINATION_CONTEXT, paginationContext);
                responseContext = new ResponseContext(
                        new ForwardResponseType(PageConstant.USERS_PAGE), requestMap, new HashMap<>());
            } else {
                responseContext = new ResponseContext(
                        new ForwardResponseType(PageConstant.USERS_PAGE), new HashMap<>(), new HashMap<>());
            }
        } catch (ServiceException e) {
            LOGGER.error("Failed move to users", e);
            responseContext = new ResponseContext(
                    new ForwardResponseType(PageConstant.ERROR_PAGE), new HashMap<>(), new HashMap<>());
        }
        return responseContext;
    }
}
