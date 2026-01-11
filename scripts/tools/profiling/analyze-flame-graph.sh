#!/bin/bash
# Analyze flame graph and generate optimization recommendations
# Issue: #1974
# Usage: ./analyze-flame-graph.sh <profile-file>

set -e

PROFILE=$1

if [ -z "$PROFILE" ]; then
    echo "Usage: ./analyze-flame-graph.sh <profile-file>"
    exit 1
fi

if [ ! -f "$PROFILE" ]; then
    echo "ERROR: Profile file not found: $PROFILE"
    exit 1
fi

echo "=========================================="
echo "Flame Graph Analysis"
echo "Profile: $PROFILE"
echo "=========================================="

echo ""
echo "Top 20 functions by cumulative time:"
echo "--------------------------------------"
go tool pprof -top -cum "$PROFILE" 2>/dev/null | head -25

echo ""
echo "Top 20 functions by flat time:"
echo "--------------------------------------"
go tool pprof -top "$PROFILE" 2>/dev/null | head -25

echo ""
echo "Functions with most samples:"
echo "--------------------------------------"
go tool pprof -top -sample_index=cpu "$PROFILE" 2>/dev/null | head -25

echo ""
echo "=========================================="
echo "Starting interactive pprof web UI..."
echo "Open http://localhost:8080 in browser"
echo "Press Ctrl+C to stop"
echo "=========================================="

go tool pprof -http=:8080 "$PROFILE"
