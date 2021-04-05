package com.epam.jwd.cafe.model;

/**
 * The class representation of {@link User} role
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public enum Role {
    ADMIN("ADMIN"),
    USER("USER");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
