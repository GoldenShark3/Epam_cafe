package com.epam.jwd.cafe.dao.field;

import com.epam.jwd.cafe.model.Order;
import com.epam.jwd.cafe.model.Product;

/**
 * The class representation of {@link Product} fields
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public enum ProductField implements EntityField<Product> {
    ID,
    NAME,
    PRICE,
    IMG_NAME,
    PRODUCT_DESCRIPTION,
    PRODUCT_TYPE_ID
}
