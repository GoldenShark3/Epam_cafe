package com.epam.jwd.cafe.command.constant;

/**
 * The class represent all {@link com.epam.jwd.cafe.command.RequestContext}<br>
 * and {@link com.epam.jwd.cafe.command.ResponseContext}
 * parameters names
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class RequestConstant {
    public final static String FIRST_NAME = "firstName";
    public final static String LAST_NAME = "lastName";
    public final static String USERNAME = "username";
    public final static String EMAIL = "email";
    public final static String PASSWORD = "password";
    public final static String REPEAT_PASSWORD = "password_repeat";
    public final static String PHONE_NUMBER = "number";
    public final static String REDIRECT_COMMAND = "redirect_command";
    public final static String SERVER_MESSAGE = "server_message";
    public final static String COMMAND = "command";
    public final static String LOCALE = "locale";
    public final static String CURRENT_PAGE = "page";
    public final static String ERROR_MESSAGE = "error_message";
    public final static String USER = "user";
    public final static String PAGINATION_CONTEXT = "pagination_context";
    public final static String ID = "id";
    public final static String LOYALTY_POINTS = "points";
    public final static String IS_BLOCKED = "check";
    public final static String CART = "cart";
    public final static String LOGOUT = "logout";
    public final static String PRODUCT_NAME = "product_name";
    public final static String IMG_FILE = "img_file";
    public final static String SELECT = "select";
    public final static String PRODUCT_TYPES = "product_types";
    public final static String TYPE_ID = "type_id";
    public final static String PRODUCT_TYPE = "product_type";
    public final static String PRODUCT_PRICE = "product_price";
    public final static String PRODUCT_DESCRIPTION = "product_description";
    public final static String TOTAL_COST = "total_cost";
    public final static String REVIEW = "review";
    public final static String RATE = "rating";
    public final static String DELIVERY_DATE = "delivery_date";
    public final static String DELIVERY_ADDRESS = "delivery_address";
    public final static String PAYMENT_METHOD = "payment_method";
    public final static String DELIVERY_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm";

    private RequestConstant() {
    }
}
