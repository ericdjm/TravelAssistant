# TravelAssistant

CPS731 - Software Engineering I - Phase 3 Implementation

## Team 20

- Hussein Saab - 501178356
- Eric Mergelas - 500845404
- Omar Ahmed - 501178119

---

## Project Overview

TravelAssistant is a conversational travel planning system that helps users discover nearby places, generate micro-itineraries, and receive personalized travel recommendations based on preferences, location, and context.

The system allows users to:
- Set travel preferences (interests, budget, radius, transport mode)
- Discover nearby points of interest (restaurants, museums, parks, cafes, etc.)
- View ranked recommendation cards based on rating, distance, and preferences
- Generate micro-itineraries with step-by-step directions and ETAs
- Save and load user profiles for personalized experiences

---

## Project Structure

```
TravelAssistant/
├── src/
│   ├── Main.java                    # Application entry point
│   ├── controller/
│   │   └── ConversationEngine.java  # Main controller, orchestrates flow
│   ├── model/
│   │   ├── RecommendationEngine.java    # POI ranking and itinerary generation
│   │   ├── RecommendationCard.java      # Card data structure
│   │   ├── Itinerary.java               # Itinerary with steps and ETAs
│   │   ├── ProfileContextStore.java     # MySQL persistence for profiles/sessions
│   │   ├── Profile.java                 # User profile entity
│   │   ├── Session.java                 # Session entity
│   │   └── AnalyticsLogger.java         # Event logging and rate limiting
│   ├── view/
│   │   ├── MobileAppUI.java             # Main Swing UI frame
│   │   ├── MainMenuPanel.java           # Home screen
│   │   ├── PlanningPanel.java           # Planning interface with cards
│   │   ├── SignInPanel.java             # User sign-in
│   │   ├── SettingsPanel.java           # Settings screen
│   │   └── AdminConsole.java            # Admin interface
│   ├── service/
│   │   ├── IntegrationLayer.java        # Facade for external services
│   │   ├── PlacesService.java           # POI data from MySQL
│   │   ├── TransitService.java          # ETA calculations
│   │   ├── WeatherService.java          # Weather data
│   │   ├── DatabaseConnection.java      # MySQL connection manager
│   │   └── I*.java                      # Service interfaces
│   └── domain/
│       ├── Preferences.java             # User preferences
│       ├── Context.java                 # Session context (location, weather, time)
│       ├── POI.java                     # Point of interest
│       ├── LatLng.java                  # Coordinates
│       ├── ETA.java                     # Estimated time of arrival
│       ├── Weather.java                 # Weather conditions
│       └── ...                          # Other domain objects
├── test/
│   ├── controller/
│   │   └── ConversationEngineTest.java
│   ├── model/
│   │   ├── ProfileContextStoreTest.java
│   │   ├── RecommendationEngineTest.java
│   │   ├── ItineraryTest.java
│   │   └── AnalyticsLoggerTest.java
│   ├── service/
│   │   └── IntegrationLayerTest.java
│   ├── domain/
│   │   └── PreferencesTest.java
│   └── README_TESTS.md
├── docs/
│   ├── mysql.md                         # Database setup guide
│   ├── run_app.md                       # Application running guide
│   ├── schema.sql                       # Database schema
│   └── schema_pois.sql                  # POI data inserts
├── lib/
│   └── mysql-connector-j-9.5.0.jar      # MySQL JDBC driver
└── bin/                                 # Compiled class files
```

---

## Architecture

The application follows the MVC (Model-View-Controller) pattern with GRASP principles:

### MVC Components

- View: MobileAppUI, PlanningPanel, SignInPanel, SettingsPanel (Java Swing)
- Controller: ConversationEngine (orchestrates user actions and system responses)
- Model: RecommendationEngine, ProfileContextStore, AnalyticsLogger, domain entities

### GRASP Principles Applied

1. Information Expert
   - ProfileContextStore manages profile/session persistence
   - Itinerary computes total ETA from its steps
   - RecommendationEngine ranks POIs using preferences and context

2. Creator
   - RecommendationEngine creates RecommendationCard and Itinerary objects

3. Low Coupling / High Cohesion
   - UI components don't know about database or ranking internals
   - Each class has a single, focused responsibility

4. Indirection / Protected Variations
   - Service interfaces (IPlacesService, ITransitService, IWeatherService) hide implementation details
   - IntegrationLayer provides a stable facade for external services

---

## Prerequisites

1. Java Development Kit (JDK) 8 or higher
2. MySQL 8.0 or higher
3. JUnit 5 Platform Console Standalone (for running tests)

---

## Setup Instructions

### Step 1: Set Up MySQL Database

Follow the instructions in docs/mysql.md or use these quick commands:

macOS:
```bash
brew install mysql
brew services start mysql
mysql -u root -e "CREATE DATABASE travel_assistant_db;"
```

Windows:
1. Download MySQL Installer from https://dev.mysql.com/downloads/installer/
2. Install and start MySQL Server
3. Create database: mysql -u root -p -e "CREATE DATABASE travel_assistant_db;"

### Step 2: Create Tables

```bash
cd TravelAssistant
mysql -u root travel_assistant_db < docs/schema.sql
mysql -u root travel_assistant_db < docs/schema_pois.sql
```

### Step 3: Verify Database Setup

```bash
mysql -u root -e "USE travel_assistant_db; SHOW TABLES;"
```

Expected output: 5 tables (conversation_history, events, pois, profiles, sessions)

---

## How to Compile

```bash
cd TravelAssistant

javac -cp "lib/mysql-connector-j-9.5.0.jar:src" \
  -d bin \
  src/domain/*.java \
  src/service/*.java \
  src/model/*.java \
  src/controller/*.java \
  src/view/*.java \
  src/*.java
```

---

## How to Run

```bash
java -cp "lib/mysql-connector-j-9.5.0.jar:bin" Main
```

The application will launch a Java Swing GUI where you can:
1. Sign in or continue as guest
2. Set your preferences (interests, budget, radius, transport mode)
3. Click "Start Planning" to get recommendations
4. View recommendation cards ranked by relevance
5. Click "View Itinerary" on any card to see step-by-step directions

---

## How to Run Tests

### Download JUnit 5

```bash
curl -o junit-platform-console-standalone-1.10.1.jar \
  https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.1/junit-platform-console-standalone-1.10.1.jar
```

### Compile Tests

```bash
javac -cp "bin:junit-platform-console-standalone-1.10.1.jar" \
  -d bin \
  test/domain/*.java \
  test/service/*.java \
  test/model/*.java \
  test/controller/*.java \
  test/TestRunner.java
```

### Run All Tests

```bash
java -jar junit-platform-console-standalone-1.10.1.jar \
  --class-path "bin:lib/mysql-connector-j-9.5.0.jar" \
  --scan-class-path \
  --include-package test
```

### Run Tests by Package

```bash
# Run all model tests
java -jar junit-platform-console-standalone-1.10.1.jar \
  --class-path "bin:lib/mysql-connector-j-9.5.0.jar" \
  --select-package test.model
```

### Run a Single Test Class

```bash
java -jar junit-platform-console-standalone-1.10.1.jar \
  --class-path "bin:lib/mysql-connector-j-9.5.0.jar" \
  --select-class test.model.ProfileContextStoreTest
```

See test/README_TESTS.md for more details on running individual tests.

---

## Test Coverage

The test suite includes 106+ test cases across 7 test classes:

| Test Class | Tests | Coverage |
|------------|-------|----------|
| PreferencesTest | 21 | Getters, setters, constructors, validation |
| ProfileContextStoreTest | 11 | Save/load profiles, sessions, MySQL persistence |
| RecommendationEngineTest | 14 | POI ranking, candidate fetching, caching |
| ItineraryTest | 17 | Itinerary creation, ETA computation, steps |
| AnalyticsLoggerTest | 13 | Event logging, rate limiting, reports |
| IntegrationLayerTest | 19 | Service integration, geocoding, weather, ETAs |
| ConversationEngineTest | 14 | Session management, planning flow, preferences |

---

## Database Schema

The application uses MySQL with the following tables:

- profiles: User preferences and settings
- sessions: User session tracking
- conversation_history: Chat message history
- events: Analytics and interaction logging
- pois: Points of interest (47 Toronto locations)

See docs/schema.sql and docs/schema_pois.sql for full schema definitions.

---

## Key Features

1. Personalized Recommendations
   - Filter POIs by interests, budget, and radius
   - Rank results by rating, distance, and preference match

2. Micro-Itinerary Generation
   - Step-by-step directions to selected POIs
   - ETA calculations for walking, driving, or transit

3. User Profiles
   - Save preferences for returning users
   - Session tracking for analytics

4. Real-time Context
   - Weather-aware recommendations
   - Time-of-day considerations

5. Rate Limiting
   - Prevents API abuse
   - Logged via AnalyticsLogger

---

## Documentation

- docs/mysql.md - Database setup guide (macOS and Windows)
- docs/run_app.md - Application running instructions
- docs/schema.sql - Database table definitions
- docs/schema_pois.sql - Sample POI data for Toronto
- test/README_TESTS.md - Test execution guide

---

## Requirements Traceability

| Use Case | Functional Requirements | Key Classes |
|----------|------------------------|-------------|
| UC-1: Discover Places | FR-1, FR-2, FR-10, FR-11 | ConversationEngine, RecommendationEngine |
| UC-2: Refine Results | FR-4 | ConversationEngine.adjustPreferences() |
| UC-4: Sign In | FR-5, FR-6 | ProfileContextStore, Profile |
| UC-5: View Itinerary | FR-11, FR-12 | Itinerary, RecommendationCard |
| UC-6: Rate Limiting | FR-20, NFR-3 | AnalyticsLogger.detectRateLimit() |

---

## Contact

- Hussein Saab - hsaab@torontomu.ca
- Eric Mergelas - eric.mergelas@torontomu.ca
- Omar Ahmed - omar.ahmed1@torontomu.ca

---

Last Updated: November 27, 2024
CPS731 - Toronto Metropolitan University
