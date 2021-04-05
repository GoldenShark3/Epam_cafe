package com.epam.jwd.cafe.command;

/**
 * Class represent types of {@link ResponseContext}
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public abstract class ResponseType {
    public enum Type{
        FORWARD,
        REDIRECT,
        REST
    }

    private final Type type;

    public ResponseType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
