package com.epam.jwd.cafe.service;

import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.model.Product;
import com.epam.jwd.cafe.model.ProductType;
import com.epam.jwd.cafe.pool.ConnectionPool;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductServiceTest {
    private static ProductService productService;
    private static Product.Builder productBuilder;


    @BeforeAll
    static void setUp() throws ClassNotFoundException {
        ConnectionPool.getInstance().initConnectionPool();
        productService = ProductService.INSTANCE;

        ProductType productType = ProductType.builder()
                .withId(35)
                .withName("Бургеры")
                .withFileName("9edf7d2a-9217-4ee0-80c5-a622efa29f9b-product_type_burger.png")
                .build();

        productBuilder = Product.builder()
                .withId(1)
                .withName("something food")
                .withDescription("description")
                .withPrice(BigDecimal.TEN)
                .withImgFileName("hello")
                .withProductType(productType);
    }

    @Test
    public void createProduct_ShouldReturnEmptyOptional() throws ServiceException {
        assertFalse(productService.createProduct(productBuilder.build()).isPresent());
    }

    @Test
    public void createProduct_ShouldReturnNameAlreadyTakenMessage() throws ServiceException {
        assertTrue(productService.createProduct(productBuilder.build()).isPresent());
    }

    @Test
    public void calcOrderCost_ShouldReturn50() {
        Map<Product, Integer> cart = new HashMap<>();
        productBuilder.withPrice(BigDecimal.TEN);
        cart.put(productBuilder.build(), 5);

        assertEquals(productService.calcOrderCost(cart), BigDecimal.valueOf(50));
    }


    @AfterAll
    static void tearDown() throws ServiceException, SQLException {
        Optional<Product> productOptional = productService.findProductByName(productBuilder.build().getName());
        if (productOptional.isPresent()) {
            productService.deleteProductById(productOptional.get().getId());
        }
        ConnectionPool.getInstance().destroyConnectionPool(false);
    }
}