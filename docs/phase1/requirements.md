# CPS731 Group 20 - Phase 1: Requirements Analysis

**Team Members:**
- Hussein Saab - 501178356
- Eric Mergelas - 500845404
- Omar Ahmed - 501178119

---

## 1. System Domain and Objectives

### System Domain
**Definition**: The problem space that your software system is intended to operate in and the problem it is addressing and sets the boundaries: what's in scope for the system to handle, and what's explicitly out of scope.

**Domain (Problem Space)**:
City-scale, same-day micro-planning for people in Toronto. The app converses with the user, captures quick preferences (cuisine/activity, budget, time window, mobility), and returns nearby suggestions with short summaries, hours, and step-by-step micro-itineraries (incl. walk/transit ETAs).

**Out of Scope**: flights/hotels, long-haul trips, bookings.

### Objectives
**Definition**: Functions that the system shall have to satisfy user requirements.

| ID | Objective | Measurable Goal |
|----|-----------|----------------|
| O1 | Enhance user travel experience | Reduce time and effort needed to plan via conversational chatbot with personalized recommendations |
| O2 | Quick decision making | Help users decide "what to do next" in ≤ 60 seconds from first message to first itinerary |
| O3 | Relevant recommendations | Provide at least 3 relevant options for typical campus-adjacent query with ≥ 80% user-rated relevance |
| O4 | Accurate time estimates | Walking/transit times with median absolute error ≤ 5 minutes for trips ≤ 45 minutes |
| O5 | Privacy protection | No sale of location history; store only coarse, consented preference/profile data |

---

## 2. Major Actors and Components

### External Actors

| Actor / External System | Type | Goals / Responsibilities | Key Interfaces |
|------------------------|------|--------------------------|----------------|
| **User** | Primary human actor | Ask for ideas "near me" now; set budget/time/mobility; pick an option; follow steps | Mobile UI (chat + cards), location permission |
| **Admin / Content curator** | Supporting human | Curate campus-adjacent lists, tag POIs, flag closures | Admin console |
| **Places API** | External system | Return nearby POIs with hours/ratings | REST / JSON |
| **Transit API (GTFS-realtime)** | External system | Give transit routes & ETAs | REST / JSON |
| **Maps/Geocoding** | External system | Geocoding, route geometry | REST / JSON |
| **Weather API** | External system | Context (rain/cold)—affect recommendations | REST / JSON |
| **Mobile OS Location Services** | External system | Current location (with consent) | OS permissions |

### Internal Components

| Internal Component | Responsibility | Stereotype |
|-------------------|----------------|------------|
| **Mobile App (UI)** | Chat UI, cards, map previews | «boundary» |
| **Conversation Engine** | Intent + slot filling (budget/time/mobility) | «control» |
| **Recommendation Engine** | Rank POIs; assemble 3-step micro-itinerary | «control» |
| **Context/Profile Store** | Minimal profile (consented), recency cache | «entity» |
| **Integration Layer** | Places/Transit/Maps/Weather adapters | «control» |
| **Analytics & Logging** | Anon usage metrics, errors | «entity» |

### Stereotype Definitions

- **«boundary»**: Anything at the edge of the system: UIs, APIs, input/output. Example: Mobile App (UI)
- **«control»**: Manages interactions between boundary and entities; contains the system's logic. Example: Conversation Engine, Recommendation Engine, Integration Layer
- **«entity»**: Persistent data or business objects. Stores information and is used by control classes. Example: Context/Profile Store, Analytics & Logging

---

## 3. Glossary

| Term | Definition |
|------|------------|
| **Chatbot** | Conversational AI system enabling natural language interaction with the user |
| **Contextual Memory** | A cache of user preferences and history for personalized suggestions |
| **Incremental Model** | A software development methodology where features are delivered in steps |
| **Location Services** | GPS-based system for identifying user position and nearby points of interest |
| **API** | Application Programming Interface, used to integrate with external travel services |
| **Travel Guide** | A system providing suggestions for activities, accommodation, and dining |
| **Micro-itinerary** | A concise, 2-4 step set of directions with ETAs |

---

## 4. Functional Requirements

**Definition**: Requirements that define what a system should do.

| Requirement ID | Functional Requirement |
|---------------|------------------------|
| FR-1 | The system shall allow users to start a conversation with the chatbot for travel planning |
| FR-2 | The chatbot shall collect travel preferences including location, activity type, and budget |
| FR-3 | The chatbot shall support editing and updating preferences mid-session |
| FR-4 | The chatbot shall maintain conversational context within a session |
| FR-5 | The chatbot shall store user sessions for returning users |
| FR-6 | The system shall retrieve user preferences from stored profiles |
| FR-7 | The system shall integrate with an external location API to fetch nearby places |
| FR-8 | The system shall integrate with a weather API to retrieve conditions for destinations |
| FR-9 | The system shall integrate with a transportation API to retrieve routes and travel times |
| FR-10 | The system shall compile and rank recommendations based on relevance and distance |
| FR-11 | The system shall generate micro-itineraries of 2–4 stops based on user preferences |
| FR-12 | The system shall provide clickable recommendation cards with name, rating, and distance |
| FR-13 | The system shall allow users to open cards to view detailed descriptions and directions |
| FR-14 | The system shall provide a "Show More" option to expand or adjust filters dynamically |
| FR-15 | The system shall provide "Show Similar" results for related experiences |
| FR-16 | The system shall handle cases with fewer than 3 results and display a suitable message |
| FR-17 | The system shall store and retrieve session history to allow reusing previous preferences |
| FR-18 | Admins shall be able to curate and tag Points of Interest (POIs) |
| FR-19 | Admins shall be able to publish and update curated lists for the app |
| FR-20 | The system shall log interactions and store analytics for personalization and system tuning |

### Functional Requirements by Actor

**Traveler / User (Primary Actor)**
- FR-1: Start a conversation with the chatbot
- FR-2: Provide travel preferences (location, activity type, budget)
- FR-3: Edit/update preferences mid-session
- FR-4: Maintain conversational context
- FR-5: Store sessions for returning users
- FR-6: Retrieve user preferences from stored profiles
- FR-11: Generate micro-itineraries of 2–4 stops
- FR-12: View clickable recommendation cards
- FR-13: View detailed card descriptions/directions
- FR-14: Show More (expand filters dynamically)
- FR-15: Show Similar (related results)
- FR-16: Handle fewer than 3 results with fallback message
- FR-17: Allow reusing previous preferences via session history

**Admin**
- FR-18: Curate and tag Points of Interest (POIs)
- FR-19: Publish and update curated lists

**External Systems / Integration Layer**
- FR-7: Integrate with external location API
- FR-8: Integrate with weather API
- FR-9: Integrate with transportation API
- FR-10: Compile & rank recommendations (depends on API data)

**System / Internal Services**
- FR-20: Log interactions & store analytics

---

## 5. Non-Functional Requirements

**Definition**: Requirements that define how a system shall perform.

| Requirement ID | Non-Functional Requirement |
|---------------|----------------------------|
| NFR-1 | Response time for chatbot queries shall not exceed 2 seconds |
| NFR-2 | The system shall produce the first set of 3 recommendations within 5 seconds (P95) |
| NFR-3 | Guest users may send up to 10 requests per minute; authenticated users up to 30 |
| NFR-4 | The system shall operate 99% uptime under normal server load |
| NFR-5 | The system shall encrypt stored user data and session histories |
| NFR-6 | The system shall conform to WCAG 2.2 AA accessibility standards |
| NFR-7 | The system shall display graceful fallback messages on API failure or no results |
| NFR-8 | The system shall support at least 10,000 concurrent sessions |
| NFR-9 | The system shall cache recent results to reduce repeated API calls by 25% |
| NFR-10 | The system shall prompt explicit consent for location tracking and allow opt-out |

---

## 6. System Assumptions

**Definition**: Statements that must be considered to be true for the system to function properly.

| Assumption ID | Assumption |
|--------------|------------|
| A-1 | Users have smartphones with internet connectivity |
| A-2 | External travel services provide accessible and reliable APIs |
| A-3 | Users will provide necessary permissions for GPS/location tracking |
| A-4 | Incremental development allows features to be added without major system redesigns |
| A-5 | Some users will create accounts for personalization; the system must support guest mode |
| A-6 | Users are familiar with using chat-based interfaces for apps |
| A-7 | APIs may occasionally fail; system will provide cached/fallback results |

---

## 7. List of Responsibilities

| RC ID | Component (stereotype) | Responsibilities | FR References |
|-------|------------------------|------------------|---------------|
| **RC-1** | Traveler (User) | RC-1.1: Provide preferences (location, activity type, budget)<br>RC-1.2: Update or edit preferences<br>RC-1.3: View results<br>RC-1.4: Request "Show More" or "Show Similar"; select a recommendation<br>RC-1.5: Navigate via map link | FR-1, FR-2, FR-3, FR-11, FR-12, FR-13, FR-14, FR-15, FR-16 |
| **RC-2** | Chatbot Interface | RC-2.1: Receive user input<br>RC-2.2: Parse and clarify ambiguous preferences<br>RC-2.3: Suggest follow-up questions<br>RC-2.4: Handle "Show More" and "Show Similar" intents | FR-1, FR-2, FR-3, FR-4, FR-14, FR-15, FR-16 |
| **RC-3** | Recommendation Engine | RC-3.1: Rank and filter POIs<br>RC-3.2: Generate micro-itineraries<br>RC-3.3: Cache and reuse previous results<br>RC-3.4: Return top-ranked cards | FR-10, FR-11, FR-12, FR-15, FR-16, FR-17 |
| **RC-4** | Integration Layer | RC-4.1: Connect to external APIs (maps, places, weather, transit)<br>RC-4.2: Handle API errors and fallbacks<br>RC-4.3: Merge data responses | FR-7, FR-8, FR-9, FR-10 |
| **RC-5** | Profile Context Store | RC-5.1: Save and load user profiles<br>RC-5.2: Store session preferences<br>RC-5.3: Reuse stored preferences on new sessions | FR-5, FR-6, FR-17 |
| **RC-6** | Admin | RC-6.1: Curate POIs<br>RC-6.2: Tag new destinations<br>RC-6.3: Approve or remove outdated entries<br>RC-6.4: Publish curated lists | FR-18, FR-19 |
| **RC-7** | Analytics Logger | RC-7.1: Track requests<br>RC-7.2: Detect rate-limit violations<br>RC-7.3: Log system errors and usage analytics | FR-20 |

---

## 8. System Use Cases

| UC ID | Goal (User Intent) | Sub-goals (system supports…) |
|-------|-------------------|------------------------------|
| **UC-1** | Plan something now | Capture/adjust prefs → Acquire location (GPS or manual) → Fetch POIs/ETAs/Weather → Rank → Show cards → View steps |
| **UC-2** | Adjust preferences | Set/modify budget, cuisine/activity, time window, mobility; reuse in session |
| **UC-3** | Use manual location | Enter address/landmark; geocode to coords; re-query |
| **UC-4** | Sign in / Personalize | Create account, login/logout; save/update prefs; auto-apply next session |
| **UC-5** | View micro-itinerary | Open a recommendation; show 2–4 steps with leg ETAs; open map link |
| **UC-6** | Handle rate limits | Detect over-limit; show message; allow retry later |
| **UC-7** (Admin) | Curate campus lists | Create/edit curated lists; tag POIs for featured surfacing |

---

## 9. Use Case Diagrams

### Actors
- **User** (primary)
- **Admin** (secondary)

### Use Cases
UC-1 through UC-7 (from table above)

### Relations
- User → UC-1, UC-2, UC-3, UC-4, UC-5, UC-6
- Admin → UC-7
- UC-1 «includes» View micro-itinerary (UC-5)
- UC-1 «includes» Adjust preferences (UC-2) when info missing
- UC-1 «extends» Use manual location (UC-3) if GPS denied

---

## 10. System Scenarios

### Scenario 1: UC-1 - Plan Something Now (Happy Path)

**Primary Actor**: Traveler
**Supporting Systems**: Chatbot, Places API, Maps/Transit API, Weather API, Location Services
**Preconditions**: App is loaded; network available
**Trigger**: User wants a quick plan now
**Success Guarantee**: User sees ≥3 relevant options and a micro-itinerary for a chosen option

#### Main Flow
1. Traveler (RC-1) opens chatbot and starts trip planning
2. Chatbot (RC-2) greets user and asks for location
3. Traveler (RC-1) provides current location or allows GPS
4. Chatbot (RC-2) asks for activity type
5. Traveler (RC-1) provides activity preference
6. Chatbot (RC-2) asks for budget or time window
7. Traveler (RC-1) provides budget
8. Chatbot (RC-2) summarizes preferences and asks confirmation
9. Traveler (RC-1) confirms or requests change
10. Integration Layer (RC-4) fetches data from APIs and returns top 3 recommendations (with ranking from RC-3)

#### Alternate Flow A: GPS Denied
- A1. Chatbot (RC-2) requests manual location
- A2. Traveler (RC-1) enters landmark
- A3. Integration Layer (RC-4) geocodes and continues

#### Alternate Flow B: Fewer than 3 Results
- B1. Chatbot (RC-2) shows available cards and a note
- B2. Recommendation Engine (RC-3) offers quick filter actions

---

### Scenario 2: UC-2 - Adjust Preferences

**Scope & Description**: Lets the user set or modify trip preferences such as budget, cuisine/activity, time window, and mobility.

**Primary Actor**: User
**Pre-condition**: Chat session active
**Trigger**: User enters "change budget" or similar

#### Main Steps
1. Traveler (RC-1) asks to change a preference
2. Chatbot (RC-2) displays current settings
3. Traveler (RC-1) provides new value
4. Profile Context Store (RC-5) updates session context

**Post-condition**: Updated preferences are stored in session memory

---

### Scenario 3: UC-3 - Use Manual Location

**Scope & Description**: Enables user to input a location when GPS access is denied.

**Primary Actor**: User
**Pre-condition**: Location permission denied or unavailable
**Trigger**: System prompts for manual location

#### Main Steps
1. Chatbot (RC-2) asks for location
2. Traveler (RC-1) types address or landmark
3. Integration Layer (RC-4) geocodes to coordinates
4. Recommendation Engine (RC-3) fetches nearby POIs

**Post-condition**: User's location established; results displayed

---

### Scenario 4: UC-4 - Sign In / Personalize

**Scope & Description**: Allows registered users to sign in and save preferences for personalized sessions.

**Primary Actor**: User
**Pre-condition**: App launched
**Trigger**: User selects "Sign In" or "Register"

#### Main Steps
1. Traveler (RC-1) enters email/password
2. Chatbot/Auth boundary (RC-2) validates credentials
3. Traveler (RC-1) updates profile/preferences
4. Profile Context Store (RC-5) saves data securely

**Post-condition**: Session personalized for that user

---

### Scenario 5: UC-5 - View Micro-Itinerary

**Scope & Description**: Shows a step-by-step route for a selected recommendation.

**Primary Actor**: User
**Pre-condition**: User has selected a recommendation card
**Trigger**: User clicks "View Steps"

#### Main Steps
1. Recommendation Engine (RC-3) retrieves steps and ETAs
2. Chatbot Interface (RC-2) renders steps and map link
3. Traveler (RC-1) opens map externally

**Post-condition**: User views itinerary details

---

### Scenario 6: UC-6 - Handle Rate Limits

**Scope & Description**: Prevents overuse of API requests.

**Primary Actor**: User
**Pre-condition**: User has sent multiple consecutive requests
**Trigger**: Request count exceeds threshold

#### Main Steps
1. Analytics Logger (RC-7) detects rate-limit breach
2. Chatbot (RC-2) displays rate-limit message
3. Traveler (RC-1) waits or upgrades
4. Analytics Logger (RC-7) resumes after cooldown

**Post-condition**: Fair API usage enforced

---

### Scenario 7: UC-7 - Curate Campus Lists (Admin)

**Scope & Description**: Admin manages curated lists of campus-adjacent POIs.

**Primary Actor**: Admin
**Pre-condition**: Admin authenticated
**Trigger**: Admin opens curation console

#### Main Steps
1. Admin (RC-6) adds/edits curated list
2. Profile/Content Store (RC-5) validates and stores
3. Admin (RC-6) tags POIs
4. Recommendation Engine (RC-3) surfaces updated content to users

**Post-condition**: Curated content refreshed in system cache

---

## 11. Activity Diagrams

*Note: Activity diagrams are visual representations created in Phase 1 submission. They model the behavioral flow of the scenarios above.*

---

## 12. Traceability Matrix

### Requirements to Use Cases

| Requirement | Use Case(s) | Component(s) |
|------------|-------------|--------------|
| FR-1 Chat flow | UC-1, UC-2 | Mobile App «boundary», Conversation Engine «control» |
| FR-2 Recommendations | UC-1 | Recommendation Engine «control», Integration Layer «control» |
| FR-3 Context memory | UC-1, UC-2, UC-4 | Conversation Engine «control», Profile/Context Store «entity» |
| FR-4 APIs (Places/Maps/Transit/Weather) | UC-1, UC-3 | Integration Layer «control» |
| FR-5 Location & fallback | UC-1, UC-3 | Mobile App «boundary», Integration Layer «control» |
| FR-6 Accounts | UC-4 | Mobile App «boundary», Profile/Context Store «entity» |
| FR-7 Cards & details | UC-1, UC-5 | Mobile App «boundary», Recommendation Engine «control» |
| FR-8 Rate limit | UC-6 | Analytics/Logging «entity», Mobile App «boundary» |
| NFR-1..NFR-6 | All | All (esp. Integration/Infra) |

---

**End of Phase 1 Requirements Analysis**

*This document captures all functional and non-functional requirements, use cases, scenarios, and traceability for the CPS731 Travel Assistant System.*
