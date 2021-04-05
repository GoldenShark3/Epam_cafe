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
import com.epam.jwd.cafe.model.Order;
import com.epam.jwd.cafe.model.OrderStatus;
import com.epam.jwd.cafe.model.PaymentMethod;
import com.epam.jwd.cafe.model.Product;
import com.epam.jwd.cafe.model.User;
import com.epam.jwd.cafe.model.dto.UserDto;
import com.epam.jwd.cafe.service.OrderService;
import com.epam.jwd.cafe.service.ProductService;
import com.epam.jwd.cafe.service.UserService;
import com.epam.jwd.cafe.util.LocalizationMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The class provides create new {@link Order}
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class CreateOrderCommand implements Command, UserCommand {
    private static final Logger LOGGER = LogManager.getLogger(CreateOrderCommand.class);
    private static final OrderService ORDER_SERVICE = OrderService.INSTANCE;
    private static final ProductService PRODUCT_SERVICE = ProductService.INSTANCE;
    private static final UserService USER_SERVICE = UserService.INSTANCE;


    @Override
    public ResponseContext execute(RequestContext request) {
        Map<String, Object> requestMap = new HashMap<>();
        Map<String, Object> sessionMap = new HashMap<>();
        String deliveryDateStr = request.getRequestParameters().get(RequestConstant.DELIVERY_DATE);
        String deliveryAddress = request.getRequestParameters().get(RequestConstant.DELIVERY_ADDRESS);
        String paymentMethod = request.getRequestParameters().get(RequestConstant.PAYMENT_METHOD);
        UserDto userDto = (UserDto) request.getSessionAttributes().get(RequestConstant.USER);
        Map<Integer, Integer> cart = (Map<Integer, Integer>) request.getSessionAttributes().get(RequestConstant.CART);

        if (cart == null || cart.isEmpty()) {
            requestMap.put(RequestConstant.CART, new HashMap<>());
            return new ResponseContext(new ForwardResponseType(PageConstant.CART_PAGE), requestMap, new HashMap<>());
        }

        try {
            Map<Product, Integer> products = PRODUCT_SERVICE.receiveProducts(cart);
            Optional<User> userOptional = USER_SERVICE.findById(userDto.getId());

            if (products.size() > 0 && userOptional.isPresent()) {
                LocalDateTime deliveryDate = LocalDateTime.parse(deliveryDateStr,
                        DateTimeFormatter.ofPattern(RequestConstant.DELIVERY_DATE_PATTERN));
                BigDecimal orderCost = PRODUCT_SERVICE.calcOrderCost(products);

                Order order = Order.builder()
                        .withId(userDto.getId())
                        .withCost(orderCost)
                        .withOrderStatus(OrderStatus.ACTIVE)
                        .withCreateDate(LocalDateTime.now())
                        .withPaymentMethod(PaymentMethod.valueOf(paymentMethod))
                        .withDeliveryDate(deliveryDate)
                        .withDeliveryAddress(deliveryAddress)
                        .withProducts(products)
                        .withUser(userOptional.get())
                        .build();

                Optional<String> serverMessage = ORDER_SERVICE.createOrder(order);

                if (!serverMessage.isPresent()) {
                    sessionMap.put(RequestConstant.CART, new HashMap<>());
                    requestMap.put(RequestConstant.REDIRECT_COMMAND, CommandManager.TO_MAIN.getCommandName());
                } else {
                    requestMap.put(RequestConstant.SERVER_MESSAGE,
                            LocalizationMessage.localize(request.getLocale(), serverMessage.get()));
                }
                return new ResponseContext(new RestResponseType(), requestMap, sessionMap);
            }
        } catch (ServiceException e) {
            LOGGER.error("Failed to create order", e);
        }
        return new ResponseContext(new ForwardResponseType(PageConstant.ERROR_PAGE), requestMap, sessionMap);
    }
}
