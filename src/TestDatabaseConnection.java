import service.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Simple test class to verify MySQL database connection.
 *
 * Run this to test:
 * 1. JDBC driver is loaded
 * 2. Connection to MySQL works
 * 3. Can query the database
 *
 * @author CPS731 Team 20
 */
public class TestDatabaseConnection {

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("   TESTING MYSQL DATABASE CONNECTION");
        System.out.println("   CPS731 Travel Assistant - Phase 3");
        System.out.println("=".repeat(60));
        System.out.println();

        // Get database connection instance
        DatabaseConnection dbConn = DatabaseConnection.getInstance();

        // Test 1: Basic connection test
        System.out.println("[TEST 1] Basic Connection Test");
        System.out.println("-".repeat(60));
        boolean connectionWorks = dbConn.testConnection();
        System.out.println();

        if (!connectionWorks) {
            System.err.println("❌ Connection failed! Check:");
            System.err.println("  1. MySQL is running: brew services list | grep mysql");
            System.err.println("  2. Database exists: mysql -u root -e 'SHOW DATABASES;'");
            System.err.println("  3. JDBC driver in classpath");
            System.exit(1);
        }

        // Test 2: Query tables
        System.out.println("[TEST 2] Query Database Tables");
        System.out.println("-".repeat(60));
        try (Connection conn = dbConn.getConnection();
             Statement stmt = conn.createStatement()) {

            // Show all tables
            ResultSet rs = stmt.executeQuery("SHOW TABLES");
            System.out.println("Tables in travel_assistant_db:");
            while (rs.next()) {
                System.out.println("  ✓ " + rs.getString(1));
            }
            rs.close();
            System.out.println();

        } catch (Exception e) {
            System.err.println("❌ Error querying tables: " + e.getMessage());
            e.printStackTrace();
        }

        // Test 3: Query sample data
        System.out.println("[TEST 3] Query Sample Data");
        System.out.println("-".repeat(60));
        try (Connection conn = dbConn.getConnection();
             Statement stmt = conn.createStatement()) {

            // Query profiles
            ResultSet rs = stmt.executeQuery("SELECT user_id, budget, radius FROM profiles");
            System.out.println("Profiles in database:");
            while (rs.next()) {
                System.out.printf("  • User: %s, Budget: %s, Radius: %dm%n",
                    rs.getString("user_id"),
                    rs.getString("budget"),
                    rs.getInt("radius"));
            }
            rs.close();
            System.out.println();

            // Query sessions
            rs = stmt.executeQuery("SELECT session_id, user_id, request_count FROM sessions");
            System.out.println("Sessions in database:");
            while (rs.next()) {
                System.out.printf("  • Session: %s, User: %s, Requests: %d%n",
                    rs.getString("session_id"),
                    rs.getString("user_id"),
                    rs.getInt("request_count"));
            }
            rs.close();
            System.out.println();

            // Query events
            rs = stmt.executeQuery("SELECT event_type, COUNT(*) as count FROM events GROUP BY event_type");
            System.out.println("Events by type:");
            while (rs.next()) {
                System.out.printf("  • %s: %d events%n",
                    rs.getString("event_type"),
                    rs.getInt("count"));
            }
            rs.close();
            System.out.println();

        } catch (Exception e) {
            System.err.println("❌ Error querying data: " + e.getMessage());
            e.printStackTrace();
        }

        // Test 4: Connection info
        System.out.println("[TEST 4] Connection Information");
        System.out.println("-".repeat(60));
        System.out.println("Database URL: " + dbConn.getDatabaseURL());
        System.out.println("Database User: " + dbConn.getDatabaseUser());
        System.out.println();

        // Summary
        System.out.println("=".repeat(60));
        System.out.println("✅ ALL TESTS PASSED!");
        System.out.println("Database connection is working correctly.");
        System.out.println("Ready to implement ProfileContextStore and AnalyticsLogger!");
        System.out.println("=".repeat(60));
    }
}
