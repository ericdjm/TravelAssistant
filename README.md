# CPS731 Travel Assistant - Phase 3 Implementation

**Team 20**
- Hussein Saab - 501178356
- Eric Mergelas - 500845404
- Omar Ahmed - 501178119

---

## Project Overview

A conversational travel planning system that helps users discover nearby places, generate micro-itineraries, and get real-time travel recommendations based on preferences, location, and context.

**Architecture**: MVC with GRASP principles (Information Expert, Creator, Low Coupling/High Cohesion, Indirection/Protected Variations)

---

## Project Structure

```
TravelAssistant/
├── .gitignore              # Excludes .class files from git
├── README.md               # This file
└── src/
    ├── view/               # MVC View - UI Components
    │   ├── MobileAppUI.java
    │   └── AdminConsole.java
    ├── controller/         # MVC Controller - Application Logic
    │   └── ConversationEngine.java
    ├── model/              # MVC Model - Business Entities
    │   ├── RecommendationEngine.java
    │   ├── RecommendationCard.java
    │   ├── Itinerary.java
    │   ├── Profile.java
    │   ├── Session.java
    │   ├── ProfileContextStore.java
    │   └── AnalyticsLogger.java
    ├── service/            # GRASP Indirection - External Service Layer
    │   ├── IExternalService.java
    │   ├── IPlacesService.java
    │   ├── ITransitService.java
    │   ├── IWeatherService.java
    │   └── IntegrationLayer.java
    └── domain/             # Value Objects & DTOs
        ├── UserID.java, SessionID.java
        ├── Preferences.java, Context.java
        ├── POI.java, POIList.java, Query.java
        ├── ETA.java, LatLng.java, RouteRequest.java
        └── Weather.java, Event.java, Report.java
```

---

## How to Compile

### Option 1: Compile All Files
```bash
# From project root directory
cd src
javac domain/*.java service/*.java model/*.java controller/*.java view/*.java
```

### Option 2: Compile with Output Directory
```bash
# From project root directory
mkdir -p bin
javac -d bin src/domain/*.java src/service/*.java src/model/*.java src/controller/*.java src/view/*.java
```

### Option 3: Clean and Recompile
```bash
# Remove all .class files and recompile
find src -name "*.class" -delete
cd src && javac domain/*.java service/*.java model/*.java controller/*.java view/*.java
```

**Note**: `.class` files are auto-generated during compilation and should NOT be committed to git (already excluded via `.gitignore`)

---

# Download MYSQL

Step-by-Step Download Instructions

  official MySQL Website

  1. Go to: https://dev.mysql.com/downloads/connector/j/
  2. Select Platform: "Platform Independent"
  3. Click Download for the ZIP Archive
  4. Click "No thanks, just start my download"
  5. Extract the ZIP → you'll get mysql-connector-j-9.5.0.jar

## Current Status (Lab-7 - Nov 3)

✅ **Completed:**
- Phase 1: Requirements Analysis (20 FRs, 10 NFRs, 7 Use Cases)
- Phase 2: System Design (Class Diagram with MVC + GRASP patterns, Sequence Diagrams)
- Package structure organized with proper MVC separation (view/, controller/, model/, service/, domain/)
- All domain objects created with real attributes and proper constructors/getters/setters
- All 28 classes compile successfully with no errors
- `.gitignore` configured to exclude .class files
- `Main.java` demo file created to demonstrate system flow
- `README.md` with compilation instructions and traceability guide

🔄 **In Progress:**
- Phase 3: System Implementation - Implementing TODO methods

---

## What We've Done So Far

### ✅ Phase 1 - Requirements Analysis (COMPLETE)
- Defined 20 Functional Requirements (atomic, traceable)
- Defined 10 Non-Functional Requirements (performance, security, accessibility)
- Created 7 Use Cases (UC-1 through UC-7)
- Built traceability matrix: FRs ↔ Use Cases ↔ NFRs

### ✅ Phase 2 - System Design (COMPLETE)
- **Class Diagram (Before Patterns)**: Initial structural design
- **Class Diagram (After Patterns)**: Applied MVC + GRASP (Creator, Information Expert, Low Coupling/High Cohesion, Indirection/Protected Variations)
- **Sequence Diagrams**: 7 diagrams for UC-1, UC-2, UC-4, UC-5, UC-6, UC-Admin
- **Traceability**: Complete mapping from Use Cases → FRs → Classes → Methods

### ✅ Phase 3 - System Implementation (IN PROGRESS)
**Completed:**
- ✅ Package structure organized (src/view, src/controller, src/model, src/service, src/domain)
- ✅ All 28 classes created with proper package declarations
- ✅ Domain objects have real attributes (not empty stubs)
  - `Preferences`: interests, budget, radius, transportMode, accessibilityNeeds
  - `POI`: id, name, location, category, rating, priceLevel, tags, openNow, address
  - `Context`: currentLocation, timestamp, currentWeather, timeOfDay
  - `Weather`, `ETA`, `LatLng`, `RouteRequest`, `Query`, etc. - all fully defined
- ✅ All classes compile successfully
- ✅ `Main.java` created to demonstrate UC-1 flow
- ✅ `.gitignore` excludes .class files

**Still To Do:**
- ⏳ Connect to local SQL database for persistence
- ⏳ Implement all TODO methods in classes
- ⏳ Create Java Swing UI to replace console interface
- ⏳ Implement mock service classes (MockPlacesService, MockTransitService, MockWeatherService)
- ⏳ Wire everything together for complete end-to-end flow

---

## Next Steps for Implementation Phase

### Week of Nov 3 (Lab-7) - **CURRENT** [5 Marks]
**Deliverable**: Implement System Objects based on Phase 2 Class Diagram

**Updated Priority Tasks:**

**Phase 3A: Database Integration**
1. ⏳ Set up local SQL database (MySQL/PostgreSQL/SQLite)
2. ⏳ Create database schema (tables for Profile, Session, POI, Event)
3. ⏳ Add JDBC dependency and database connection configuration
4. ⏳ Implement `ProfileContextStore` with SQL persistence (replace HashMap)
5. ⏳ Implement `AnalyticsLogger` with SQL persistence for events

**Phase 3B: Business Logic Implementation**
6. ⏳ Implement `ConversationEngine.startPlanning()` - main entry point for UC-1
7. ⏳ Implement `RecommendationEngine.rankPOIs()` - core ranking logic based on distance, rating, preferences
8. ⏳ Implement `RecommendationEngine.buildMicroItinerary()` - generate 2-4 stop itineraries
9. ⏳ Implement `RecommendationEngine.fetchCandidates()` - query integration layer for POIs
10. ⏳ Create mock service implementations:
    - `MockPlacesService` - return sample POI data
    - `MockTransitService` - return sample ETA data
    - `MockWeatherService` - return sample weather data
11. ⏳ Implement `IntegrationLayer` methods to delegate to services

**Phase 3C: UI Implementation (Java Swing)**
12. ⏳ Design Swing UI layout (main window, preference form, recommendation cards, itinerary view)
13. ⏳ Implement `MobileAppUI` with Swing components:
    - `capturePreferences()` - GUI form with JTextField, JComboBox
    - `renderCards()` - JPanel with card layout
    - `showSteps()` - JList/JTable for itinerary steps
    - `showMessage()` - JOptionPane for alerts
14. ⏳ Connect UI events to `ConversationEngine` (button clicks → method calls)
15. ⏳ Add location input (manual address entry with geocoding)

**Phase 3D: Testing & Integration**
16. ⏳ Wire all components together in `Main.java`
17. ⏳ Test complete flow: preferences → recommendations → itinerary
18. ⏳ Add error handling (rate limits, empty results, API failures)
19. ⏳ Prepare demo for TA assessment

**TA Assessment Focus:**
- Can you explain how GRASP Creator principle applies? (RecommendationEngine creates RecommendationCard/Itinerary)
- Can you explain Information Expert? (ProfileContextStore manages profiles, Itinerary calculates total ETA)
- Can you trace from FR to Class to Method? (e.g., FR-10 → RecommendationEngine.rankPOIs())
- Can you show the MVC separation in your code?
- How does your database design support persistence?

---

### Week of Nov 10 (Lab-8) [5 Marks]
**Deliverable**: Implement System UI Classes (Java Swing GUI)

**Tasks:**
1. **Complete Java Swing UI Implementation:**
   - Design main application window with proper layout
   - Preference form with text fields, combo boxes, sliders for all preference attributes
   - Recommendation cards display with images, ratings, distances
   - Itinerary view with step-by-step instructions and map links
   - Error/success message dialogs

2. **Implement all `MobileAppUI` methods with Swing:**
   - `capturePreferences()` - GUI form with validation
   - `renderCards()` - dynamic card generation with JPanel
   - `showSteps()` - formatted itinerary display
   - `showMessage()` - styled JOptionPane dialogs

3. **Event Handling & Flow:**
   - Wire button clicks to controller methods
   - Implement "Plan Now", "Show More", "Adjust Filters" buttons
   - Handle card selection and itinerary display
   - Add loading indicators for API calls

4. **Implement `AdminConsole` (optional/bonus):**
   - `addPOI()`, `tagPOI()`, `publishList()` with admin UI

5. **Integration:**
   - Connect Swing UI to `ConversationEngine`
   - Test complete flow with database persistence
   - Add error handling and user feedback

6. Submit complete code by **Friday Nov 14, 11:59pm**

---

### Week of Nov 17 (Lab-9) [5 Marks]
**Deliverable**: Write Test Plan & Unit Test Cases

**Tasks:**
1. Create `test/` package structure
2. Write at least 5 unit test cases:
   - Test `Preferences` creation and getter/setter
   - Test `POI` ranking logic
   - Test `Itinerary.computeTotalETA()`
   - Test `ProfileContextStore.saveProfile()` and `loadProfile()`
   - Test `AnalyticsLogger.detectRateLimit()`
3. Use JUnit or manual test driver classes
4. Document test results

---

### Week of Nov 24 (Lab-10) [10 Marks]
**Final Submission + Presentation**

**Tasks:**
1. Prepare complete SDLC documentation (Phases 1-4)
2. Create presentation slides (5-10 minutes)
3. Demo the working system
4. Submit final package: `CPS731_SecXX_Team20/`
5. **Deadline**: Friday Nov 28, 11:59pm

---

## Design Patterns Applied (Phase 2)

### MVC (Model-View-Controller)
- **View**: `MobileAppUI`, `AdminConsole` (user interaction)
- **Controller**: `ConversationEngine` (handles system events, orchestrates flow)
- **Model**: `RecommendationEngine`, `ProfileContextStore`, domain entities

### GRASP Principles

**1. Information Expert**
- `ProfileContextStore` manages profile/session persistence (knows the data)
- `Itinerary` computes total ETA (owns the steps/ETAs)
- `RecommendationEngine` ranks POIs (has the algorithm and context)

**2. Creator**
- `RecommendationEngine` creates `RecommendationCard` and `Itinerary` (has initialization data)

**3. Low Coupling / High Cohesion**
- UI doesn't know about geocoding/ranking internals
- Each class has focused responsibility
- Controller delegates instead of doing work

**4. Indirection / Protected Variations**
- Service interfaces (`IPlacesService`, `ITransitService`, `IWeatherService`) hide vendor APIs
- `IntegrationLayer` provides stable boundary
- Swapping API providers doesn't affect controller/model

---

## Functional Requirements Coverage

The implementation must cover these key FRs from Phase 1:

| FR | Requirement | Key Classes |
|----|-------------|-------------|
| FR-1 | Start conversation with chatbot | `MobileAppUI`, `ConversationEngine` |
| FR-2 | Collect travel preferences | `Preferences`, `ConversationEngine` |
| FR-7 | Integrate with location API | `IPlacesService`, `IntegrationLayer` |
| FR-8 | Integrate with weather API | `IWeatherService`, `IntegrationLayer` |
| FR-9 | Integrate with transportation API | `ITransitService`, `IntegrationLayer` |
| FR-10 | Rank recommendations | `RecommendationEngine.rankPOIs()` |
| FR-11 | Generate micro-itineraries | `RecommendationEngine.buildMicroItinerary()` |
| FR-12 | Display recommendation cards | `RecommendationCard`, `MobileAppUI.renderCards()` |
| FR-17 | Store/retrieve session history | `ProfileContextStore`, `Session` |
| FR-20 | Log interactions & analytics | `AnalyticsLogger` |

---

## Use Cases to Implement

**UC-1**: Plan something now (happy path)
**UC-2**: Adjust preferences
**UC-5**: View micro-itinerary details
**UC-6**: Handle rate limits

*(UC-4: Sign-in and UC-7: Admin curation are optional for bonus)*

---

## Traceability

All implementations should trace back to:
1. **Use Case** → **Functional Requirement** → **Class** → **Method**
2. Example: UC-1 → FR-10 → `RecommendationEngine` → `rankPOIs()`

Refer to Phase 2 traceability tables when implementing each method.

---

## Notes for TA Q&A

Be prepared to explain:
1. **Why did you put method X in class Y?** (Information Expert - class has the data)
2. **How does your code follow MVC?** (Show separation: UI, Controller, Model)
3. **How do you handle API changes?** (Indirection via service interfaces)
4. **Trace FR-10 to your code** (FR-10 → RecommendationEngine.rankPOIs())
5. **What GRASP principles did you apply?** (Creator, Information Expert, Low Coupling, Indirection)

---

## 📊 Project Summary & Progress Tracker

### What We've Accomplished ✅

**Week 1-2 (Sep 15-22):** Phase 1 Initial
- ✅ Team formed (Team 20: Hussein, Eric, Omar)
- ✅ Project topic selected: Travel Assistant System
- ✅ Software Project Management Plan (SPMP) with Gantt chart
- ✅ System domain and objectives defined

**Week 3-4 (Sep 29 - Oct 6):** Phase 1 Complete
- ✅ 20 Functional Requirements (atomic, traceable)
- ✅ 10 Non-Functional Requirements
- ✅ 7 Use Cases with goals/sub-goals
- ✅ Use Case diagrams, Scenario diagrams, Activity diagrams
- ✅ Traceability matrix: FRs ↔ UCs ↔ NFRs

**Week 5-6 (Oct 20-27):** Phase 2 Complete
- ✅ Class Diagram (before patterns)
- ✅ Class Diagram (after applying MVC + GRASP patterns)
- ✅ 7 Sequence Diagrams (UC-1, UC-2, UC-4, UC-5, UC-6, UC-Admin)
- ✅ Pattern justification (2 paragraphs)
- ✅ Complete traceability: FRs → Classes → Methods

**Week 7 (Nov 3):** Phase 3 Started - **CURRENT**
- ✅ Package structure organized (28 classes in proper packages)
- ✅ All domain objects with real attributes
- ✅ Compilation successful (no errors)
- ✅ `.gitignore` configured
- ✅ `Main.java` demo created
- ✅ `README.md` with comprehensive documentation

### What's Left To Do ⏳

**Week 7-8 (Nov 3-10):** Phase 3 Implementation
- ⏳ **Database Integration:**
  - Set up local SQL database (SQLite recommended for simplicity)
  - Create schema (Profile, Session, POI, Event tables)
  - Add JDBC driver
  - Implement `ProfileContextStore` with SQL persistence
  - Implement `AnalyticsLogger` with SQL persistence

- ⏳ **Business Logic:**
  - Implement all TODO methods in controller/model classes
  - Create mock service implementations (Places, Transit, Weather)
  - Implement ranking algorithm in `RecommendationEngine.rankPOIs()`
  - Implement itinerary generation in `buildMicroItinerary()`
  - Wire ConversationEngine to orchestrate flow

- ⏳ **Java Swing UI:**
  - Design main application window
  - Build preference input form (text fields, combos, sliders)
  - Build recommendation card display panel
  - Build itinerary view
  - Connect UI events to controller
  - Add loading indicators and error dialogs

**Week 9 (Nov 17):** Phase 4 Testing
- ⏳ Write test plan document
- ⏳ Create 5+ unit test cases
- ⏳ Test profile persistence
- ⏳ Test ranking algorithm
- ⏳ Test itinerary generation
- ⏳ Document test results

**Week 10 (Nov 24):** Final Submission & Presentation
- ⏳ Prepare complete SDLC documentation package
- ⏳ Create presentation slides
- ⏳ Prepare live demo
- ⏳ Submit by Nov 28, 11:59pm

### Current Implementation Status by Component

| Component | Status | Notes |
|-----------|--------|-------|
| **Domain Objects** | ✅ Complete | All 14 domain classes with attributes |
| **Service Interfaces** | ✅ Complete | IExternalService hierarchy defined |
| **ProfileContextStore** | ⏳ In Progress | Need SQL implementation |
| **AnalyticsLogger** | ⏳ In Progress | Need SQL implementation |
| **RecommendationEngine** | ⏳ TODO | Need rankPOIs(), fetchCandidates(), buildMicroItinerary() |
| **ConversationEngine** | ⏳ TODO | Need startPlanning(), adjustPreferences() |
| **IntegrationLayer** | ⏳ TODO | Need to implement delegations |
| **Mock Services** | ⏳ TODO | Need MockPlaces, MockTransit, MockWeather |
| **MobileAppUI (Swing)** | ⏳ TODO | Need full Swing implementation |
| **Main.java** | ✅ Created | Demo skeleton ready, needs wiring |

### Timeline & Deadlines

| Date | Deliverable | Status |
|------|-------------|--------|
| Nov 3 (Lab-7) | System Objects Implementation | 🔄 In Progress |
| Nov 14, 11:59pm | Complete Phase 3 Code | ⏳ Upcoming |
| Nov 17 (Lab-9) | Unit Test Cases | ⏳ Upcoming |
| Nov 28, 11:59pm | Final Package Submission | ⏳ Upcoming |
| Week of Nov 24 | Final Presentation | ⏳ Upcoming |

---

## Git Best Practices

```bash
# Check status
git status

# Add only source files (not .class)
git add src/
git add README.md

# Commit with clear message
git commit -m "feat: implement ProfileContextStore with HashMap storage"

# Push to remote
git push origin main
```

**Remember**: Never commit `.class` files - they're auto-generated!

---

## Contact

For questions or issues, contact any team member:
- Hussein Saab - hsaab@torontomu.ca
- Eric Mergelas - eric.mergelas@torontomu.ca
- Omar Ahmed - omar.ahmed1@torontomu.ca

---

**Last Updated: November 20, 2024**
**Current Phase: Phase 3 - System Implementation (Week of Nov 3)**
**Next Milestone: Database Integration + Business Logic + Swing UI (Due Nov 14)**
