# TravelAssistant - JUnit 5 Test Suite

## Overview

This directory contains comprehensive JUnit 5 test suites for the TravelAssistant project. All tests follow JUnit 5 (Jupiter) best practices with multiple test cases covering normal flows, edge cases, and error conditions.

## Test Structure

```
test/
├── TestRunner.java              # Main test runner
├── controller/
│   └── ConversationEngineTest.java    # Tests for conversation flow
├── domain/
│   └── PreferencesTest.java           # Tests for preferences domain
├── model/
│   ├── AnalyticsLoggerTest.java       # Tests for analytics logging
│   ├── ItineraryTest.java             # Tests for itinerary building
│   ├── ProfileContextStoreTest.java   # Tests for profile persistence
│   └── RecommendationEngineTest.java  # Tests for POI ranking
└── service/
    └── IntegrationLayerTest.java      # Tests for external services
```

## Prerequisites

1. **JUnit 5 Platform Console Standalone JAR**
   - Download from: https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/
   - Recommended version: 1.10.1 or higher
   - Save as: `junit-platform-console-standalone-1.10.1.jar` in your project root

2. **Java Development Kit (JDK)**
   - Java 8 or higher required
   - Java 11+ recommended

## Setup Instructions

### Step 1: Download JUnit 5

```bash
# Navigate to your project root
cd /Users/emergelas/Documents/GitHub/TravelAssistant

# Download JUnit 5 Platform Console Standalone
curl -o junit-platform-console-standalone-1.10.1.jar \
  https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.1/junit-platform-console-standalone-1.10.1.jar
```

### Step 2: Compile All Source Code

```bash
# Compile main source code first
javac -d . src/domain/*.java
javac -d . src/service/*.java
javac -d . src/model/*.java
javac -d . src/controller/*.java
```

### Step 3: Compile Test Code

```bash
# Compile all test files
javac -cp ".:junit-platform-console-standalone-1.10.1.jar" \
  test/domain/*.java \
  test/service/*.java \
  test/model/*.java \
  test/controller/*.java \
  test/TestRunner.java
```

## Running Tests

### Option 1: Run All Tests Using JUnit Console Launcher

```bash
java -jar junit-platform-console-standalone-1.10.1.jar \
  --class-path . \
  --scan-class-path \
  --details tree
```

### Option 2: Run All Tests Using TestRunner

```bash
java -cp ".:junit-platform-console-standalone-1.10.1.jar" test.TestRunner
```

### Option 3: Run Specific Test Class

```bash
# Run only ProfileContextStoreTest
java -jar junit-platform-console-standalone-1.10.1.jar \
  --class-path . \
  --select-class test.model.ProfileContextStoreTest

# Run only ConversationEngineTest
java -jar junit-platform-console-standalone-1.10.1.jar \
  --class-path . \
  --select-class test.controller.ConversationEngineTest
```

### Option 4: Run Tests by Package

```bash
# Run all model tests
java -jar junit-platform-console-standalone-1.10.1.jar \
  --class-path . \
  --select-package test.model

# Run all service tests
java -jar junit-platform-console-standalone-1.10.1.jar \
  --class-path . \
  --select-package test.service
```

## Test Coverage

### 1. **ProfileContextStoreTest** (18 tests)
- ✅ Save and load profiles
- ✅ Save and load sessions
- ✅ Null handling and validation
- ✅ Update existing profiles
- ✅ Multiple sessions per user
- ✅ Accessibility needs handling

### 2. **RecommendationEngineTest** (18 tests)
- ✅ Fetch candidates with various queries
- ✅ Rank POIs by score
- ✅ Different budget preferences
- ✅ Different interests filtering
- ✅ Radius-based filtering
- ✅ Cache functionality
- ✅ Accessibility support

### 3. **ConversationEngineTest** (13 tests)
- ✅ Start planning flow
- ✅ Refine search functionality
- ✅ User sign-in handling
- ✅ Different transport modes
- ✅ Multiple sessions
- ✅ Complete workflow integration

### 4. **PreferencesTest** (24 tests)
- ✅ Constructor validation
- ✅ All getters and setters
- ✅ Budget levels (low/medium/high)
- ✅ Transport modes
- ✅ Radius settings
- ✅ Accessibility options
- ✅ Interest list handling

### 5. **IntegrationLayerTest** (16 tests)
- ✅ Get nearby places
- ✅ Get ETAs
- ✅ Geocoding addresses
- ✅ Weather retrieval
- ✅ Service wiring
- ✅ Different transport modes

### 6. **AnalyticsLoggerTest** (13 tests)
- ✅ Log interactions
- ✅ Rate limit detection
- ✅ Report generation
- ✅ Multiple event types
- ✅ Multiple users
- ✅ Complete analytics flow

### 7. **ItineraryTest** (16 tests)
- ✅ Create itinerary from POIs
- ✅ Compute total ETA
- ✅ Steps management
- ✅ Different transport modes
- ✅ Single and multiple POIs
- ✅ Complete workflow

## Total Test Count: **118+ Test Cases**

## Expected Output

When running tests successfully, you should see output like:

```
╷
├─ JUnit Jupiter ✔
│  ├─ ProfileContextStoreTest ✔
│  │  ├─ testSaveAndLoadProfile() ✔
│  │  ├─ testSaveProfileNullUserId() ✔
│  │  ├─ testSaveNullProfile() ✔
│  │  └─ ... (15 more tests)
│  ├─ RecommendationEngineTest ✔
│  │  ├─ testFetchCandidatesValid() ✔
│  │  ├─ testRankPOIsValid() ✔
│  │  └─ ... (16 more tests)
│  └─ ... (5 more test classes)
└─ JUnit Vintage ✔

Test run finished after XXX ms
[     118 tests successful      ]
[       0 tests failed          ]
```

## Troubleshooting

### Issue: "org.junit cannot be resolved"
**Solution:** Ensure JUnit JAR is in classpath during compilation:
```bash
javac -cp ".:junit-platform-console-standalone-1.10.1.jar" test/**/*.java
```

### Issue: "Class not found"
**Solution:** Compile source code before tests:
```bash
# Compile source first
javac -d . src/**/*.java
# Then compile tests
javac -cp ".:junit-platform-console-standalone-1.10.1.jar" test/**/*.java
```

### Issue: "No tests found"
**Solution:** Use `--scan-class-path` flag:
```bash
java -jar junit-platform-console-standalone-1.10.1.jar \
  --class-path . \
  --scan-class-path
```

## Build Script (Optional)

Create a `compile_tests.sh` script:

```bash
#!/bin/bash

echo "Compiling TravelAssistant Test Suite..."

# Compile source code
echo "Step 1: Compiling source code..."
javac -d . src/domain/*.java
javac -d . src/service/*.java
javac -d . src/model/*.java
javac -d . src/controller/*.java

# Compile tests
echo "Step 2: Compiling tests..."
javac -cp ".:junit-platform-console-standalone-1.10.1.jar" \
  test/domain/*.java \
  test/service/*.java \
  test/model/*.java \
  test/controller/*.java \
  test/TestRunner.java

echo "Compilation complete!"
echo ""
echo "Run tests with:"
echo "  java -jar junit-platform-console-standalone-1.10.1.jar --class-path . --scan-class-path"
```

Make it executable:
```bash
chmod +x compile_tests.sh
./compile_tests.sh
```

## Test Execution Script (Optional)

Create a `run_tests.sh` script:

```bash
#!/bin/bash

echo "Running TravelAssistant Test Suite..."
echo ""

java -jar junit-platform-console-standalone-1.10.1.jar \
  --class-path . \
  --scan-class-path \
  --details tree

echo ""
echo "Test execution complete!"
```

Make it executable:
```bash
chmod +x run_tests.sh
./run_tests.sh
```

## Notes

1. **Database Tests**: ProfileContextStoreTest may require MySQL database connection. Ensure database is configured in `DatabaseConnection.java`

2. **Mock Data**: Some tests use mock/stub data from services like `PlacesService`, `TransitService`, and `WeatherService`

3. **Test Independence**: All tests are independent and can run in any order

4. **Cleanup**: Tests use `@BeforeEach` and `@AfterEach` for setup and cleanup

## Contributing

When adding new tests:
1. Follow the existing naming convention: `<ClassName>Test.java`
2. Use `@DisplayName` for descriptive test names
3. Group related tests with comments
4. Include edge cases and error conditions
5. Ensure tests are independent and repeatable

## License

See main project LICENSE file.
