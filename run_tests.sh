#!/bin/bash

# TravelAssistant - Test Execution Script
# This script runs all JUnit 5 tests

echo "========================================="
echo "  TravelAssistant - Test Execution"
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
    echo "Please run: ./compile_tests.sh first"
    exit 1
fi

# Check if tests are compiled
if [ ! -d "test" ]; then
    echo -e "${RED}ERROR: Test directory not found!${NC}"
    exit 1
fi

echo -e "${YELLOW}Running JUnit 5 Test Suite...${NC}"
echo ""

# Run all tests
java -jar junit-platform-console-standalone-1.10.1.jar \
  --class-path . \
  --scan-class-path \
  --details tree

EXIT_CODE=$?

echo ""
echo "========================================="
if [ $EXIT_CODE -eq 0 ]; then
    echo -e "${GREEN}  ALL TESTS PASSED!${NC}"
else
    echo -e "${RED}  SOME TESTS FAILED!${NC}"
fi
echo "========================================="
echo ""

exit $EXIT_CODE
