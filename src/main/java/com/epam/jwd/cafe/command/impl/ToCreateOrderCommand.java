package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.constant.PageConstant;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.command.marker.UserCommand;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.model.Product;
import com.epam.jwd.cafe.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * The class provides moving {@link com.epam.jwd.cafe.model.User} to page for create order
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class ToCreateOrderCommand implements Command, UserCommand {
    private static final Logger LOGGER = LogManager.getLogger(ToCreateOrderCommand.class);
    private static final ProductService PRODUCT_SERVICE = ProductService.INSTANCE;

    @Override
    public ResponseContext execute(RequestContext request) {
        Map<String, Object> requestMap = new HashMap<>();
        Map<Integer, Integer> cart = (Map<Integer, Integer>) request.getSessionAttributes().get(RequestConstant.CART);

        if (cart != null) {
            try {
                Map<Product, Integer> productsInCart = PRODUCT_SERVICE.receiveProducts(cart);
                if (!productsInCart.isEmpty()) {
                    BigDecimal totalCost = PRODUCT_SERVICE.calcOrderCost(productsInCart);
                    requestMap.put(RequestConstant.TOTAL_COST, totalCost);

                    return new ResponseContext(new ForwardResponseType(PageConstant.CREATE_ORDER), requestMap, new HashMap<>());
                }
            } catch (ServiceException e) {
                LOGGER.error("Failed to receive product from cart", e);
            }
        }
        return new ResponseContext(new ForwardResponseType(PageConstant.ERROR_PAGE), requestMap, new HashMap<>());
    }
}
