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
import java.util.HashMap;
import java.util.Map;

public class ToCartCommand implements Command {
    private static final ProductService PRODUCT_SERVICE = ProductService.INSTANCE;

    @Override
    public ResponseContext execute(RequestContext request) {
        try {
            Map<Integer, Integer> cart = (Map<Integer, Integer>) request.getSessionAttributes().get(RequestConstant.CART);
            Map<Product, Integer> productsInCart;

            if (cart == null) {
                productsInCart = new HashMap<>();
            } else {
                productsInCart = PRODUCT_SERVICE.receiveProducts(cart);
            }

            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put(RequestConstant.CART, productsInCart.entrySet());
            return new ResponseContext(new ForwardResponseType(PageConstant.CART_PAGE), requestMap, new HashMap<>());
        } catch (ServiceException e) {
            //todo: log & change exception
        }
        return new ResponseContext(new ForwardResponseType(PageConstant.ERROR_PAGE));
    }
}
