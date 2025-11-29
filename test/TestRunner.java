package test;

/**
 * Main test runner for TravelAssistant project.
 *
 * Usage: Run tests directly with JUnit Platform Console Launcher:
 *   java -jar junit-platform-console-standalone-1.10.1.jar \
 *     --class-path bin \
 *     --scan-class-path \
 *     --include-package test
 */
public class TestRunner {

    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("   TRAVELASSISTANT - JUNIT 5 TEST SUITE");
        System.out.println("=".repeat(80));
        System.out.println();
        System.out.println("Please run tests using the JUnit Platform Console Launcher:");
        System.out.println();
        System.out.println("  java -jar junit-platform-console-standalone-1.10.1.jar \\");
        System.out.println("    --class-path bin \\");
        System.out.println("    --scan-class-path \\");
        System.out.println("    --include-package test");
        System.out.println();
    }
}
