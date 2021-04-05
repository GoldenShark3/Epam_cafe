package com.epam.jwd.cafe.config;

import com.epam.jwd.cafe.util.PropertyReaderUtil;
import java.util.Properties;

/**
 * The class represented all data from app.properties file
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class ApplicationConfig {
    private static ApplicationConfig instance;

    private static final String PROPERTIES_NAME = "app";
    private static final String POINTS_PER_DOLLAR = "app.pointsPerDollar";
    private static final String POINTS_TO_BLOCK = "app.pointsToBlock";

    private Integer loyaltyPointsPerDollar;
    private Integer loyaltyPointsToBlock;

    private ApplicationConfig() {
        initConfig();
    }

    public static ApplicationConfig getInstance(){
        if (instance == null) {
            instance = new ApplicationConfig();
        }
        return instance;
    }

    public Integer getLoyaltyPointsPerDollar() {
        return loyaltyPointsPerDollar;
    }

    public Integer getLoyaltyPointsToBlock() {
        return loyaltyPointsToBlock;
    }

    private void initConfig() {
        Properties properties = PropertyReaderUtil.readProperties(PROPERTIES_NAME);
        loyaltyPointsPerDollar = Integer.parseInt(properties.getProperty(POINTS_PER_DOLLAR));
        loyaltyPointsToBlock = Integer.parseInt(properties.getProperty(POINTS_TO_BLOCK));

    }

}
