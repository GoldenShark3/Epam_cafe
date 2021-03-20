package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.constant.PageConstant;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.service.ProductTypeService;
import java.util.HashMap;
import java.util.Map;

public class ToMenuCommand implements Command {

    @Override
    public ResponseContext execute(RequestContext request) {
        Map<String, Object> requestMap = new HashMap<>();
        ResponseContext responseContext;
        try {
            requestMap.put(RequestConstant.PRODUCT_TYPES, ProductTypeService.INSTANCE.findAllProductTypes());
            responseContext = new ResponseContext(new ForwardResponseType(PageConstant.MENU_PAGE), requestMap, new HashMap<>());
        } catch (ServiceException e) {
            //todo: log
            responseContext = new ResponseContext(new ForwardResponseType(PageConstant.ERROR_PAGE));
        }
        return responseContext;
    }
}
