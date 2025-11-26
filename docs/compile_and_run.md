# Compilation and Execution Guide

**CPS731 Travel Assistant - Phase 3**

---

## 📦 Project Structure

```
TravelAssistant/
├── lib/
│   └── mysql-connector-j-9.5.0.jar  ← MySQL JDBC Driver
├── src/
│   ├── view/
│   ├── controller/
│   ├── model/
│   ├── service/
│   │   └── DatabaseConnection.java  ← NEW
│   ├── domain/
│   ├── Main.java
│   └── TestDatabaseConnection.java  ← NEW
└── docs/
```

---

## 🛠️ Compiling with JDBC Driver

### Important: Include JDBC Driver in Classpath

Since we're using MySQL, we must include the JDBC driver JAR in the classpath when compiling and running.

### Option 1: Compile All Files (Recommended)

```bash
# Navigate to project root
cd ~/Documents/GitHub/TravelAssistant

# Compile all source files with JDBC driver in classpath
javac -cp "lib/mysql-connector-j-9.5.0.jar:src" \
  -d bin \
  src/domain/*.java \
  src/service/*.java \
  src/model/*.java \
  src/controller/*.java \
  src/view/*.java \
  src/*.java
```

**Explanation:**
- `-cp "lib/mysql-connector-j-9.5.0.jar:src"` - Include JDBC driver and src folder
- `-d bin` - Output compiled .class files to bin/ directory
- Compiles in correct order: domain → service → model → controller → view → main

### Option 2: Compile Individual Packages

```bash
cd ~/Documents/GitHub/TravelAssistant

# Create bin directory
mkdir -p bin

# Compile domain (no dependencies)
javac -cp src -d bin src/domain/*.java

# Compile service (needs domain + JDBC driver)
javac -cp "lib/mysql-connector-j-9.5.0.jar:bin:src" -d bin src/service/*.java

# Compile model (needs domain + service)
javac -cp "lib/mysql-connector-j-9.5.0.jar:bin:src" -d bin src/model/*.java

# Compile controller (needs domain + model + service)
javac -cp "lib/mysql-connector-j-9.5.0.jar:bin:src" -d bin src/controller/*.java

# Compile view (needs model + domain)
javac -cp "lib/mysql-connector-j-9.5.0.jar:bin:src" -d bin src/view/*.java

# Compile main classes
javac -cp "lib/mysql-connector-j-9.5.0.jar:bin:src" -d bin src/*.java
```

### Option 3: Quick Compile (In src directory)

```bash
cd ~/Documents/GitHub/TravelAssistant/src

javac -cp "../lib/mysql-connector-j-9.5.0.jar:." \
  domain/*.java \
  service/*.java \
  model/*.java \
  controller/*.java \
  view/*.java \
  *.java
```

---

## ▶️ Running Programs

### Test Database Connection First

```bash
cd ~/Documents/GitHub/TravelAssistant

# Run database connection test
java -cp "lib/mysql-connector-j-9.5.0.jar:bin" TestDatabaseConnection
```

**Expected Output:**
```
============================================================
   TESTING MYSQL DATABASE CONNECTION
   CPS731 Travel Assistant - Phase 3
============================================================

[TEST 1] Basic Connection Test
------------------------------------------------------------
✓ MySQL JDBC Driver loaded successfully
✓ Connected to MySQL database: travel_assistant_db
✅ Database connection test: SUCCESS
   Database: travel_assistant_db
   Driver: MySQL Connector/J
   Version: mysql-connector-j-9.5.0
...
✅ ALL TESTS PASSED!
```

### Run Main Application

```bash
cd ~/Documents/GitHub/TravelAssistant

# Run main application
java -cp "lib/mysql-connector-j-9.5.0.jar:bin" Main
```

---

## 🚨 Common Errors & Solutions

### Error: "ClassNotFoundException: com.mysql.cj.jdbc.Driver"

**Problem**: JDBC driver not in classpath

**Solution**: Make sure to include `-cp "lib/mysql-connector-j-9.5.0.jar:bin"` when running

```bash
# ❌ Wrong (missing JDBC driver)
java Main

# ✅ Correct
java -cp "lib/mysql-connector-j-9.5.0.jar:bin" Main
```

### Error: "SQLException: Communications link failure"

**Problem**: MySQL server not running

**Solution**: Start MySQL
```bash
brew services start mysql
```

### Error: "SQLException: Unknown database 'travel_assistant_db'"

**Problem**: Database not created

**Solution**: Run schema.sql
```bash
mysql -u root travel_assistant_db < docs/schema.sql
```

### Error: "package service does not exist"

**Problem**: Compiled without proper classpath

**Solution**: Recompile with correct order and classpath
```bash
javac -cp "lib/mysql-connector-j-9.5.0.jar:src" -d bin src/domain/*.java src/service/*.java ...
```

---

## 📝 Quick Reference Commands

### Full Build & Test Workflow

```bash
# 1. Clean previous build
rm -rf bin/
mkdir -p bin

# 2. Compile everything
cd ~/Documents/GitHub/TravelAssistant
javac -cp "lib/mysql-connector-j-9.5.0.jar:src" -d bin \
  src/domain/*.java \
  src/service/*.java \
  src/model/*.java \
  src/controller/*.java \
  src/view/*.java \
  src/*.java

# 3. Test database connection
java -cp "lib/mysql-connector-j-9.5.0.jar:bin" TestDatabaseConnection

# 4. Run main application
java -cp "lib/mysql-connector-j-9.5.0.jar:bin" Main
```

### One-Line Compile & Run

```bash
# Compile
cd ~/Documents/GitHub/TravelAssistant && \
javac -cp "lib/mysql-connector-j-9.5.0.jar:src" -d bin src/**/*.java src/*.java

# Run test
java -cp "lib/mysql-connector-j-9.5.0.jar:bin" TestDatabaseConnection
```

---

## 🎯 For TA Demo

**Show compilation with proper classpath:**
```bash
javac -cp "lib/mysql-connector-j-9.5.0.jar:src" -d bin src/**/*.java src/*.java
```

**Show database connection test:**
```bash
java -cp "lib/mysql-connector-j-9.5.0.jar:bin" TestDatabaseConnection
```

**Explain traceability:**
- "DatabaseConnection.java implements GRASP Indirection pattern"
- "Isolates database logic from ProfileContextStore and AnalyticsLogger"
- "Singleton pattern for connection management"

---

## 📚 Additional Notes

### Why Include JDBC Driver in Classpath?

Java doesn't automatically know where to find the MySQL JDBC driver. We must explicitly tell Java:
- **Compile time**: Where to find the Driver classes (`com.mysql.cj.jdbc.Driver`)
- **Runtime**: Where to load the Driver JAR from

### Classpath Syntax

**macOS/Linux:**
```bash
-cp "lib/mysql-connector-j-9.5.0.jar:bin:src"
```

**Windows:**
```cmd
-cp "lib\mysql-connector-j-9.5.0.jar;bin;src"
```

(Note: `:` on Mac/Linux, `;` on Windows)

### Alternative: Using CLASSPATH Environment Variable

```bash
# Set CLASSPATH (not recommended for projects)
export CLASSPATH="lib/mysql-connector-j-9.5.0.jar:bin:src"

# Now you can compile without -cp
javac src/domain/*.java
java Main
```

But for educational projects, **explicit -cp is better** (clearer what's happening).

---

**Last Updated**: November 20, 2024
**Phase 3 - Database Integration**
