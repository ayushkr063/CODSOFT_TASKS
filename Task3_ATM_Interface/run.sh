#!/bin/bash
# Convenient runner script for Task 3: ATM Interface

cd "$(dirname "$0")"

echo "Compiling Java source files..."
javac *.java

if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

echo "Compilation successful."

if [ "$1" == "--test" ]; then
    echo "Running automated verification tests..."
    java ATMTest
else
    java Main "$@"
fi
