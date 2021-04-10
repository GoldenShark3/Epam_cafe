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

class PhoneNumberHandlerTest {
    private PhoneNumberHandler phoneNumberHandler;
    private Map<String, String> requestMap;

    @BeforeEach
    void setUp() {
        phoneNumberHandler = new PhoneNumberHandler();
        requestMap = new HashMap<>();
    }

    @Test
    public void handleRequest_ShouldReturnEmptySet_WhenPhoneNumberMatchPattern(){
        requestMap.put(RequestConstant.PHONE_NUMBER, "+375333132549");
        RequestContext requestContext = new RequestContext(requestMap, "ru_RU");

        assertEquals(Collections.EMPTY_SET, phoneNumberHandler.handleRequest(requestContext));
    }

    @Test
    public void handleRequest_ShouldReturnServerMessage_WhenPhoneNumberDoesntMatchPattern() {
        requestMap.put(RequestConstant.PHONE_NUMBER, "+375259999999456");
        RequestContext requestContext = new RequestContext(requestMap, "ru_RU");

        assertNotEquals(Collections.emptySet(), phoneNumberHandler.handleRequest(requestContext));
    }

    @AfterTest
    void clearRequestMap() {
        requestMap.clear();
    }

    @AfterEach
    void tearDown(){
        phoneNumberHandler = null;
        requestMap = null;
    }
}