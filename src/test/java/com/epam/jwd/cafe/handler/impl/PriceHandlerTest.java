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

class PriceHandlerTest {
    private PriceHandler priceHandler;
    private Map<String, String> requestMap;

    @BeforeEach
    void setUp() {
        priceHandler = new PriceHandler();
        requestMap = new HashMap<>();
    }

    @Test
    public void handleRequest_ShouldReturnEmptySet_WhenPriceMatchPattern(){
        requestMap.put(RequestConstant.PRODUCT_PRICE, "15.99");
        RequestContext requestContext = new RequestContext(requestMap, "ru_RU");

        assertEquals(Collections.EMPTY_SET, priceHandler.handleRequest(requestContext));
    }

    @Test
    public void handleRequest_ShouldReturnServerMessage_WhenPriceDoesntMatchPattern() {
        requestMap.put(RequestConstant.PRODUCT_PRICE, "195.590");
        RequestContext requestContext = new RequestContext(requestMap, "ru_RU");

        assertNotEquals(Collections.emptySet(), priceHandler.handleRequest(requestContext));
    }

    @AfterTest
    void clearRequestMap() {
        requestMap.clear();
    }

    @AfterEach
    void tearDown(){
        priceHandler = null;
        requestMap = null;
    }
}