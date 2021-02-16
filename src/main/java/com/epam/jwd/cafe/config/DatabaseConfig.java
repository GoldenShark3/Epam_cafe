package com.epam.jwd.cafe.config;

import com.epam.jwd.cafe.util.PropertyReaderUtil;

import java.util.Properties;

public class DatabaseConfig {
    private static DatabaseConfig instance;

    private static final String PROPERTIES_NAME = "database";
    private static final String JDBC_URL = "db.jdbc.url";
    private static final String LOGIN = "db.login";
    private static final String PASSWORD = "db.password";
    private static final String INIT_POOL_SIZE = "db.initPoolSize";
    private static final String MAX_POOL_SIZE = "db.maxPoolSize";
    private static final String HOST = "db.host";
    private static final String PORT = "db.port";
    private static final String NAME = "db.name";

    private String jdbcUrl;
    private String login;
    private String password;
    private int initPoolSize;
    private int maxPoolSize;
    private String host;
    private String port;
    private String name;

    public static DatabaseConfig getInstance(){
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    private DatabaseConfig() {
        initConfig();
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public int getInitPoolSize() {
        return initPoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public void initConfig() {
        Properties properties = PropertyReaderUtil.readProperties(PROPERTIES_NAME);
        jdbcUrl = properties.getProperty(JDBC_URL);
        login = properties.getProperty(LOGIN);
        password = properties.getProperty(PASSWORD);
        initPoolSize = Integer.parseInt(properties.getProperty(INIT_POOL_SIZE));
        maxPoolSize = Integer.parseInt(properties.getProperty(MAX_POOL_SIZE));
        host = properties.getProperty(HOST);
        port = properties.getProperty(PORT);
        name = properties.getProperty(NAME);
    }

    public String retrieveDatabaseURL() {
        return String.format("%s%s:%s/%s", jdbcUrl, host, port, name);
    }

}
