package com.epam.jwd.cafe.dao.field;

import com.epam.jwd.cafe.model.User;

public enum UserField implements EntityField<User> {
    ID,
    USERNAME,
    FIRSTNAME,
    LASTNAME,
    PHONE_NUMBER,
    EMAIL
}
