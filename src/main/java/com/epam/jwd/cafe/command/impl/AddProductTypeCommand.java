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
import com.epam.jwd.cafe.handler.impl.ImgFileHandler;
import com.epam.jwd.cafe.handler.impl.ProductNameHandler;
import com.epam.jwd.cafe.model.Product;
import com.epam.jwd.cafe.model.ProductType;
import com.epam.jwd.cafe.service.ProductTypeService;
import com.epam.jwd.cafe.util.LocalizationMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Part;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * The class provides for adding new {@link ProductType} by admin
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class AddProductTypeCommand implements Command, AdminCommand {
    private static final Logger LOGGER = LogManager.getLogger(AddProductTypeCommand.class);
    private static final ProductTypeService PRODUCT_TYPE_SERVICE = ProductTypeService.INSTANCE;
    private static final Handler PRODUCT_TYPE_HANDLER = new ImgFileHandler(new ProductNameHandler());

    @Override
    public ResponseContext execute(RequestContext request) {
        Set<String> errorMessages = PRODUCT_TYPE_HANDLER.handleRequest(request);
        Map<String, Object> requestMap = new HashMap<>();
        ResponseContext responseContext;

        if (errorMessages.isEmpty()) {
            Part imgFile = request.getRequestParts().get(RequestConstant.IMG_FILE);
            String imgFileName = UUID.randomUUID() + "-" + imgFile.getSubmittedFileName();
            String productTypeName = request.getRequestParameters().get(RequestConstant.PRODUCT_NAME);

            ProductType productType = ProductType.builder()
                    .withName(productTypeName)
                    .withFileName(imgFileName)
                    .build();

            try {
                Optional<String> serverMessage = PRODUCT_TYPE_SERVICE.createProductType(productType);
                if (!serverMessage.isPresent()) {
                    imgFile.write(imgFileName);
                    requestMap.put(RequestConstant.REDIRECT_COMMAND, CommandManager.TO_MENU.getCommandName());
                } else {
                    requestMap.put(RequestConstant.SERVER_MESSAGE, LocalizationMessage.localize(request.getLocale(), serverMessage.get()));
                }
                responseContext = new ResponseContext(new RestResponseType(), requestMap, new HashMap<>());
            } catch (ServiceException | IOException e) {
                LOGGER.error("Failed to add product type", e);
                responseContext = new ResponseContext(new ForwardResponseType(PageConstant.ERROR_PAGE));
            }
        } else {
            requestMap.put(RequestConstant.ERROR_MESSAGE, errorMessages);
            responseContext = new ResponseContext(new RestResponseType(), requestMap, new HashMap<>());
        }
        return responseContext;
    }
}
