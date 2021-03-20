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
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.handler.impl.NumberHandler;
import com.epam.jwd.cafe.service.ProductService;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DeleteProductFromCartCommand implements Command {
    private static final ProductService PRODUCT_SERVICE = ProductService.INSTANCE;

    @Override
    public ResponseContext execute(RequestContext request) {
        Map<String, Object> requestMap = new HashMap<>();
        String id = request.getRequestParameters().get(RequestConstant.ID);
        Set<String> errorMessage = new NumberHandler(id).handleRequest(request);

        if (errorMessage.isEmpty()) {
            int productId = Integer.parseInt(id);
            try {
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
                //todo: log
            }
        }
        return new ResponseContext(new ForwardResponseType(PageConstant.ERROR_PAGE), requestMap, new HashMap<>());
    }
}
