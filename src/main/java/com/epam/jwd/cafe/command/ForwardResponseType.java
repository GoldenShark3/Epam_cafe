package com.epam.jwd.cafe.command;

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
