package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.RestResponseType;
import com.epam.jwd.cafe.command.constant.PageConstant;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.command.marker.UserCommand;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.handler.impl.NumberHandler;
import com.epam.jwd.cafe.model.Product;
import com.epam.jwd.cafe.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The class provides for adding {@link Product} to {@link com.epam.jwd.cafe.model.User} cart
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class AddProductToCartCommand implements Command, UserCommand {
    private static final Logger LOGGER = LogManager.getLogger(AddProductToCartCommand.class);
    private static final ProductService PRODUCT_SERVICE = ProductService.INSTANCE;

    @Override
    public ResponseContext execute(RequestContext request) {
        String id = request.getRequestParameters().get(RequestConstant.ID);
        Set<String> errorMessage = new NumberHandler(request.getRequestParameters().get(RequestConstant.ID)).handleRequest(request);

        if (errorMessage.isEmpty()) {
            int productId = Integer.parseInt(id);
            try {
                if (PRODUCT_SERVICE.findProductById(productId).isPresent()) {
                    Map<String, Object> sessionResponse = new HashMap<>();
                    Map<Integer, Integer> cart = (Map<Integer, Integer>) request.getSessionAttributes().get(RequestConstant.CART);
                    if (cart == null) {
                        cart = new HashMap<>();
                        sessionResponse.put(RequestConstant.CART, cart);
                    }
                    if (cart.containsKey(productId)) {
                        cart.put(productId, cart.get(productId) + 1);
                    } else {
                        cart.put(productId, 1);
                    }
                    return new ResponseContext(new RestResponseType(), new HashMap<>(), sessionResponse);
                }

            } catch (ServiceException e) {
                LOGGER.error("Failed to add product to cart", e);
            }
        }
        return new ResponseContext(new ForwardResponseType(PageConstant.ERROR_PAGE), new HashMap<>(), new HashMap<>());
    }
}
