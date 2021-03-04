package com.epam.jwd.cafe.model.dto;

import com.epam.jwd.cafe.model.Role;
import java.util.Objects;

public class UserDto {
    private final int id;
    private final Role role;

    public UserDto(int id, Role role) {
        this.id = id;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public Role getRole() {
        return role;
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
