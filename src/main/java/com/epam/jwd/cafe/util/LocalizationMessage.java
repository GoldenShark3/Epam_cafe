package com.epam.jwd.cafe.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The class used to localize content in the server
 *
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class LocalizationMessage {
    private LocalizationMessage() {
    }

    /**
     * Localize message in the server
     *
     * @param locale  the locale of current user
     * @param message name of content in property
     * @return Localized message
     */
    public static String localize(String locale, String message) {
        String[] parsedLocale = locale.split("_");
        ResourceBundle resourceBundle = ResourceBundle.getBundle("content",
                new Locale(parsedLocale[0], parsedLocale[1]));
        return resourceBundle.getString(message);
    }
}
