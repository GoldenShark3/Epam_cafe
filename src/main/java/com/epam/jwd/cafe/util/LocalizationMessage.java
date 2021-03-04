package com.epam.jwd.cafe.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationMessage {
    private LocalizationMessage() {
    }

    public static String localize(String locale, String message) {
        String[] parsedLocale = locale.split("_");
        ResourceBundle resourceBundle = ResourceBundle.getBundle("content",
                new Locale(parsedLocale[0], parsedLocale[1]));
        return resourceBundle.getString(message);
    }
}
