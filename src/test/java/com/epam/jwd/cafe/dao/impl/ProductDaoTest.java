package com.epam.jwd.cafe.dao.impl;

import com.epam.jwd.cafe.dao.field.ProductField;
import com.epam.jwd.cafe.exception.DaoException;
import com.epam.jwd.cafe.model.Product;
import com.epam.jwd.cafe.model.ProductType;
import com.epam.jwd.cafe.pool.ConnectionPool;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductDaoTest {
    private static ProductType productType;

    @BeforeAll
    public static void beforeAll() throws ClassNotFoundException {
        ConnectionPool.getInstance().initConnectionPool();
        productType = ProductType.builder()
                .withId(35)
                .withName("Бургеры")
                .withFileName("9edf7d2a-9217-4ee0-80c5-a622efa29f9b-product_type_burger.png")
                .build();
    }

    @Test
    public void successCRUDOperationsTest() throws DaoException {
        ProductDao productDao = ProductDao.INSTANCE;
        Product.Builder productBuilder = Product.builder()
                .withId(1)
                .withName("product")
                .withImgFileName("imgFile")
                .withDescription("description")
                .withPrice(new BigDecimal("0.00"))
                .withProductType(productType);

        productDao.create(productBuilder.build());
        List<Product> products = productDao.findByField("product", ProductField.NAME);
        productBuilder.withId(products.get(0).getId());
        assertTrue(products.contains(productBuilder.build()));

        productBuilder.withName("updateName");
        productDao.update(productBuilder.build());
        products = productDao.findByField("updateName", ProductField.NAME);
        assertTrue(products.contains(productBuilder.build()));

        productDao.deleteById(products.get(0).getId());
        products = productDao.findByField("updateName", ProductField.NAME);
        assertFalse(products.contains(productBuilder.build()));
    }

    @AfterAll
    public static void afterAll() throws SQLException {
        ConnectionPool.getInstance().destroyConnectionPool(false);
    }

}