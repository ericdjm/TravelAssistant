package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection utility class using Singleton pattern.
 * Manages connections to MySQL database for Travel Assistant system.
 *
 * GRASP: Indirection - Isolates database connection logic from business classes
 *
 * Traceability:
 * - NFR-5: Secure connection management
 * - All persistence operations (ProfileContextStore, AnalyticsLogger)
 *
 * @author CPS731 Team 20 (Hussein, Eric, Omar)
 */
public class DatabaseConnection {

    // Singleton instance
    private static DatabaseConnection instance;

    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/travel_assistant_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // Empty for local dev (no password)

    // JDBC Driver class name
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * Private constructor to enforce Singleton pattern.
     * Loads the MySQL JDBC driver.
     */
    private DatabaseConnection() {
        try {
            // Load MySQL JDBC Driver
            Class.forName(JDBC_DRIVER);
            System.out.println("✓ MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ ERROR: MySQL JDBC Driver not found!");
            System.err.println("Please add mysql-connector-j jar to classpath");
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    /**
     * Get singleton instance of DatabaseConnection.
     * Thread-safe lazy initialization.
     *
     * @return DatabaseConnection singleton instance
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Get a connection to the MySQL database.
     *
     * Connection parameters:
     * - URL: jdbc:mysql://localhost:3306/travel_assistant_db
     * - User: root
     * - Password: (empty for local development)
     *
     * @return Connection object to MySQL database
     * @throws SQLException if connection fails
     */
    public Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("✓ Connected to MySQL database: travel_assistant_db");
            return conn;
        } catch (SQLException e) {
            System.err.println("❌ ERROR: Failed to connect to database");
            System.err.println("URL: " + DB_URL);
            System.err.println("User: " + DB_USER);
            System.err.println("Error: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Test the database connection.
     * Useful for verifying setup.
     *
     * @return true if connection successful, false otherwise
     */
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Database connection test: SUCCESS");
                System.out.println("   Database: " + conn.getCatalog());
                System.out.println("   Driver: " + conn.getMetaData().getDriverName());
                System.out.println("   Version: " + conn.getMetaData().getDriverVersion());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Database connection test: FAILED");
            System.err.println("   Error: " + e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * Close a database connection safely.
     * Handles null connections gracefully.
     *
     * @param conn Connection to close
     */
    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("✓ Database connection closed");
            } catch (SQLException e) {
                System.err.println("⚠️ Warning: Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Get database URL (useful for debugging).
     *
     * @return Database connection URL
     */
    public String getDatabaseURL() {
        return DB_URL;
    }

    /**
     * Get database user (useful for debugging).
     *
     * @return Database username
     */
    public String getDatabaseUser() {
        return DB_USER;
    }
}
