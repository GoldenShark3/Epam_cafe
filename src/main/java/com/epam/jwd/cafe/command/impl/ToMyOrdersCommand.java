package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.constant.PageConstant;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.command.marker.UserCommand;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.model.Order;
import com.epam.jwd.cafe.model.dto.UserDto;
import com.epam.jwd.cafe.service.OrderService;
import com.epam.jwd.cafe.util.PaginationContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToMyOrdersCommand implements Command, UserCommand {
    private static final Logger LOGGER = LogManager.getLogger(ToMyOrdersCommand.class);
    private static final OrderService ORDER_SERVICE = OrderService.INSTANCE;

    @Override
    public ResponseContext execute(RequestContext request) {
        ResponseContext responseContext;
        Map<String, Object> requestMap = new HashMap<>();

        UserDto userDto = (UserDto) request.getSessionAttributes().get(RequestConstant.USER);
        int userId = userDto.getId();
        try {
            int page = Integer.parseInt(request.getRequestParameters().get(RequestConstant.CURRENT_PAGE));
            List<Order> orders = ORDER_SERVICE.findAllOrdersByUserId(userId);

            if (orders.size() > 0) {
                PaginationContext<Order> paginationContext = new PaginationContext<>(orders, page);
                requestMap.put(RequestConstant.PAGINATION_CONTEXT, paginationContext);
            }

            responseContext = new ResponseContext(
                    new ForwardResponseType(PageConstant.MY_ORDERS_PAGE), requestMap, new HashMap<>());

        } catch (ServiceException | NumberFormatException e) {
            LOGGER.error("Failed move to 'my orders' page", e);
            responseContext = new ResponseContext(
                    new ForwardResponseType(PageConstant.ERROR_PAGE), requestMap, new HashMap<>());
        }
        return responseContext;
    }
}
