package com.epam.jwd.cafe.command;

public abstract class ResponseType {
    public enum Type{
        FORWARD,
        REDIRECT
    }

    private final Type type;

    public ResponseType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
