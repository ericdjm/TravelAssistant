# MySQL Quick Reference Guide

**CPS731 Travel Assistant - Database Setup**

---

## ✅ One-Time Setup (Already Done!)

You've already completed these steps:
- ✅ Installed MySQL via Homebrew
- ✅ Started MySQL as a background service
- ✅ Created database: `travel_assistant_db`

---

## 🚀 Do I Need to Start MySQL Every Time?

**NO!** You already ran:
```bash
brew services start mysql
```

This made MySQL run as a **background service** that:
- ✅ Starts automatically when your Mac boots
- ✅ Runs in the background always
- ✅ No need to start it again in new terminal windows

**Translation**: MySQL is now always running on your Mac. You're good to go! 🎉

---

## 🔍 Check if MySQL is Running

```bash
brew services list | grep mysql
```

**Expected output:**
```
mysql    started    husseinsaab    ~/Library/LaunchAgents/homebrew.mxcl.mysql.plist
```

If it says `started` → ✅ MySQL is running!

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

**Stop MySQL** (if you need to):
```bash
brew services stop mysql
```

**Start MySQL**:
```bash
brew services start mysql
```

**Restart MySQL** (if something goes wrong):
```bash
brew services restart mysql
```

**Check status**:
```bash
brew services list
```

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
