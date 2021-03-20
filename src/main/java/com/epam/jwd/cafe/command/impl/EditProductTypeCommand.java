package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.CommandManager;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.RestResponseType;
import com.epam.jwd.cafe.command.constant.PageConstant;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.handler.Handler;
import com.epam.jwd.cafe.handler.impl.ImgFileHandler;
import com.epam.jwd.cafe.handler.impl.ProductNameHandler;
import com.epam.jwd.cafe.model.ProductType;
import com.epam.jwd.cafe.service.ProductTypeService;
import com.epam.jwd.cafe.util.LocalizationMessage;

import javax.servlet.http.Part;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class EditProductTypeCommand implements Command {
    private static final ProductTypeService PRODUCT_TYPE_SERVICE = ProductTypeService.INSTANCE;
    private static final Handler EDIT_HANDLER = new ProductNameHandler(new ImgFileHandler());

    @Override
    public ResponseContext execute(RequestContext request) {
        Set<String> errorMessages = EDIT_HANDLER.handleRequest(request);
        Map<String, Object> requestMap = new HashMap<>();

        if (errorMessages.isEmpty()) {
            Part editedImg = request.getRequestParts().get(RequestConstant.IMG_FILE);
            String newFileName = UUID.randomUUID() + editedImg.getSubmittedFileName();
            String newProductName = request.getRequestParameters().get(RequestConstant.PRODUCT_NAME);
            int type_id = Integer.parseInt(request.getRequestParameters().get(RequestConstant.ID));

            try {
                Optional<String> serverMessage = PRODUCT_TYPE_SERVICE.editProductType(type_id, newFileName, newProductName);

                if (!serverMessage.isPresent()) {
                    editedImg.write(newFileName);
                    requestMap.put(RequestConstant.REDIRECT_COMMAND, CommandManager.TO_MENU.getCommandName());
                } else {
                    requestMap.put(RequestConstant.SERVER_MESSAGE,
                            LocalizationMessage.localize(request.getLocale(), serverMessage.get()));
                }
                return new ResponseContext(new RestResponseType(), requestMap, new HashMap<>());

            } catch (ServiceException | IOException e) {
                //todo: log
                return new ResponseContext(new ForwardResponseType(PageConstant.ERROR_PAGE), new HashMap<>(), new HashMap<>());
            }
        } else {
            requestMap.put(RequestConstant.ERROR_MESSAGE, errorMessages);
            return new ResponseContext(new RestResponseType(), requestMap, new HashMap<>());
        }
    }
}
