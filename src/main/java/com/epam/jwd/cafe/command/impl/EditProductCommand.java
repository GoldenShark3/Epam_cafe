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
import com.epam.jwd.cafe.handler.Handler;
import com.epam.jwd.cafe.handler.impl.DescriptionHandler;
import com.epam.jwd.cafe.handler.impl.NumberHandler;
import com.epam.jwd.cafe.handler.impl.PriceHandler;
import com.epam.jwd.cafe.handler.impl.ProductNameHandler;
import com.epam.jwd.cafe.model.Product;
import com.epam.jwd.cafe.service.ProductService;
import com.epam.jwd.cafe.util.LocalizationMessage;
import com.sun.javafx.fxml.builder.JavaFXFontBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class EditProductCommand implements Command, AdminCommand {
    private static final Logger LOGGER = LogManager.getLogger(EditProductCommand.class);
    private static final ProductService PRODUCT_SERVICE = ProductService.INSTANCE;
    private static final Handler EDIT_PRODUCT_HANDLER = new PriceHandler(new ProductNameHandler(new DescriptionHandler()));

    @Override
    public ResponseContext execute(RequestContext request) {
        ResponseContext responseContext;
        Map<String, Object> requestMap = new HashMap<>();

        String id = request.getRequestParameters().get(RequestConstant.ID);
        Set<String> errorMessage = new NumberHandler(EDIT_PRODUCT_HANDLER, id).handleRequest(request);

        if (errorMessage.isEmpty()) {
            int productId = Integer.parseInt(id);
            String price = request.getRequestParameters().get(RequestConstant.PRODUCT_PRICE);
            String productName = request.getRequestParameters().get(RequestConstant.PRODUCT_NAME);
            BigDecimal productPrice = new BigDecimal(price);
            String productDescription = request.getRequestParameters().get(RequestConstant.PRODUCT_DESCRIPTION);

            try {
                Optional<String> serverMessage = PRODUCT_SERVICE.editProduct(productId, productName, productPrice, productDescription);

                if (!serverMessage.isPresent()) {
                    requestMap.put(RequestConstant.REDIRECT_COMMAND, CommandManager.TO_MENU_ITEM.getCommandName());
                } else {
                    requestMap.put(RequestConstant.SERVER_MESSAGE, LocalizationMessage.localize(request.getLocale(), serverMessage.get()));
                }
                responseContext = new ResponseContext(new RestResponseType(), requestMap, new HashMap<>());

            } catch (ServiceException e) {
                LOGGER.error("Failed to edit product", e);
                responseContext = new ResponseContext(new ForwardResponseType(PageConstant.ERROR_PAGE), new HashMap<>(), new HashMap<>());
            }
        } else {
            requestMap.put(RequestConstant.ERROR_MESSAGE, errorMessage);
            responseContext = new ResponseContext(new RestResponseType(), requestMap, new HashMap<>());
        }
        return responseContext;
    }
}
