package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.constant.PageConstant;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.exception.DaoException;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.model.Order;
import com.epam.jwd.cafe.model.User;
import com.epam.jwd.cafe.service.OrderService;
import com.epam.jwd.cafe.util.PaginationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToOrdersCommand implements Command {
    private static final OrderService ORDER_SERVICE = OrderService.INSTANCE;

    @Override
    public ResponseContext execute(RequestContext request) {
        ResponseContext responseContext;
        Map<String, Object> requestMap = new HashMap<>();
        int page = Integer.parseInt(request.getRequestParameters().get(RequestConstant.CURRENT_PAGE));
        try {
            List<Order> orders = ORDER_SERVICE.findAllOrders();
            if (orders.size() > 0) {
                PaginationContext<Order> paginationContext = new PaginationContext<>(orders, page);
                requestMap.put(RequestConstant.PAGINATION_CONTEXT, paginationContext);
                responseContext = new ResponseContext(
                        new ForwardResponseType(PageConstant.ORDERS_PAGE), requestMap, new HashMap<>());
            } else {
                responseContext = new ResponseContext(
                        new ForwardResponseType(PageConstant.ORDERS_PAGE), new HashMap<>(), new HashMap<>());
            }
        } catch (ServiceException e) {
            //todo: log
            responseContext = new ResponseContext(
                    new ForwardResponseType(PageConstant.ERROR_PAGE), new HashMap<>(), new HashMap<>());
        }
        return responseContext;
    }
}
