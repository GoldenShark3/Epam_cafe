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
import com.epam.jwd.cafe.handler.impl.ImgFileHandler;
import com.epam.jwd.cafe.handler.impl.PriceHandler;
import com.epam.jwd.cafe.handler.impl.ProductNameHandler;
import com.epam.jwd.cafe.model.Product;
import com.epam.jwd.cafe.model.ProductType;
import com.epam.jwd.cafe.model.User;
import com.epam.jwd.cafe.service.ProductService;
import com.epam.jwd.cafe.service.ProductTypeService;
import com.epam.jwd.cafe.util.LocalizationMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Part;
import java.io.FilePermission;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * The class provides for adding new {@link Product} by admin
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class AddProductCommand implements Command, AdminCommand {
    private static final Logger LOGGER = LogManager.getLogger(AddProductCommand.class);
    private static final ProductService PRODUCT_SERVICE = ProductService.INSTANCE;
    private static final ProductTypeService PRODUCT_TYPE_SERVICE = ProductTypeService.INSTANCE;
    private static final Handler PRODUCT_HANDLER = new ProductNameHandler(new PriceHandler(
            new DescriptionHandler(new ImgFileHandler())));

    @Override
    public ResponseContext execute(RequestContext request) {
        Set<String> errorMessages = PRODUCT_HANDLER.handleRequest(request);
        Map<String, Object> requestMap = new HashMap<>();

        if (errorMessages.isEmpty()) {
            try {
                Part imgFile = request.getRequestParts().get(RequestConstant.IMG_FILE);
                String imgFileName = UUID.randomUUID() + "-" + imgFile.getSubmittedFileName();
                int productTypeId = Integer.parseInt(request.getRequestParameters().get(RequestConstant.TYPE_ID));
                String productName = request.getRequestParameters().get(RequestConstant.PRODUCT_NAME);
                String productDescription = request.getRequestParameters().get(RequestConstant.PRODUCT_DESCRIPTION);
                BigDecimal productPrice = new BigDecimal(request.getRequestParameters().get(RequestConstant.PRODUCT_PRICE));
                Optional<ProductType> productTypeOptional = PRODUCT_TYPE_SERVICE.findProductTypeById(productTypeId);

                if (productTypeOptional.isPresent()) {
                    Product product = Product.builder()
                            .withId(productTypeId)
                            .withName(productName)
                            .withProductType(productTypeOptional.get())
                            .withDescription(productDescription)
                            .withPrice(productPrice)
                            .withImgFileName(imgFileName)
                            .build();

                    Optional<String> serverMessage = PRODUCT_SERVICE.createProduct(product);

                    if (!serverMessage.isPresent()) {
                        imgFile.write(imgFileName);
                        requestMap.put(RequestConstant.REDIRECT_COMMAND, CommandManager.TO_MENU_ITEM.getCommandName());
                    } else {
                        requestMap.put(RequestConstant.SERVER_MESSAGE,
                                LocalizationMessage.localize(request.getLocale(), serverMessage.get()));
                    }
                    return new ResponseContext(new RestResponseType(), requestMap, new HashMap<>());
                }
            } catch (ServiceException | IOException e) {
                LOGGER.error("Filed to add product", e);
            }
        } else {
            requestMap.put(RequestConstant.ERROR_MESSAGE, errorMessages);
            return new ResponseContext(new RestResponseType(), requestMap, new HashMap<>());
        }
        return new ResponseContext(new ForwardResponseType(PageConstant.ERROR_PAGE), new HashMap<>(), new HashMap<>());
    }

}
