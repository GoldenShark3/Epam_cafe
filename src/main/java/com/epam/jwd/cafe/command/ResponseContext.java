package com.epam.jwd.cafe.command;

import java.util.Map;

public class ResponseContext {
    private ResponseType responseType;
    private final Map<String, Object> requestAttributes;
    private final Map<String, Object> sessionAttributes;

    public ResponseContext(ResponseType responseType, Map<String, Object> requestAttributes, Map<String, Object> sessionAttributes) {
        this.responseType = responseType;
        this.requestAttributes = requestAttributes;
        this.sessionAttributes = sessionAttributes;
    }

    public ResponseContext(Map<String, Object> requestAttributes, Map<String, Object> sessionAttributes) {
        this.requestAttributes = requestAttributes;
        this.sessionAttributes = sessionAttributes;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public Map<String, Object> getRequestAttributes() {
        return requestAttributes;
    }

    public Map<String, Object> getSessionAttributes() {
        return sessionAttributes;
    }
}
