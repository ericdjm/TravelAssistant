# MySQL Quick Reference Guide

**CPS731 Travel Assistant - Database Setup**

---

## 🚀 First-Time Setup (New Users Start Here!)

If this is your first time setting up the project, follow the instructions for your operating system.

---

### 🍎 macOS Setup

#### Step 1: Install MySQL via Homebrew

```bash
# Install Homebrew if you don't have it
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Install MySQL
brew install mysql
```

#### Step 2: Start MySQL Service

```bash
# Start MySQL as a background service (auto-starts on boot)
brew services start mysql
```

#### Step 3: Create the Database

```bash
# Connect to MySQL and create the database
mysql -u root -e "CREATE DATABASE travel_assistant_db;"
```

#### Step 4: Create Tables (Run Schema Files)

```bash
# Navigate to project directory
cd ~/Documents/GitHub/TravelAssistant

# Run the main schema (creates profiles, sessions, conversation_history, events tables)
mysql -u root travel_assistant_db < docs/schema.sql

# Run the POIs schema (creates pois table with 47 sample Toronto POIs)
mysql -u root travel_assistant_db < docs/schema_pois.sql
```

#### Step 5: Verify Setup

```bash
# Check that all tables were created
mysql -u root -e "USE travel_assistant_db; SHOW TABLES;"

# Verify POIs were inserted (should show 47)
mysql -u root -e "SELECT COUNT(*) as total_pois FROM travel_assistant_db.pois;"
```

---

### 🪟 Windows Setup

#### Step 1: Download and Install MySQL

1. Go to: https://dev.mysql.com/downloads/installer/
2. Download **MySQL Installer for Windows** (mysql-installer-community)
3. Run the installer and choose **"Developer Default"** or **"Server only"**
4. During setup:
   - Set root password (or leave blank for local dev)
   - Keep default port: `3306`
   - Select **"Start MySQL Server at System Startup"**
5. Complete the installation

#### Step 2: Add MySQL to PATH (if not done automatically)

1. Find MySQL installation path (usually `C:\Program Files\MySQL\MySQL Server 8.0\bin`)
2. Add to System PATH:
   - Open **System Properties** → **Environment Variables**
   - Under **System Variables**, find `Path` and click **Edit**
   - Add: `C:\Program Files\MySQL\MySQL Server 8.0\bin`
   - Click **OK** to save

#### Step 3: Open Command Prompt and Create Database

```cmd
# Open Command Prompt (cmd) or PowerShell as Administrator

# If you set a root password during installation:
mysql -u root -p -e "CREATE DATABASE travel_assistant_db;"

# If no password (local dev):
mysql -u root -e "CREATE DATABASE travel_assistant_db;"
```

#### Step 4: Create Tables (Run Schema Files)

```cmd
# Navigate to project directory
cd C:\Users\YourUsername\Documents\GitHub\TravelAssistant

# Run the main schema (if you have a password, add -p flag)
mysql -u root travel_assistant_db < docs\schema.sql

# Run the POIs schema
mysql -u root travel_assistant_db < docs\schema_pois.sql
```

**Alternative: Use MySQL Workbench**
1. Open MySQL Workbench
2. Connect to your local MySQL server
3. Open `docs/schema.sql` and execute (lightning bolt icon)
4. Open `docs/schema_pois.sql` and execute

#### Step 5: Verify Setup

```cmd
mysql -u root -e "USE travel_assistant_db; SHOW TABLES;"

# Verify POIs were inserted (should show 47)
mysql -u root -e "SELECT COUNT(*) as total_pois FROM travel_assistant_db.pois;"
```

---

### ✅ Expected Output (Both Platforms)

After running the schema files, you should see 5 tables:

```
+--------------------------------+
| Tables_in_travel_assistant_db  |
+--------------------------------+
| conversation_history           |
| events                         |
| pois                           |
| profiles                       |
| sessions                       |
+--------------------------------+
```

And 47 POIs in the database.

---

## ✅ One-Time Setup (Already Done!)

If you've already completed the steps above:
- ✅ Installed MySQL (Homebrew on macOS / Installer on Windows)
- ✅ Started MySQL as a background service
- ✅ Created database: `travel_assistant_db`
- ✅ Created tables via schema.sql and schema_pois.sql

---

## 🚀 Do I Need to Start MySQL Every Time?

**NO!** MySQL runs as a **background service** that:
- ✅ Starts automatically when your computer boots
- ✅ Runs in the background always
- ✅ No need to start it again in new terminal windows

**Translation**: MySQL is now always running. You're good to go!

---

## 🔍 Check if MySQL is Running

### macOS
```bash
brew services list | grep mysql
```

**Expected output:**
```
mysql    started    username    ~/Library/LaunchAgents/homebrew.mxcl.mysql.plist
```

### Windows (Command Prompt as Admin)
```cmd
sc query mysql
```

Or check in **Services**:
1. Press `Win + R`, type `services.msc`, press Enter
2. Look for **MySQL** or **MySQL80** - Status should be **Running**

If it says `started` or `Running` → ✅ MySQL is running!

---

## 🔌 Connect to MySQL

### Option 1: Interactive Shell (for manual queries)
```bash
mysql -u root
```

You'll see:
```
mysql>
```

Now you can run SQL commands:
```sql
SHOW DATABASES;
USE travel_assistant_db;
SHOW TABLES;
EXIT;
```

### Option 2: Run Single Command (quick check)
```bash
mysql -u root -e "SHOW DATABASES;"
```

### Option 3: Run SQL Script File
```bash
mysql -u root travel_assistant_db < schema.sql
```

---

## 🛠️ Common MySQL Commands

### Check Your Database
```bash
# List all databases
mysql -u root -e "SHOW DATABASES;"

# Show tables in your database
mysql -u root -e "USE travel_assistant_db; SHOW TABLES;"

# Check table structure
mysql -u root -e "USE travel_assistant_db; DESCRIBE profiles;"
```

### Start/Stop/Restart MySQL Service

#### macOS (using Homebrew)

```bash
# Stop MySQL
brew services stop mysql

# Start MySQL
brew services start mysql

# Restart MySQL
brew services restart mysql

# Check status
brew services list
```

#### Windows (Command Prompt as Administrator)

```cmd
# Stop MySQL
net stop mysql

# Start MySQL
net start mysql

# Restart MySQL
net stop mysql && net start mysql
```

Or use **Services** GUI:
1. Press `Win + R`, type `services.msc`, press Enter
2. Find **MySQL** or **MySQL80**
3. Right-click → Start / Stop / Restart

---

## 🗄️ Your Database Info

| Setting | Value |
|---------|-------|
| **Database Name** | `travel_assistant_db` |
| **Host** | `localhost` (127.0.0.1) |
| **Port** | `3306` (default) |
| **Username** | `root` |
| **Password** | *(none - local dev only)* |
| **JDBC URL** | `jdbc:mysql://localhost:3306/travel_assistant_db` |

---

## 📊 Using MySQL with Your Java Project

### Connection String
```java
String url = "jdbc:mysql://localhost:3306/travel_assistant_db";
String username = "root";
String password = ""; // Empty for local dev

Connection conn = DriverManager.getConnection(url, username, password);
```

### MySQL JDBC Driver
Download from: https://dev.mysql.com/downloads/connector/j/

Or add Maven dependency:
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

---

## 🔧 Common Issues & Solutions

### Issue: "Can't connect to MySQL server"
**Solution**: Start MySQL
```bash
brew services start mysql
```

### Issue: "Access denied for user 'root'"
**Solution**: You set a password during `mysql_secure_installation`. Connect with:
```bash
mysql -u root -p
```
(then enter your password)

### Issue: "Database 'travel_assistant_db' doesn't exist"
**Solution**: Recreate it
```bash
mysql -u root -e "CREATE DATABASE travel_assistant_db;"
```

### Issue: MySQL won't start
**Solution**: Check logs
```bash
tail -f /usr/local/var/mysql/*.err
```

Or restart:
```bash
brew services restart mysql
```

---

## 🎯 Quick Workflow for Your Project

### Daily Development (Normal Workflow)
```bash
# 1. Open terminal → MySQL is already running! ✅
# 2. Run your Java application → it connects automatically
# 3. Done!
```

**You don't need to do anything!** MySQL is always running in the background.

---

### When You Need to Run SQL Commands
```bash
# Connect to MySQL
mysql -u root

# Switch to your database
mysql> USE travel_assistant_db;

# Run queries
mysql> SELECT * FROM profiles;

# Exit
mysql> EXIT;
```

---

### When You Need to Run Schema Updates
```bash
# Run the schema.sql file to create/update tables
mysql -u root travel_assistant_db < docs/schema.sql
```

---

## 📝 Useful SQL Commands for Your Project

### View All Tables
```sql
USE travel_assistant_db;
SHOW TABLES;
```

### View Table Structure
```sql
DESCRIBE profiles;
DESCRIBE sessions;
DESCRIBE pois;
DESCRIBE events;
```

### View Data
```sql
SELECT * FROM profiles;
SELECT * FROM sessions;
SELECT * FROM pois LIMIT 10;
SELECT * FROM events ORDER BY timestamp DESC LIMIT 20;
```

### Clear Data (for testing)
```sql
TRUNCATE TABLE events;
TRUNCATE TABLE sessions;
TRUNCATE TABLE profiles;
```

### Drop and Recreate (full reset)
```sql
DROP DATABASE travel_assistant_db;
CREATE DATABASE travel_assistant_db;
USE travel_assistant_db;
SOURCE docs/schema.sql;
```

---

## 🎓 For TA Demo

When demonstrating to the TA:

**Show MySQL is Running:**
```bash
brew services list | grep mysql
```

**Show Your Database:**
```bash
mysql -u root -e "SHOW DATABASES;"
mysql -u root -e "USE travel_assistant_db; SHOW TABLES;"
```

**Show Sample Data:**
```bash
mysql -u root travel_assistant_db -e "SELECT * FROM profiles;"
```

---

## 🔐 Security Note

**Current Setup:** No password (local development only)

**For Production:** You should:
1. Set a root password: `mysql_secure_installation`
2. Create a dedicated app user:
```sql
CREATE USER 'travelapp'@'localhost' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON travel_assistant_db.* TO 'travelapp'@'localhost';
FLUSH PRIVILEGES;
```

**For CPS731 Project:** Current setup is fine (local development, no password needed)

---

## 🆘 Emergency Commands

**MySQL won't start:**
```bash
brew services restart mysql
brew services list
tail -f /usr/local/var/mysql/*.err
```

**Complete reinstall** (if everything breaks):
```bash
brew services stop mysql
brew uninstall mysql
brew cleanup
brew install mysql
brew services start mysql
mysql -u root -e "CREATE DATABASE travel_assistant_db;"
```

**Backup your database** (before big changes):
```bash
mysqldump -u root travel_assistant_db > backup_$(date +%Y%m%d).sql
```

**Restore from backup:**
```bash
mysql -u root travel_assistant_db < backup_20241120.sql
```

---

## 📚 Additional Resources

- **MySQL Documentation**: https://dev.mysql.com/doc/
- **JDBC Tutorial**: https://docs.oracle.com/javase/tutorial/jdbc/
- **Homebrew Services**: `brew services --help`

---

## ✅ TL;DR (Too Long; Didn't Read)

**Normal workflow:**
1. MySQL is **always running** in the background (you already started it!)
2. Just run your Java app → it connects automatically
3. No need to start MySQL in new terminal windows

**Need to run SQL commands:**
```bash
mysql -u root
mysql> USE travel_assistant_db;
mysql> [your SQL here]
mysql> EXIT;
```

**Need to check if MySQL is running:**
```bash
brew services list | grep mysql
```

**That's it!** 🎉

---

*Last Updated: November 20, 2024*
*CPS731 Team 20 - Phase 3 Implementation*
