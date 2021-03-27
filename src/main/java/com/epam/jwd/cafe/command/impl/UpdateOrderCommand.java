package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.CommandManager;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.RestResponseType;
import com.epam.jwd.cafe.command.constant.PageConstant;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.command.marker.AdminCommand;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.model.Order;
import com.epam.jwd.cafe.model.OrderStatus;
import com.epam.jwd.cafe.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UpdateOrderCommand implements Command, AdminCommand {
    private static final Logger LOGGER = LogManager.getLogger(UpdateOrderCommand.class);
    private static final OrderService ORDER_SERVICE = OrderService.INSTANCE;

    @Override
    public ResponseContext execute(RequestContext request) {
        Map<String, Object> requestMap = new HashMap<>();
        try {
            int orderId = Integer.parseInt(request.getRequestParameters().get(RequestConstant.ID));
            OrderStatus orderStatus = OrderStatus.valueOf(request.getRequestParameters().get(RequestConstant.SELECT));
            Optional<Order> orderOptional = ORDER_SERVICE.findOrderById(orderId);
            if (orderOptional.isPresent()) {
                Order order = Order.builder()
                        .withId(orderOptional.get().getId())
                        .withUser(orderOptional.get().getUser())
                        .withProducts(orderOptional.get().getProducts())
                        .withDeliveryDate(orderOptional.get().getDeliveryDate())
                        .withDeliveryAddress(orderOptional.get().getDeliveryAddress())
                        .withCreateDate(orderOptional.get().getCreateDate())
                        .withPaymentMethod(orderOptional.get().getPaymentMethod())
                        .withOrderStatus(orderStatus)
                        .withCost(orderOptional.get().getCost())
                        .build();
                ORDER_SERVICE.updateOrder(order);
                requestMap.put(RequestConstant.REDIRECT_COMMAND, CommandManager.TO_ORDERS.getCommandName());
                return new ResponseContext(new RestResponseType(), requestMap, new HashMap<>());
            }
        } catch (ServiceException e) {
            LOGGER.error("Failed update order", e);
        }
        return new ResponseContext(new ForwardResponseType(PageConstant.ERROR_PAGE), requestMap, new HashMap<>());
    }
}
