#!/bin/bash

# ESLint runner script
# Runs ESLint from the linting directory

cd "$(dirname "$0")/linting" || exit 1

if [ ! -f "package.json" ]; then
    echo "Error: package.json not found in linting directory"
    exit 1
fi

# Install dependencies if node_modules doesn't exist
if [ ! -d "node_modules" ]; then
    echo "Installing ESLint dependencies..."
    npm install
fi

# Run ESLint
echo "Running ESLint..."
npm run lint "$@"
