package test;

import org.junit.platform.console.ConsoleLauncher;

/**
 * Main test runner for TravelAssistant project.
 * Executes all JUnit 5 test suites.
 * 
 * Usage:
 *   Compile: javac -cp ".:junit-platform-console-standalone-1.10.1.jar" test/domain/*.java
 *   Run: java -jar junit-platform-console-standalone-1.10.1.jar --class-path . --scan-class-path
 * 
 * This runner discovers and executes all tests in the test package.
 */
public class TestRunner {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("   TRAVELASSISTANT - JUNIT 5 TEST SUITE");
        System.out.println("=".repeat(80));
        System.out.println();
        
        // Use JUnit Platform Console Launcher to run all tests
        String[] testArgs = {
            "--scan-class-path",
            "--disable-ansi-colors",
            "--details=tree"
        };
        int exitCode = ConsoleLauncher.execute(System.out, System.err, testArgs).getExitCode();
        
        System.out.println();
        System.out.println("=".repeat(80));
        System.out.println("   TEST EXECUTION COMPLETED - Exit Code: " + exitCode);
        System.out.println("=".repeat(80));
        
        System.exit(exitCode);
    }
}
