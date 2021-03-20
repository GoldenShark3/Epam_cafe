package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.constant.PageConstant;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.model.Product;
import com.epam.jwd.cafe.service.ProductService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ToCreateOrderCommand implements Command {
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
                //todo: log
            }
        }
        return new ResponseContext(new ForwardResponseType(PageConstant.ERROR_PAGE), requestMap, new HashMap<>());
    }
}
