package com.epam.jwd.cafe.handler.impl;

import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.annotations.AfterTest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProductNameHandlerTest {
    private ProductNameHandler productNameHandler;
    private Map<String, String> requestMap;

    @BeforeEach
    void setUp() {
        productNameHandler = new ProductNameHandler();
        requestMap = new HashMap<>();
    }

    @Test
    public void handleRequest_ShouldReturnEmptySet_WhenNameMatchPattern(){
        requestMap.put(RequestConstant.PRODUCT_NAME, "steak medium-rare");
        RequestContext requestContext = new RequestContext(requestMap, "ru_RU");

        assertEquals(Collections.EMPTY_SET, productNameHandler.handleRequest(requestContext));
    }

    @Test
    public void handleRequest_ShouldReturnServerMessage_WhenNameIsNull() {
        requestMap.put(RequestConstant.PRODUCT_NAME, "he");
        RequestContext requestContext = new RequestContext(requestMap, "ru_RU");

        assertNotEquals(Collections.emptySet(), productNameHandler.handleRequest(requestContext));
    }

    @AfterTest
    void clearRequestMap() {
        requestMap.clear();
    }

    @AfterEach
    void tearDown(){
        productNameHandler = null;
        requestMap = null;
    }
}