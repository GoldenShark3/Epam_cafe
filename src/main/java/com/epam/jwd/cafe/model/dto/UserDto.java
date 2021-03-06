package com.epam.jwd.cafe.model.dto;

import com.epam.jwd.cafe.model.Role;
import java.util.Objects;

/**
 * The class representation {@link com.epam.jwd.cafe.model.User} in session
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class UserDto {
    private final int id;
    private final Role role;
    private final boolean isBlocked;

    public UserDto(int id, Role role, boolean isBlocked) {
        this.id = id;
        this.role = role;
        this.isBlocked = isBlocked;
    }

    public int getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return id == userDto.id && role == userDto.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role);
    }
}
