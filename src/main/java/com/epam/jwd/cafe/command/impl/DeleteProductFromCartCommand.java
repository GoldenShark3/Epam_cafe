package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.CommandManager;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RedirectResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.RestResponseType;
import com.epam.jwd.cafe.command.constant.PageConstant;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.command.marker.UserCommand;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.handler.impl.NumberHandler;
import com.epam.jwd.cafe.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The class provides delete product from {@link com.epam.jwd.cafe.model.User} cart
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class DeleteProductFromCartCommand implements Command, UserCommand {
    private static final Logger LOGGER = LogManager.getLogger(DeleteProductFromCartCommand.class);
    private static final ProductService PRODUCT_SERVICE = ProductService.INSTANCE;

    @Override
    public ResponseContext execute(RequestContext request) {
        Map<String, Object> requestMap = new HashMap<>();
        String id = request.getRequestParameters().get(RequestConstant.ID);
        Set<String> errorMessage = new NumberHandler(id).handleRequest(request);

        if (errorMessage.isEmpty()) {
            try {
                int productId = Integer.parseInt(id);
                Map<Integer, Integer> cart = (Map<Integer, Integer>) request.getSessionAttributes().get(RequestConstant.CART);

                if (cart != null && PRODUCT_SERVICE.findProductById(productId).isPresent()) {
                    if (cart.containsKey(productId)) {
                        cart.put(productId, cart.get(productId) - 1);
                        if (cart.get(productId) < 1) {
                            cart.remove(productId);
                            requestMap.put(RequestConstant.REDIRECT_COMMAND, CommandManager.TO_CART.getCommandName());
                        }
                    }
                    return new ResponseContext(new RestResponseType(), requestMap, new HashMap<>());
                }
            } catch (ServiceException | NumberFormatException e) {
                LOGGER.error("Failed to delete product from cart or incorrect product id", e);
            }
        }
        return new ResponseContext(new ForwardResponseType(PageConstant.ERROR_PAGE), requestMap, new HashMap<>());
    }
}
