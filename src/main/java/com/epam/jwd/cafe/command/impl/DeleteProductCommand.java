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
import com.epam.jwd.cafe.model.Product;
import com.epam.jwd.cafe.service.ProductService;
import com.epam.jwd.cafe.util.IOUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DeleteProductCommand implements Command {
    private static final ProductService PRODUCT_SERVICE = ProductService.INSTANCE;

    @Override
    public ResponseContext execute(RequestContext request) {
        int productId = Integer.parseInt(request.getRequestParameters().get(RequestConstant.ID));
        try {
            Optional<Product> productOptional = PRODUCT_SERVICE.findProductById(productId);

            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                PRODUCT_SERVICE.deleteProductById(product.getId());
                IOUtil.deleteData(product.getImgFileName());

                Map<String, Object> requestMap = new HashMap<>();
                requestMap.put(RequestConstant.REDIRECT_COMMAND, CommandManager.TO_MENU_ITEM.getCommandName());
                return new ResponseContext(new RestResponseType(), requestMap, new HashMap<>());
            }

        } catch (ServiceException | NumberFormatException e) {
            //todo:log
        }
        return new ResponseContext(new ForwardResponseType(PageConstant.ERROR_PAGE), new HashMap<>(), new HashMap<>());
    }
}
