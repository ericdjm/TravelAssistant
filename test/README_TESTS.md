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
javac -cp "bin:junit-platform-console-standalone-1.10.1.jar" \
      -d bin \                                            
      test/domain/*.java \
      test/service/*.java \ 
      test/model/*.java \
      test/controller/*.java \
      test/TestRunner.java
```

## Running Tests

### Run All Tests

```bash
java -jar junit-platform-console-standalone-1.10.1.jar \
  --class-path "bin:lib/mysql-connector-j-9.5.0.jar" \
  --scan-class-path \
  --include-package test
```

---

### Run Tests by Folder/Package

Run all tests in a specific package (folder):

```bash
# Run all tests in test/model/ folder
java -jar junit-platform-console-standalone-1.10.1.jar \
  --class-path "bin:lib/mysql-connector-j-9.5.0.jar" \
  --select-package test.model

# Run all tests in test/service/ folder
java -jar junit-platform-console-standalone-1.10.1.jar \
  --class-path "bin:lib/mysql-connector-j-9.5.0.jar" \
  --select-package test.service

# Run all tests in test/controller/ folder
java -jar junit-platform-console-standalone-1.10.1.jar \
  --class-path "bin:lib/mysql-connector-j-9.5.0.jar" \
  --select-package test.controller

# Run all tests in test/domain/ folder
java -jar junit-platform-console-standalone-1.10.1.jar \
  --class-path "bin:lib/mysql-connector-j-9.5.0.jar" \
  --select-package test.domain
```

---

### Run a Single Test Class

Run all tests in one specific test file:

```bash
# Run only ProfileContextStoreTest (11 tests)
java -jar junit-platform-console-standalone-1.10.1.jar \
  --class-path "bin:lib/mysql-connector-j-9.5.0.jar" \
  --select-class test.model.ProfileContextStoreTest

# Run only RecommendationEngineTest (14 tests)
java -jar junit-platform-console-standalone-1.10.1.jar \
  --class-path "bin:lib/mysql-connector-j-9.5.0.jar" \
  --select-class test.model.RecommendationEngineTest

# Run only ConversationEngineTest (14 tests)
java -jar junit-platform-console-standalone-1.10.1.jar \
  --class-path "bin:lib/mysql-connector-j-9.5.0.jar" \
  --select-class test.controller.ConversationEngineTest

# Run only IntegrationLayerTest (19 tests)
java -jar junit-platform-console-standalone-1.10.1.jar \
  --class-path "bin:lib/mysql-connector-j-9.5.0.jar" \
  --select-class test.service.IntegrationLayerTest

# Run only PreferencesTest (21 tests)
java -jar junit-platform-console-standalone-1.10.1.jar \
  --class-path "bin:lib/mysql-connector-j-9.5.0.jar" \
  --select-class test.domain.PreferencesTest
```

---

### Run a Single Test Method

Run one specific test method from a class:

```bash
# Example: Run only "testSaveAndLoadProfile" from ProfileContextStoreTest
java -jar junit-platform-console-standalone-1.10.1.jar \
  --class-path "bin:lib/mysql-connector-j-9.5.0.jar" \
  --select-method "test.model.ProfileContextStoreTest#testSaveAndLoadProfile"

# Example: Run only "testRankPOIsValid" from RecommendationEngineTest
java -jar junit-platform-console-standalone-1.10.1.jar \
  --class-path "bin:lib/mysql-connector-j-9.5.0.jar" \
  --select-method "test.model.RecommendationEngineTest#testRankPOIsValid"

# Example: Run only "testStartPlanningValid" from ConversationEngineTest
java -jar junit-platform-console-standalone-1.10.1.jar \
  --class-path "bin:lib/mysql-connector-j-9.5.0.jar" \
  --select-method "test.controller.ConversationEngineTest#testStartPlanningValid"
```

---

### Quick Reference Table

| What to Run | Flag to Use | Example |
|-------------|-------------|---------|
| All tests | `--scan-class-path --include-package test` | Run entire test suite |
| One folder | `--select-package test.model` | All tests in `test/model/` |
| One class | `--select-class test.model.ItineraryTest` | All tests in `ItineraryTest.java` |
| One method | `--select-method "test.model.ItineraryTest#testCreateItinerary"` | Single test method |

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
[     106 tests successful      ]
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
