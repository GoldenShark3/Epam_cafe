package com.epam.jwd.cafe.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * The class provide encrypt user password
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class PasswordEncoder {

    private PasswordEncoder() {
    }

    public static String encryptPassword(String password) {
        return DigestUtils.md5Hex(password);
    }
}
