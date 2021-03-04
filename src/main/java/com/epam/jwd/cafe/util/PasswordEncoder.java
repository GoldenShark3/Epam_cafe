package com.epam.jwd.cafe.util;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordEncoder {

    private PasswordEncoder() {
    }

    public static String encryptPassword(String password) {
        return DigestUtils.md5Hex(password);
    }
}
