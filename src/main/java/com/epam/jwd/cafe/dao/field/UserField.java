package com.epam.jwd.cafe.dao.field;

import com.epam.jwd.cafe.model.User;

/**
 * The class representation of {@link User} fields
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public enum UserField implements EntityField<User> {
    ID,
    USERNAME,
    FIRSTNAME,
    LASTNAME,
    PHONE_NUMBER,
    EMAIL
}
