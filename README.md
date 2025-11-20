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

## Current Status (Lab-7 - Nov 3)

✅ **Completed:**
- Phase 1: Requirements Analysis (20 FRs, 10 NFRs, 7 Use Cases)
- Phase 2: System Design (Class Diagram with MVC + GRASP patterns, Sequence Diagrams)
- Package structure organized with proper MVC separation
- All domain objects created with attributes and methods
- All classes compile successfully

🔄 **In Progress:**
- Phase 3: System Implementation - Implementing TODO methods

---

## Next Steps for Implementation Phase

### Week of Nov 3 (Lab-7) - **CURRENT** [5 Marks]
**Deliverable**: Implement System Objects based on Phase 2 Class Diagram

**Priority Tasks:**
1. ✅ Domain objects with real attributes (DONE)
2. ⏳ Implement `ProfileContextStore` with HashMap-based in-memory storage
3. ⏳ Implement `ConversationEngine.startPlanning()` - main entry point for UC-1
4. ⏳ Implement `RecommendationEngine.rankPOIs()` - core ranking logic
5. ⏳ Implement `RecommendationEngine.buildMicroItinerary()` - generate 2-4 stop itineraries
6. ⏳ Implement `AnalyticsLogger` - event tracking and rate limit detection
7. ⏳ Create mock implementations of service interfaces (IPlacesService, ITransitService, IWeatherService)
8. ⏳ Implement `IntegrationLayer` to delegate to services
9. ⏳ Create a `Main.java` demo class to show system flow

**TA Assessment Focus:**
- Can you explain how GRASP Creator principle applies? (RecommendationEngine creates RecommendationCard/Itinerary)
- Can you explain Information Expert? (ProfileContextStore manages profiles, Itinerary calculates total ETA)
- Can you trace from FR to Class to Method?
- Can you show the MVC separation in your code?

---

### Week of Nov 10 (Lab-8) [5 Marks]
**Deliverable**: Implement System UI Classes

**Tasks:**
1. Implement `MobileAppUI` methods:
   - `capturePreferences()` - console/scanner input
   - `renderCards()` - display recommendation cards
   - `showSteps()` - display itinerary
   - `showMessage()` - error/info messages
2. Implement `AdminConsole` methods (optional/bonus):
   - `addPOI()`, `tagPOI()`, `publishList()`
3. Wire everything together in `Main.java` with a complete flow
4. Submit complete code by **Friday Nov 14, 11:59pm**

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

*Last Updated: November 20, 2024*
*Phase 3 - System Implementation*
