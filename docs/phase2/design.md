# CPS731 Group 20 - Phase 2: System Design

**Team Members:**
- Hussein Saab - 501178356
- Eric Mergelas - 500845404
- Omar Ahmed - 501178119

---

## 1. Fixes Applied from Phase 1

| Change Category | Original Phase-1 (OG) | Updated Phase-1 (New) | Explanation of Change |
|----------------|----------------------|----------------------|----------------------|
| **Functional Requirements (FRs)** | Only 8 FRs defined, large/combined, multi-statement, not atomic | Expanded to 20 FRs, each smaller and more specific | Requirements were broken down properly to meet instructor feedback: atomic, clear, traceable |
| **Non-Functional Requirements (NFRs)** | Only 6 NFRs defined | Expanded to 10 NFRs | Added performance, availability, efficiency, accessibility, privacy, concurrency |
| **Responsibilities (generic)** | Responsibilities were described broadly and not tied cleanly to components | Changed responsibilities to be more specific, actionable, and mapped to sub-responsibilities | Improves traceability, aligns with UML stereotypes: boundary, control, entity |
| **Component granularity** | Responsibilities per component were high-level | Sub-numbered responsibilities under each component | Makes grading easier; matches typical use case responsibility breakdown structure |
| **System Scenarios** | Scenarios refer vaguely to "System" doing everything | Updated to reference proper actors/components using RC IDs | More consistent with UML requirements; respects boundary/control/entity roles in the main flow |
| **Actors (Scenario steps)** | OG scenario used generic "System" language | New version assigns actions to real actors | Enables proper mapping of responsibilities to components |
| **List of Responsibilities organization** | Responsibilities were listed as short bullets per component | Responsibilities now list sub-responsibilities per component and reference specific FRs | Shows richer decomposition required by rubric |
| **Traceability readiness** | Harder to map due to wide/combined requirements | Atomic FRs allow clean UC/component mapping | Corrects rubric complaint: "too many behaviors stuffed into one FR" |

---

## 2. System Structural Aspects (Class Diagram - Before Patterns)

### Requirements
- For each class, attributes should cover the functional requirements identified in Phase 1 (FRs for that component)
- For each class, methods should cover the tasks for relevant component identified in Phase 1 (steps in scenarios that the component is involved in)

### Feedback Addressed
- Drop arrows (use proper association lines)
- Add multiplicity (how many itineraries per UI, how many recommendation engines per mobile app)

*Note: Visual class diagram was submitted in Phase 2. See submitted diagrams for full UML notation.*

### Key Classes (Before Pattern Application)
- **MobileAppUI** (View)
- **ConversationEngine** (Controller)
- **RecommendationEngine** (Model/Control)
- **RecommendationCard** (Entity)
- **Itinerary** (Entity)
- **Profile** (Entity)
- **Session** (Entity)
- **ProfileContextStore** (Entity)
- **AnalyticsLogger** (Entity)
- **IntegrationLayer** (Service)
- **IExternalService** (Interface)
- **IPlacesService, ITransitService, IWeatherService** (Interfaces)
- **Domain objects**: Preferences, Context, POI, ETA, LatLng, Query, Weather, etc.

---

## 3. Design Patterns - Selected Patterns Rationale

### Pattern Selection Justification

We use **MVC** to separate concerns cleanly: the **View** (MobileAppUI) handles input/rendering, a single **Controller** (ConversationEngine) receives system events ("plan now," "adjust filters," "show more"), and the **Model** holds core logic/data (RecommendationEngine, Itinerary, RecommendationCard, Profile/Session). Within the model, **Information Expert** assigns behavior to the classes that own the data (e.g., Itinerary computes total ETA; ProfileContextStore loads/saves preferences), and **Creator** lets RecommendationEngine create RecommendationCard/Itinerary since it has the needed initialization info. This keeps the UI thin and the domain logic centralized and testable.

To keep the code base small but stable, we emphasize **Low Coupling and High Cohesion** (GRASP): UI code doesn't know ranking/geocoding details, the controller delegates instead of doing work, and the model classes stay focused (ranking vs. persistence vs. formatting). For external services (places/transit/weather), we introduce a tiny **Indirection / Protected Variations** layer via simple Java interfaces (IPlacesService, ITransitService, IWeatherService) and an IntegrationLayer that calls them. If a vendor API changes, we swap one implementation without touching the controller or model. This minimal set—**MVC + Information Expert + Creator + Low Coupling/High Cohesion + Indirection/Protected Variations**—is enough for a straightforward Java backend while keeping future changes safe and local.

### Patterns Applied Summary

| Pattern | Purpose | Implementation |
|---------|---------|----------------|
| **MVC (Model-View-Controller)** | Separate presentation, logic, and data | View: MobileAppUI, AdminConsole<br>Controller: ConversationEngine<br>Model: RecommendationEngine, ProfileContextStore, domain entities |
| **Information Expert (GRASP)** | Assign responsibility to the class with the information | ProfileContextStore manages profiles/sessions<br>Itinerary computes total ETA<br>RecommendationEngine ranks POIs |
| **Creator (GRASP)** | Assign creation responsibility to the class that has initialization data | RecommendationEngine creates RecommendationCard and Itinerary |
| **Low Coupling (GRASP)** | Minimize dependencies between classes | UI doesn't know about ranking/geocoding<br>Controller delegates to model<br>Service layer isolates external APIs |
| **High Cohesion (GRASP)** | Keep related responsibilities together | Each class has focused, single purpose |
| **Indirection / Protected Variations (GRASP)** | Protect system from external changes | Service interfaces (IPlacesService, ITransitService, IWeatherService)<br>IntegrationLayer provides stable boundary |

---

## 4. Updated Class Diagram (After Applying Patterns)

*Note: Visual class diagram with proper UML notation (associations, multiplicities, stereotypes) was submitted in Phase 2.*

### Key Architectural Decisions

**MVC Structure:**
- **View Layer**: MobileAppUI (1) -- uses --> (1) ConversationEngine
- **Controller Layer**: ConversationEngine (1) -- controls --> (1) RecommendationEngine
- **Model Layer**: All business entities and logic

**Multiplicities (Examples):**
- MobileAppUI (1) -- displays --> (0..*) RecommendationCard
- MobileAppUI (1) -- shows --> (0..1) Itinerary
- RecommendationEngine (1) -- creates --> (0..*) RecommendationCard
- RecommendationEngine (1) -- builds --> (0..1) Itinerary
- ProfileContextStore (1) -- aggregates --> (0..*) Profile
- ProfileContextStore (1) -- composes --> (0..*) Session

**Stereotypes Applied:**
- «view»: MobileAppUI, AdminConsole
- «controller»: ConversationEngine
- «entity»: RecommendationEngine, RecommendationCard, Itinerary, Profile, Session, ProfileContextStore, AnalyticsLogger
- «service/indirection»: IntegrationLayer, IExternalService interfaces

---

## 5. Sequence Diagrams

### UC-1: "Plan Something Now" (Happy Path)

**Actors**: User, MobileAppUI, ConversationEngine, RecommendationEngine, IntegrationLayer, External APIs

**Flow**:
1. User → MobileAppUI: startPlanning()
2. MobileAppUI → ConversationEngine: capturePreferences()
3. ConversationEngine → User: requestLocation()
4. User → ConversationEngine: provideLocation(coords)
5. ConversationEngine → IntegrationLayer: getWeather(coords)
6. IntegrationLayer → WeatherAPI: fetch()
7. ConversationEngine → RecommendationEngine: fetchCandidates(query)
8. RecommendationEngine → IntegrationLayer: getNearbyPlaces(coords, prefs)
9. IntegrationLayer → PlacesAPI: search()
10. RecommendationEngine → RecommendationEngine: rankPOIs(prefs, context)
11. RecommendationEngine → ConversationEngine: return cards
12. ConversationEngine → MobileAppUI: renderCards(cards)
13. MobileAppUI → User: display recommendations

*Note: Full sequence diagrams with proper UML notation submitted in Phase 2.*

---

### UC-1 (Alternate): GPS Denied → Manual Location (Geocode)

**Additional Steps**:
- If GPS denied, MobileAppUI prompts for manual address
- ConversationEngine → IntegrationLayer: geocode(address)
- IntegrationLayer → MapsAPI: geocodeAddress()
- Returns LatLng coordinates, continues with main flow

---

### UC-5: "View Micro-Itinerary Details" (On Card Select)

**Flow**:
1. User → MobileAppUI: selectCard(cardId)
2. MobileAppUI → ConversationEngine: handleSelectCard(cardId)
3. ConversationEngine → RecommendationEngine: buildMicroItinerary(prefs, card)
4. RecommendationEngine → IntegrationLayer: getETAs(routeRequest)
5. IntegrationLayer → TransitAPI: getTransitETAs()
6. RecommendationEngine: creates Itinerary with 2-4 steps
7. RecommendationEngine → ConversationEngine: return itinerary
8. ConversationEngine → MobileAppUI: showSteps(itinerary)
9. MobileAppUI → User: display step-by-step directions

---

### UC-2: "Show More / Adjust Filters" (Iteration Loop)

**Flow**:
1. User → MobileAppUI: adjustPreferences(delta)
2. MobileAppUI → ConversationEngine: adjustPreferences(delta)
3. ConversationEngine → ProfileContextStore: updateSessionPreferences()
4. ConversationEngine → RecommendationEngine: rankPOIs(updatedPrefs, context)
5. RecommendationEngine → ConversationEngine: return updated cards
6. ConversationEngine → MobileAppUI: renderCards(cards)
7. MobileAppUI → User: display updated recommendations

---

### UC-6: "Rate Limit Handling" (Observer Pattern)

**Flow**:
1. User → MobileAppUI: multiple rapid requests
2. MobileAppUI → ConversationEngine: startPlanning() [multiple times]
3. ConversationEngine → AnalyticsLogger: logInteraction(event)
4. AnalyticsLogger: detectRateLimit(userId) returns true
5. ConversationEngine → MobileAppUI: showMessage("Rate limit exceeded")
6. MobileAppUI → User: display rate limit message

---

### UC-4: "Sign-in and Load Profile/Session" (Repository Pattern)

**Flow**:
1. User → MobileAppUI: signIn(credentials)
2. MobileAppUI → ConversationEngine: loadProfile(userId)
3. ConversationEngine → ProfileContextStore: loadProfile(userId)
4. ProfileContextStore: queries internal storage
5. ProfileContextStore → ConversationEngine: return profile
6. ConversationEngine → ProfileContextStore: loadSession(sessionId)
7. ProfileContextStore → ConversationEngine: return session
8. ConversationEngine → MobileAppUI: profileLoaded()
9. MobileAppUI → User: show personalized preferences

---

### UC-Admin: "Curate POI List and Publish" (Admin Console)

**Flow**:
1. Admin → AdminConsole: addPOI(poi)
2. AdminConsole → ProfileContextStore: savePOI(poi)
3. Admin → AdminConsole: tagPOI(poiId, tag)
4. AdminConsole → ProfileContextStore: updatePOITags()
5. Admin → AdminConsole: publishList(listId)
6. AdminConsole → ProfileContextStore: publishList()
7. ProfileContextStore → RecommendationEngine: refresh cache
8. AdminConsole → Admin: confirmation message

---

## 6. Complete Traceability (Phase 2 Update)

### Non-Functional Requirements → Functional Requirements

| NFR ID | Non-Functional Requirement | FR Links |
|--------|----------------------------|----------|
| NFR-1 | Response time for chatbot queries shall not exceed 2 seconds | FR-1, FR-2, FR-3, FR-4 |
| NFR-2 | The system shall produce the first set of 3 recommendations within 5 seconds (P95) | FR-7, FR-8, FR-9, FR-10, FR-11 |
| NFR-3 | Guest users may send up to 10 requests per minute; authenticated users up to 30 | FR-1, FR-2, FR-12, FR-14, FR-15 |
| NFR-4 | The system shall operate 99% uptime under normal server load | All FRs |
| NFR-5 | The system shall encrypt stored user data and session histories | FR-5, FR-6, FR-17, FR-18, FR-19 |
| NFR-6 | The system shall conform to WCAG 2.2 AA accessibility standards | FR-12, FR-13, FR-14, FR-15, FR-16 |
| NFR-7 | The system shall display graceful fallback messages on API failure or no results | FR-7, FR-8, FR-9, FR-16 |
| NFR-8 | The system shall support at least 10,000 concurrent sessions | FR-1 to FR-17 |
| NFR-9 | The system shall cache recent results to reduce repeated API calls by 25% | FR-7, FR-8, FR-9, FR-10, FR-15 |
| NFR-10 | The system shall prompt explicit consent for location tracking and allow opt-out | FR-2, FR-6, FR-14, FR-17 |

---

### Functional Requirements → Use Cases

| Requirement ID | Functional Requirement | Use Cases Supported |
|---------------|------------------------|---------------------|
| FR-1 | The system shall allow users to start a conversation with the chatbot for travel planning | UC-1 |
| FR-2 | The chatbot shall collect travel preferences including location, activity type, and budget | UC-1, UC-2 |
| FR-3 | The chatbot shall support editing and updating preferences mid-session | UC-2 |
| FR-4 | The chatbot shall maintain conversational context within a session | UC-1, UC-2 |
| FR-5 | The chatbot shall store user sessions for returning users | UC-1, UC-4 |
| FR-6 | The system shall retrieve user preferences from stored profiles | UC-1, UC-4 |
| FR-7 | The system shall integrate with an external location API to fetch nearby places | UC-1 |
| FR-8 | The system shall integrate with a weather API to retrieve conditions for destinations | UC-1 |
| FR-9 | The system shall integrate with a transportation API to retrieve routes and travel times | UC-1, UC-5 |
| FR-10 | The system shall compile and rank recommendations based on relevance and distance | UC-1 |
| FR-11 | The system shall generate micro-itineraries of 2–4 stops based on user preferences | UC-1, UC-5 |
| FR-12 | The system shall provide clickable recommendation cards with name, rating, and distance | UC-1, UC-5 |
| FR-13 | The system shall allow users to open cards to view detailed descriptions and directions | UC-5 |
| FR-14 | The system shall provide a "Show More" option to expand or adjust filters dynamically | UC-1, UC-2 |
| FR-15 | The system shall provide "Show Similar" results for related experiences | UC-1 |
| FR-16 | The system shall handle cases with fewer than 3 results and display a suitable message | UC-1 |
| FR-17 | The system shall store and retrieve session history to allow reusing previous preferences | UC-1, UC-4 |
| FR-18 | Admins shall be able to curate and tag Points of Interest (POIs) | UC-7 |
| FR-19 | Admins shall be able to publish and update curated lists for the app | UC-7 |
| FR-20 | The system shall log interactions and store analytics for personalization and system tuning | UC-1, UC-6, UC-7 |

---

### Use Cases Summary

| UC ID | Goal (User Intent) | Sub-goals (system supports…) | Supported FRs |
|-------|-------------------|------------------------------|---------------|
| UC-1 | Plan something now | Capture/adjust prefs → Acquire location (GPS or manual) → Fetch POIs/ETAs/Weather → Rank → Show cards → View steps | FR-1, FR-2, FR-4, FR-6, FR-7, FR-8, FR-9, FR-10, FR-11, FR-12, FR-14, FR-15, FR-16, FR-17, FR-20 |
| UC-2 | Adjust preferences | Set/modify budget, cuisine/activity, time window, mobility; reuse in session | FR-2, FR-3, FR-4, FR-14 |
| UC-3 | Use manual location | Enter address/landmark; geocode to coords; re-query | FR-2, FR-7 |
| UC-4 | Sign in / Personalize | Create account, login/logout; save/update prefs; auto-apply next session | FR-5, FR-6, FR-17 |
| UC-5 | View micro-itinerary | Open a recommendation; show 2–4 steps with leg ETAs; open map link | FR-9, FR-11, FR-12, FR-13 |
| UC-6 | Handle rate limits | Detect over-limit; show message; allow retry later | FR-20 |
| UC-7 (Admin) | Curate campus lists | Create/edit curated lists; tag POIs for featured surfacing | FR-18, FR-19, FR-20 |

---

### Complete Traceability: FR → UC → Class → Method

| FR | Use Case | Class | Method | GRASP Principle |
|----|----------|-------|--------|----------------|
| FR-1 | UC-1 | MobileAppUI | capturePreferences() | Boundary |
| FR-2 | UC-1, UC-2 | Preferences | getInterests(), getBudget() | Information Expert |
| FR-5 | UC-1, UC-4 | ProfileContextStore | saveSession(session) | Information Expert |
| FR-6 | UC-1, UC-4 | ProfileContextStore | loadProfile(userId) | Information Expert |
| FR-10 | UC-1 | RecommendationEngine | rankPOIs(prefs, ctx) | Information Expert |
| FR-11 | UC-1, UC-5 | RecommendationEngine | buildMicroItinerary(prefs, card) | Creator |
| FR-12 | UC-1 | MobileAppUI | renderCards(cards) | Boundary |
| FR-13 | UC-5 | MobileAppUI | showSteps(itinerary) | Boundary |
| FR-17 | UC-4 | Session | getSessionId() | Entity |
| FR-20 | UC-6 | AnalyticsLogger | logInteraction(event) | Information Expert |

---

## 7. Class Responsibilities (GRASP Mapping)

### Information Expert
- **ProfileContextStore**: Manages profile/session data (has the storage map)
- **Itinerary**: Computes total ETA (owns the steps/ETA data)
- **RecommendationEngine**: Ranks POIs (has ranking algorithm and context)

### Creator
- **RecommendationEngine**: Creates RecommendationCard and Itinerary (has initialization data from POIs and ETAs)

### Controller
- **ConversationEngine**: Orchestrates system events, delegates to model

### Low Coupling
- UI layer doesn't know about ranking algorithms
- Controller delegates to model/services
- Service interfaces isolate external dependencies

### High Cohesion
- Each class has focused responsibility
- RecommendationEngine: ranking/itinerary generation only
- ProfileContextStore: persistence only
- MobileAppUI: presentation only

### Indirection / Protected Variations
- **IntegrationLayer**: Shields system from external API changes
- **Service Interfaces** (IPlacesService, ITransitService, IWeatherService): Stable boundary for vendor APIs

---

**End of Phase 2 System Design**

*This document captures the complete architectural design, patterns application, sequence diagrams, and traceability for the CPS731 Travel Assistant System.*
