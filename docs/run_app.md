# Running the Travel Assistant Application

**CPS731 Phase 3 - Full System Implementation**

---

## Prerequisites

### 1. Download MySQL and Create Tables

Before running the application, you must set up the MySQL database.

**Follow the instructions in [mysql.md](mysql.md)** to:
- Install MySQL via Homebrew
- Start the MySQL service
- Create the `travel_assistant_db` database
- Run `schema.sql` and `schema_pois.sql` to create tables and insert sample data

**Quick Setup Commands:**
```bash
# Install and start MySQL
brew install mysql
brew services start mysql

# Create database and tables
mysql -u root -e "CREATE DATABASE travel_assistant_db;"
mysql -u root travel_assistant_db < docs/schema.sql
mysql -u root travel_assistant_db < docs/schema_pois.sql
```

### 2. Verify MySQL is Running

```bash
brew services list | grep mysql
```

You should see `mysql started` in the output.

---

## How to Run

### Method 1: Quick Run (if code compiled)

```bash
cd ~/Documents/GitHub/TravelAssistant

# Run the application
java -cp "lib/mysql-connector-j-9.5.0.jar:bin" Main
```

### Method 2: Compile and Run

If you made changes:

```bash
cd ~/Documents/GitHub/TravelAssistant

# Compile everything
javac -cp "lib/mysql-connector-j-9.5.0.jar:src" \
  -d bin \
  src/domain/*.java \
  src/service/*.java \
  src/model/*.java \
  src/controller/*.java \
  src/view/*.java \
  src/*.java

# Run the application
java -cp "lib/mysql-connector-j-9.5.0.jar:bin" Main
```

---

## 🎮 Using the Application

### Step 1: Enter Preferences
- **Interests**: Comma-separated list (e.g., "restaurants, museums, parks")
- **Budget**: Select "low", "medium", or "high"
- **Radius**: Search radius in meters (default: 5000)
- **Transport Mode**: "walking", "driving", or "transit"

### Step 2: Start Planning
- Click **"Start Planning"** button
- Console will show system activity (services initializing, POIs fetching, ranking)
- UI will display ranked recommendation cards

### Step 3: View Recommendations
Each card shows:
- **Name** - POI name (e.g., "Sushi Haven")
- **Summary** - Category, price level, tags (e.g., "Restaurant • $$$ • japanese, sushi")
- **Rating** - Star rating (e.g., "⭐ 4.7")
- **Distance** - Distance from you (e.g., "📍 1250m away")

### Step 4: View Itinerary
- Click **"View Itinerary"** on any card
- Dialog shows micro-itinerary with steps and total ETA

### Step 5: Adjust Preferences (Optional)
- Change preferences in the left panel
- Click **"Start Planning"** again to get new recommendations

---

## 📊 Sample Test Cases

### Test Case 1: Restaurant Search
```
Interests: restaurants
Budget: medium
Radius: 3000
Transport: walking

Expected Results:
- Pasta Palace (⭐ 4.5, $$ italian)
- Sushi Haven (⭐ 4.7, $$$ japanese)
- Burger Barn (⭐ 4.2, $ american)
```

### Test Case 2: Museum Search
```
Interests: museums
Budget: medium
Radius: 5000
Transport: transit

Expected Results:
- Royal Ontario Museum (⭐ 4.8, $$ history)
- Art Gallery of Ontario (⭐ 4.7, $$ art)
```

### Test Case 3: Mixed Interests
```
Interests: restaurants, cafes, parks
Budget: low
Radius: 4000
Transport: walking

Expected Results:
- Mix of restaurants, cafes, and parks
- Ranked by rating and distance
```

---

## 🏗️ Architecture Overview

```
Main.java
  ├── Initializes MockPlacesService, MockTransitService, MockWeatherService
  ├── Wires IntegrationLayer
  ├── Initializes ProfileContextStore (MySQL)
  ├── Initializes RecommendationEngine
  ├── Initializes ConversationEngine
  └── Launches MobileAppUI

User clicks "Start Planning"
  └── MobileAppUI.handleStartPlanning()
      └── ConversationEngine.startPlanning(prefs)
          ├── Initialize Context (location, weather, time)
          └── RecommendationEngine.rankPOIs(prefs, ctx)
              ├── fetchCandidates(query)
              │   └── IntegrationLayer.getNearbyPlaces()
              │       └── MockPlacesService.searchPlaces()
              └── Rank by score (rating + distance + budget)

User clicks "View Itinerary"
  └── MobileAppUI.handleCardClick(card)
      └── ConversationEngine.handleSelectCard(cardId)
          └── RecommendationEngine.buildMicroItinerary(prefs, card)
```

---

## 📝 Console Output

When running the app, you'll see detailed console output showing:

1. **Initialization**:
   ```
   ✓ MockPlacesService initialized with 15 POIs
   ✓ MockTransitService initialized
   ✓ MockWeatherService initialized
   ✓ IntegrationLayer initialized
   ✓ ProfileContextStore initialized with MySQL persistence
   ✓ Database connection verified
   ✓ RecommendationEngine initialized
   ✓ ConversationEngine initialized
   ✅ UI launched successfully!
   ```

2. **Planning Session**:
   ```
   ======================================================================
      STARTING PLANNING SESSION
   ======================================================================

   [ConversationEngine] Initializing session context...
     Location: LatLng{43.6532, -79.3832}
     Weather: 22.0°C, sunny
     Time of day: afternoon

   [ConversationEngine] Getting recommendations...
   Searching for POIs near LatLng{43.6532, -79.3832}
     Interests: [restaurants, museums]
     Radius: 5000m
     Found 6 matching POIs

   [RecommendationEngine] Ranking POIs...
     Ranked 6 cards

   ✅ Planning session started!
     Found 6 recommendations
   ======================================================================
   ```

3. **Itinerary Generation**:
   ```
   [ConversationEngine] Card selected: poi_002

   📍 MICRO-ITINERARY:
   ----------------------------------------------------------------------
   1. Visit Sushi Haven (main destination)
      Rating: 4.7/5.0
      Distance: 1250m from you

   2. Explore the area (30 min suggested)

   Total estimated time: 1-2 hours
   ----------------------------------------------------------------------
   ```

---

## 🎯 Key Features Demonstrated

### GRASP Patterns
- **Controller**: ConversationEngine coordinates the flow
- **Information Expert**: RecommendationEngine knows how to rank POIs
- **Indirection**: IntegrationLayer isolates external service dependencies
- **Protected Variations**: Mock services allow testing without real APIs

### MVC Architecture
- **Model**: RecommendationEngine, ProfileContextStore, AnalyticsLogger
- **View**: MobileAppUI (Java Swing)
- **Controller**: ConversationEngine

### Functional Requirements
- ✅ FR-1: Generate personalized recommendations
- ✅ FR-2: Rank POIs based on preferences
- ✅ FR-3: Build micro-itineraries
- ✅ FR-4: Allow preference adjustments
- ✅ FR-5: Store user sessions
- ✅ FR-6: Retrieve user preferences

### Use Cases
- ✅ UC-1: Discover Places (main flow + itinerary)
- ✅ UC-2: Refine Results (preference adjustment)
- ✅ UC-4: Sign in / Personalize (profile load/save)

---

## 🔧 Troubleshooting

### Error: "ClassNotFoundException: com.mysql.cj.jdbc.Driver"
**Solution**: Include JDBC driver in classpath
```bash
java -cp "lib/mysql-connector-j-9.5.0.jar:bin" Main
```

### Error: "ConversationEngine not initialized"
**Solution**: Make sure Main.java properly wires all components (already done)

### No recommendations showing
**Solution**: Check console for errors. Make sure:
- Interests match POI categories ("restaurants" not "food")
- Radius is large enough (try 5000m)

---

**Last Updated**: November 25, 2024
