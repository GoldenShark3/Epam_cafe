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

class UsernameHandlerTest {
    private UsernameHandler usernameHandler;
    private Map<String, String> requestMap;

    @BeforeEach
    void setUp() {
        usernameHandler = new UsernameHandler();
        requestMap = new HashMap<>();
    }

    @Test
    public void handleRequest_ShouldReturnEmptySet_WhenUsernameMatchPattern(){
        requestMap.put(RequestConstant.USERNAME, "userName");
        RequestContext requestContext = new RequestContext(requestMap, "ru_RU");

        assertEquals(Collections.EMPTY_SET, usernameHandler.handleRequest(requestContext));
    }

    @Test
    public void handleRequest_ShouldReturnServerMessage_WhenUsernameDoesntMatchPattern() {
        requestMap.put(RequestConstant.PRODUCT_DESCRIPTION, "invalid username long 123");
        RequestContext requestContext = new RequestContext(requestMap, "ru_RU");

        assertNotEquals(Collections.emptySet(), usernameHandler.handleRequest(requestContext));
    }

    @AfterTest
    void clearRequestMap() {
        requestMap.clear();
    }

    @AfterEach
    void tearDown(){
        usernameHandler = null;
        requestMap = null;
    }
}