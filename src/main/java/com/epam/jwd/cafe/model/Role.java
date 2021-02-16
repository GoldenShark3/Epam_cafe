package com.epam.jwd.cafe.model;

import com.epam.jwd.cafe.exception.EntityNotFoundException;

public enum Role {
    ADMIN,
    USER;

    public static Role resolveRoleById(int id) throws EntityNotFoundException {
        Role role;
        try{
            role = Role.values()[id - 1];
        }  catch(ArrayIndexOutOfBoundsException e) {
            throw new EntityNotFoundException("Role with id: " + id + " - not found");
        }
        return role;
    }
}
