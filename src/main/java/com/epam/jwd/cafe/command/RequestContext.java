package com.epam.jwd.cafe.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * The class is wrap all data from {@link HttpServletRequest}
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class RequestContext {
    private final Map<String, String> requestParameters;
    private final Map<String, Object> requestAttributes;
    private final Map<String, Object> sessionAttributes;
    private final Map<String, Part> requestParts;
    private final String locale;

    public RequestContext(HttpServletRequest request) throws IOException, ServletException {
        requestParameters = retrieveRequestParameters(request);
        requestAttributes = retrieveRequestAttributes(request);
        sessionAttributes = retrieveSessionAttributes(request);
        requestParts = retrieveRequestParts(request);
        locale = retrieveLocale(request);
    }

    public RequestContext(Map<String, String> requestParameters, String locale) {
        this.requestParameters = requestParameters;
        this.locale = locale;
        requestAttributes = null;
        sessionAttributes = null;
        requestParts = null;
    }

    public Map<String, String> getRequestParameters() {
        return requestParameters;
    }

    public Map<String, Object> getRequestAttributes() {
        return requestAttributes;
    }

    public Map<String, Object> getSessionAttributes() {
        return sessionAttributes;
    }

    public Map<String, Part> getRequestParts() {
        return requestParts;
    }

    public String getLocale() {
        return locale;
    }

    private Map<String, String> retrieveRequestParameters(HttpServletRequest request) {
        Map<String, String> parametersMap = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            parametersMap.put(parameterName, request.getParameter(parameterName));
        }

        return parametersMap;
    }

    private Map<String, Object> retrieveRequestAttributes(HttpServletRequest request) {
        Map<String, Object> attributes = new HashMap<>();
        Enumeration<String> attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            attributes.put(attributeName, request.getAttribute(attributeName));
        }
        return attributes;
    }

    private Map<String, Part> retrieveRequestParts(HttpServletRequest request) throws IOException, ServletException {
        Map<String, Part> parts = new HashMap<>();
        if (request.getContentType() != null && request.getContentType().toLowerCase().contains("multipart/form-data")) {
            for (Part part : request.getParts()) {
                parts.put(part.getName(), part);
            }
        }
        return parts;
    }

    private Map<String, Object> retrieveSessionAttributes(HttpServletRequest request) {
        Map<String, Object> sessionAttributes = new HashMap<>();
        Enumeration<String> attributeNames = request.getSession(true).getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            sessionAttributes.put(attributeName, request.getSession().getAttribute(attributeName));
        }
        return sessionAttributes;
    }

    private String retrieveLocale(HttpServletRequest request) {
        String locale = (String) request.getSession().getAttribute("locale");
        return locale != null ? locale : "ru_RU";
    }
}
