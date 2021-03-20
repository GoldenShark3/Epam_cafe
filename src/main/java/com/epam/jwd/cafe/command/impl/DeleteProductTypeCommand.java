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
import com.epam.jwd.cafe.model.ProductType;
import com.epam.jwd.cafe.service.ProductTypeService;
import com.epam.jwd.cafe.util.IOUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DeleteProductTypeCommand implements Command {
    private static final ProductTypeService PRODUCT_TYPE_SERVICE = ProductTypeService.INSTANCE;

    @Override
    public ResponseContext execute(RequestContext request) {
        int typeId = Integer.parseInt(request.getRequestParameters().get(RequestConstant.ID));
        try {
            Optional<ProductType> productTypeOptional = PRODUCT_TYPE_SERVICE.findProductTypeById(typeId);

            if (productTypeOptional.isPresent()) {
                ProductType productType = productTypeOptional.get();
                PRODUCT_TYPE_SERVICE.deleteProductTypeById(productType.getId());
                IOUtil.deleteData(productType.getFileName());

                Map<String, Object> requestMap = new HashMap<>();
                requestMap.put(RequestConstant.REDIRECT_COMMAND, CommandManager.TO_MENU.getCommandName());
                return new ResponseContext(new RestResponseType(), requestMap, new HashMap<>());
            }

        } catch (ServiceException | NumberFormatException e) {
            //todo:log
        }
        return new ResponseContext(new ForwardResponseType(PageConstant.ERROR_PAGE), new HashMap<>(), new HashMap<>());
    }

}
