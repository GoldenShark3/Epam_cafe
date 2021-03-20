package com.epam.jwd.cafe.util;

import java.io.File;

public class IOUtil {
    private static final String DATA_DIR = "C:\\Users\\Aleksey\\Desktop\\EPAM\\EpamCafe\\src\\main\\webapp\\data\\";

    private IOUtil() {
    }

    public static void deleteData(String fileName) {
        File file = new File(DATA_DIR + fileName);
        file.delete();
//        if (!file.delete()) {
            //todo: log.error("Failed to delete upload with filename: " + filename);
//        }
    }
}
