package com.epam.jwd.cafe.command;

/**
 * The class is represented in {@link RequestContext} in order to {@link com.epam.jwd.cafe.controller.Controller} do forward
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class ForwardResponseType extends ResponseType {
    private final String page;

    public ForwardResponseType(String page) {
        super(Type.FORWARD);
        this.page = page;
    }

    public String getPage() {
        return page;
    }
}
