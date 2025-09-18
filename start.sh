#!/bin/bash

echo "🚀 Starting Travel Assistant Application"
echo "========================================"

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker first."
    exit 1
fi

# Function to clean up on exit
cleanup() {
    echo ""
    echo "🛑 Shutting down services..."
    docker compose down
}

# Trap to cleanup on script exit
trap cleanup EXIT

# Start in development mode by default
if [ "$1" = "prod" ]; then
    echo "🏭 Starting in PRODUCTION mode..."
    docker compose up --build
else
    echo "🔧 Starting in DEVELOPMENT mode..."
    echo "Use './start.sh prod' for production mode"
    docker compose -f docker-compose.dev.yml up --build
fi