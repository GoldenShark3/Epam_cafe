package com.epam.jwd.cafe.handler.impl;

import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.annotations.AfterTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


class DescriptionHandlerTest {
    private DescriptionHandler descriptionHandler;
    private Map<String, String> requestMap;

    @BeforeEach
    void setUp() {
        descriptionHandler = new DescriptionHandler();
        requestMap = new HashMap<>();
    }

    @Test
    public void handleRequest_ShouldReturnEmptySet_WhenDescriptionMatchPattern(){
        requestMap.put(RequestConstant.PRODUCT_DESCRIPTION, "valid description");
        RequestContext requestContext = new RequestContext(requestMap, "ru_RU");

        assertEquals(Collections.EMPTY_SET, descriptionHandler.handleRequest(requestContext));
    }

    @Test
    public void handleRequest_ShouldReturnServerMessage_WhenDescriptionDoesntMatchPattern() {
        requestMap.put(RequestConstant.PRODUCT_DESCRIPTION, "he");
        RequestContext requestContext = new RequestContext(requestMap, "ru_RU");

        assertNotEquals(Collections.emptySet(), descriptionHandler.handleRequest(requestContext));
    }

    @AfterTest
    void clearRequestMap() {
        requestMap.clear();
    }

    @AfterEach
    void tearDown(){
        descriptionHandler = null;
        requestMap = null;
    }
}