package com.banking.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String CONFIG_FILE = "database.properties";
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    
    // Default configuration (fallback)
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/smart_banking?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DEFAULT_USERNAME = "root";
    private static final String DEFAULT_PASSWORD = "root";

    static {
        loadConfiguration();
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }

    private static void loadConfiguration() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            props.load(fis);
            URL = props.getProperty("db.url", DEFAULT_URL);
            USERNAME = props.getProperty("db.username", DEFAULT_USERNAME);
            PASSWORD = props.getProperty("db.password", DEFAULT_PASSWORD);
            System.out.println("Database configuration loaded from file.");
        } catch (IOException e) {
            // Use default configuration
            URL = DEFAULT_URL;
            USERNAME = DEFAULT_USERNAME;
            PASSWORD = DEFAULT_PASSWORD;
            System.out.println("Using default database configuration.");
        }
    }

    /**
     * Get a database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    /**
     * Close a database connection
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Test database connection
     * @return true if connection successful
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get database URL
     * @return database URL
     */
    public static String getUrl() {
        return URL;
    }

    /**
     * Get database username
     * @return database username
     */
    public static String getUsername() {
        return USERNAME;
    }
}
