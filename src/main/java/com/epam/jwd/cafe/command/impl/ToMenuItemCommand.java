package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.constant.PageConstant;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.model.Product;
import com.epam.jwd.cafe.model.ProductType;
import com.epam.jwd.cafe.service.ProductService;
import com.epam.jwd.cafe.service.ProductTypeService;
import com.epam.jwd.cafe.util.PaginationContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The class provides moving to menu item page
 *
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class ToMenuItemCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(ToMenuItemCommand.class);
    private static final ProductService PRODUCT_SERVICE = ProductService.INSTANCE;
    private static final ProductTypeService PRODUCT_TYPE_SERVICE = ProductTypeService.INSTANCE;

    @Override
    public ResponseContext execute(RequestContext request) {
        ResponseContext responseContext;
        Map<String, Object> requestMap = new HashMap<>();
        try {
            int page = Integer.parseInt(request.getRequestParameters().get(RequestConstant.CURRENT_PAGE));
            int type_id = Integer.parseInt(request.getRequestParameters().get(RequestConstant.TYPE_ID));
            Optional<ProductType> productTypeOptional = PRODUCT_TYPE_SERVICE.findProductTypeById(type_id);

            List<Product> products = PRODUCT_SERVICE.findProductsByTypeId(type_id);
            if ((products.size() > 0) && (productTypeOptional.isPresent())) {
                PaginationContext<Product> paginationContext = new PaginationContext<>(products, page);

                requestMap.put(RequestConstant.PAGINATION_CONTEXT, paginationContext);
                requestMap.put(RequestConstant.PRODUCT_TYPE, productTypeOptional.get());

                responseContext = new ResponseContext(new ForwardResponseType(PageConstant.PRODUCTS_PAGE), requestMap, new HashMap<>());
            } else {
                responseContext = new ResponseContext(new ForwardResponseType(PageConstant.PRODUCTS_PAGE), new HashMap<>(), new HashMap<>());
            }
        } catch (ServiceException | NumberFormatException e) {
            LOGGER.error("Moving to menu item has failed", e);
            responseContext = new ResponseContext(new ForwardResponseType(PageConstant.ERROR_PAGE), new HashMap<>(), new HashMap<>());
        }
        return responseContext;
    }

}
