#!/usr/bin/env bash

# ==============================================================================
# CodSoft Java Development Internship - Task 4: Quiz Application with Timer
# Build and Run Script for macOS and Linux
# ==============================================================================

set -e

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BIN_DIR="${PROJECT_DIR}/bin"
SRC_MAIN="${PROJECT_DIR}/src/main/java"
SRC_TEST="${PROJECT_DIR}/src/test/java"

echo "========================================================"
echo "  CodSoft Task 4 - Quiz Application with Timer          "
echo "========================================================"

# Step 1: Create bin directory
mkdir -p "${BIN_DIR}"

# Step 2: Compile main source files
echo "==> Compiling Java source files..."
javac -d "${BIN_DIR}" $(find "${SRC_MAIN}" -name "*.java")

# Optional: compile test files if they exist
if [ -d "${SRC_TEST}" ]; then
    javac -d "${BIN_DIR}" -sourcepath "${SRC_MAIN}:${SRC_TEST}" $(find "${SRC_TEST}" -name "*.java")
fi

echo "==> Compilation successful!"

# Check arguments
if [ "$1" == "test" ]; then
    echo "==> Running Automated Unit Tests..."
    java -cp "${BIN_DIR}" com.codsoft.quiz.QuizAppTest
    echo ""
    echo "==> Running GUI Component Smoke Test..."
    java -cp "${BIN_DIR}" com.codsoft.quiz.GuiSmokeTest
    exit 0
fi

# Step 3: Run the Application
echo "==> Launching Desktop Application..."
java -cp "${BIN_DIR}" com.codsoft.quiz.Main
