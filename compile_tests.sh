#!/bin/bash

# TravelAssistant - Test Compilation Script
# This script compiles all source code and tests

echo "========================================="
echo "  TravelAssistant - Test Compilation"
echo "========================================="
echo ""

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if JUnit JAR exists
if [ ! -f "junit-platform-console-standalone-1.10.1.jar" ]; then
    echo -e "${RED}ERROR: junit-platform-console-standalone-1.10.1.jar not found!${NC}"
    echo ""
    echo "Please download it from:"
    echo "https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.1/junit-platform-console-standalone-1.10.1.jar"
    echo ""
    echo "Or run:"
    echo "curl -o junit-platform-console-standalone-1.10.1.jar https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.1/junit-platform-console-standalone-1.10.1.jar"
    exit 1
fi

echo -e "${YELLOW}Step 1: Compiling source code...${NC}"

# Compile domain classes
echo "  - Compiling domain classes..."
javac -d . src/domain/*.java 2>&1
if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to compile domain classes${NC}"
    exit 1
fi

# Compile service classes
echo "  - Compiling service classes..."
javac -d . src/service/*.java 2>&1
if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to compile service classes${NC}"
    exit 1
fi

# Compile model classes
echo "  - Compiling model classes..."
javac -d . src/model/*.java 2>&1
if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to compile model classes${NC}"
    exit 1
fi

# Compile controller classes
echo "  - Compiling controller classes..."
javac -d . src/controller/*.java 2>&1
if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to compile controller classes${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Source code compiled successfully${NC}"
echo ""

echo -e "${YELLOW}Step 2: Compiling test code...${NC}"

# Compile domain tests
echo "  - Compiling domain tests..."
javac -cp ".:junit-platform-console-standalone-1.10.1.jar" test/domain/*.java 2>&1
if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to compile domain tests${NC}"
    exit 1
fi

# Compile service tests
echo "  - Compiling service tests..."
javac -cp ".:junit-platform-console-standalone-1.10.1.jar" test/service/*.java 2>&1
if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to compile service tests${NC}"
    exit 1
fi

# Compile model tests
echo "  - Compiling model tests..."
javac -cp ".:junit-platform-console-standalone-1.10.1.jar" test/model/*.java 2>&1
if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to compile model tests${NC}"
    exit 1
fi

# Compile controller tests
echo "  - Compiling controller tests..."
javac -cp ".:junit-platform-console-standalone-1.10.1.jar" test/controller/*.java 2>&1
if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to compile controller tests${NC}"
    exit 1
fi

# Compile test runner
echo "  - Compiling test runner..."
javac -cp ".:junit-platform-console-standalone-1.10.1.jar" test/TestRunner.java 2>&1
if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to compile test runner${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Tests compiled successfully${NC}"
echo ""

echo "========================================="
echo -e "${GREEN}  COMPILATION COMPLETE!${NC}"
echo "========================================="
echo ""
echo "To run tests, execute:"
echo "  ./run_tests.sh"
echo ""
echo "Or run manually:"
echo "  java -jar junit-platform-console-standalone-1.10.1.jar --class-path . --scan-class-path"
echo ""
