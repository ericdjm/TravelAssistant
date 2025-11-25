# CPS731 Travel Assistant - Project Documentation

**Team 20**
- Hussein Saab - 501178356
- Eric Mergelas - 500845404
- Omar Ahmed - 501178119

---

## 📁 Documentation Structure

This folder contains all project documentation from Phases 1-4 of the Software Development Life Cycle (SDLC).

```
docs/
├── README.md           # This file
├── phase1/             # Requirements Analysis
│   └── requirements.md
├── phase2/             # System Design
│   └── design.md
└── phase3/             # (Future) Implementation notes
    └── implementation_notes.md
```

---

## 📋 Phase Documents

### Phase 1: Requirements Analysis
**File**: `phase1/requirements.md`

**Contents**:
- System Domain and Objectives
- Major Actors and Components
- Glossary
- 20 Functional Requirements (FR-1 to FR-20)
- 10 Non-Functional Requirements (NFR-1 to NFR-10)
- System Assumptions
- List of Responsibilities (RC-1 to RC-7)
- 7 Use Cases (UC-1 to UC-7)
- System Scenarios (detailed flows)
- Traceability Matrix

**Key Deliverables**:
- ✅ 20 atomic, traceable FRs
- ✅ 10 NFRs covering performance, security, accessibility
- ✅ Complete use case analysis
- ✅ Scenario-based requirements
- ✅ Full traceability: FRs ↔ UCs ↔ Components

---

### Phase 2: System Design
**File**: `phase2/design.md`

**Contents**:
- Fixes Applied from Phase 1 Feedback
- Class Diagram (Before Patterns)
- Design Patterns Rationale (MVC + GRASP)
- Updated Class Diagram (After Patterns)
- 7 Sequence Diagrams:
  - UC-1: Plan something now (happy path)
  - UC-1 alt: GPS denied → manual location
  - UC-5: View micro-itinerary
  - UC-2: Show more / adjust filters
  - UC-6: Rate limit handling
  - UC-4: Sign-in and load profile
  - UC-7 Admin: Curate POI lists
- Complete Traceability (NFRs → FRs → UCs → Classes → Methods)
- GRASP Principles Mapping

**Key Deliverables**:
- ✅ MVC architecture with proper separation
- ✅ GRASP patterns: Information Expert, Creator, Low Coupling, High Cohesion, Indirection
- ✅ Service layer for external API isolation
- ✅ Complete sequence diagrams for all use cases
- ✅ Full traceability matrix updated

---

### Phase 3: System Implementation
**Status**: In Progress (Week of Nov 3)

**Current Activities**:
- Package structure organized (view/, controller/, model/, service/, domain/)
- All 28 classes created with proper attributes
- SQLite database schema design
- Business logic implementation
- Java Swing UI development

**Deliverable**: Complete working code (Due Nov 14)

---

### Phase 4: System Testing
**Status**: Upcoming (Week of Nov 17)

**Planned Activities**:
- Unit test cases (minimum 5)
- Test plan documentation
- Test results reporting

**Deliverable**: Test cases with results (Due Nov 17)

---

## 🎯 How to Use These Documents

### For Team Members
- **Implementing features?** → Check `phase2/design.md` for class responsibilities and sequence diagrams
- **Writing tests?** → Check `phase1/requirements.md` for FRs to test, then `phase2/design.md` for class→method mapping
- **Need traceability?** → Both phases have complete traceability tables

### For TA Assessment
- **"Trace FR-10 to your code"** → `phase2/design.md` shows FR-10 → UC-1 → RecommendationEngine → rankPOIs()
- **"Explain GRASP principles"** → `phase2/design.md` Section 7 has complete GRASP mapping
- **"Show requirements coverage"** → `phase1/requirements.md` has all 20 FRs with traceability

### For Final Submission
All documents in this folder will be included in the final `CPS731_SecXX_Team20/` package.

---

## 📊 Quick Reference

### Key System Objectives
- **O2**: Help users decide "what to do next" in ≤ 60 seconds
- **O3**: Provide ≥3 relevant options with ≥80% user-rated relevance
- **O4**: Estimate ETAs with ≤5 minute median error

### Core Use Cases
1. **UC-1**: Plan something now (main flow)
2. **UC-2**: Adjust preferences
3. **UC-5**: View micro-itinerary
4. **UC-6**: Handle rate limits

### Design Patterns Applied
- **MVC**: View (MobileAppUI), Controller (ConversationEngine), Model (RecommendationEngine, entities)
- **Information Expert**: ProfileContextStore, Itinerary, RecommendationEngine
- **Creator**: RecommendationEngine creates RecommendationCard & Itinerary
- **Indirection**: IntegrationLayer + service interfaces

### Key Classes
- **View**: MobileAppUI, AdminConsole
- **Controller**: ConversationEngine
- **Model**: RecommendationEngine, ProfileContextStore, AnalyticsLogger
- **Service**: IntegrationLayer, IPlacesService, ITransitService, IWeatherService
- **Domain**: Preferences, Context, POI, Session, Profile, etc.

---

## 📝 Document Updates

| Date | Phase | Update |
|------|-------|--------|
| Oct 6, 2024 | Phase 1 | Initial requirements submission |
| Oct 27, 2024 | Phase 2 | Design patterns and traceability |
| Nov 20, 2024 | Phase 3 | Documentation organized into markdown |

---

**For questions or clarifications**, contact any team member:
- Hussein Saab - hsaab@torontomu.ca
- Eric Mergelas - eric.mergelas@torontomu.ca
- Omar Ahmed - omar.ahmed1@torontomu.ca
