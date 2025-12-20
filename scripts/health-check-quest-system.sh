#!/bin/bash
# Quest System Health Check
# Issue: #51 - DevOps setup

set -e

echo "🏥 Quest System Health Check"
echo "================================="

BASE_URL="${QUEST_API_URL:-http://localhost:8083}"
WS_URL="${QUEST_WS_URL:-ws://localhost:8301}"

# 1. REST API Health Check
echo "📡 Testing REST API health..."
if curl -s -f "${BASE_URL}/health" > /dev/null 2>&1; then
    echo "OK REST API is healthy"
else
    echo "❌ REST API is not responding"
    exit 1
fi

# 2. Database Connectivity
echo "🗄️ Testing database connectivity..."
QUEST_COUNT=$(curl -s "${BASE_URL}/gameplay/quests" | jq '.quests | length' 2>/dev/null || echo "0")
if [ "$QUEST_COUNT" -gt 0 ]; then
    echo "OK Database connectivity OK (found $QUEST_COUNT quests)"
else
    echo "WARNING Database may be empty or API not fully functional"
fi

# 3. Metrics Endpoint
echo "📊 Testing metrics endpoint..."
if curl -s -f "${BASE_URL}/metrics" > /dev/null 2>&1; then
    echo "OK Metrics endpoint is accessible"
else
    echo "❌ Metrics endpoint not responding"
fi

# 4. WebSocket Health (basic connectivity test)
echo "🔌 Testing WebSocket connectivity..."
if command -v websocat > /dev/null 2>&1; then
    # Try to establish WebSocket connection
    if timeout 5 websocat "${WS_URL}/health" > /dev/null 2>&1; then
        echo "OK WebSocket endpoint is accessible"
    else
        echo "WARNING WebSocket endpoint not responding (may be normal if service not running)"
    fi
else
    echo "WARNING websocat not installed, skipping WebSocket test"
fi

# 5. Quest Content Validation
echo "📋 Testing quest content integrity..."
QUEST_IDS=$(curl -s "${BASE_URL}/gameplay/quests" | jq -r '.quests[].id' 2>/dev/null || echo "")

if [ -n "$QUEST_IDS" ]; then
    VALID_QUESTS=0
    TOTAL_QUESTS=0

    for quest_id in $QUEST_IDS; do
        TOTAL_QUESTS=$((TOTAL_QUESTS + 1))
        if curl -s -f "${BASE_URL}/gameplay/quests/${quest_id}" > /dev/null 2>&1; then
            VALID_QUESTS=$((VALID_QUESTS + 1))
        fi
    done

    echo "OK Quest validation: $VALID_QUESTS/$TOTAL_QUESTS quests accessible"
else
    echo "WARNING No quests found in system"
fi

# 6. Performance Metrics
echo "⚡ Checking performance metrics..."
RESPONSE_TIME=$(curl -s -w "%{time_total}" -o /dev/null "${BASE_URL}/health")
if (( $(echo "$RESPONSE_TIME < 0.1" | bc -l) )); then
    echo "OK API response time: ${RESPONSE_TIME}s (excellent)"
elif (( $(echo "$RESPONSE_TIME < 0.5" | bc -l) )); then
    echo "OK API response time: ${RESPONSE_TIME}s (good)"
else
    echo "WARNING API response time: ${RESPONSE_TIME}s (needs optimization)"
fi

echo ""
echo "🎉 Health check completed!"
echo "================================="
echo "Environment variables for customization:"
echo "  QUEST_API_URL - REST API base URL (default: http://localhost:8083)"
echo "  QUEST_WS_URL  - WebSocket base URL (default: ws://localhost:8301)"
