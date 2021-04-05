package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.constant.PageConstant;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.model.ProductType;
import com.epam.jwd.cafe.service.ProductTypeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class provides moving to menu page
 *
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class ToMenuCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(ToMenuCommand.class);

    @Override
    public ResponseContext execute(RequestContext request) {
        Map<String, Object> requestMap = new HashMap<>();
        ResponseContext responseContext;
        try {
            List<ProductType> productTypes = ProductTypeService.INSTANCE.findAllProductTypes();
            requestMap.put(RequestConstant.PRODUCT_TYPES, productTypes);
            responseContext = new ResponseContext(new ForwardResponseType(PageConstant.MENU_PAGE), requestMap, new HashMap<>());
        } catch (ServiceException e) {
            LOGGER.error("Failed to find all product types", e);
            responseContext = new ResponseContext(new ForwardResponseType(PageConstant.ERROR_PAGE));
        }
        return responseContext;
    }
}
