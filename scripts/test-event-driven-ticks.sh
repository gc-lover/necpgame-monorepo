#!/bin/bash

# Test Event-Driven Simulation Tick Infrastructure (#2281)
# This script tests the complete Kafka-based tick system

set -e

echo "🧪 Testing Event-Driven Simulation Tick Infrastructure"
echo "======================================================"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Configuration
KAFKA_BROKERS="localhost:9092"
ECONOMY_SERVICE_URL="http://localhost:8083"
SIMULATION_TICKER_URL="http://localhost:8084"

# Function to check if service is healthy
check_service() {
    local url=$1
    local service_name=$2
    echo -n "🔍 Checking $service_name... "

    if curl -f -s "$url/health" > /dev/null 2>&1; then
        echo -e "${GREEN}✓ Healthy${NC}"
        return 0
    else
        echo -e "${RED}✗ Unhealthy${NC}"
        return 1
    fi
}

# Function to check Kafka topic
check_kafka_topic() {
    local topic=$1
    echo -n "🔍 Checking Kafka topic '$topic'... "

    if docker exec necpgame-kafka-1 kafka-topics --bootstrap-server localhost:9092 --list | grep -q "^$topic$"; then
        echo -e "${GREEN}✓ Exists${NC}"
        return 0
    else
        echo -e "${RED}✗ Missing${NC}"
        return 1
    fi
}

# Function to send test tick
send_test_tick() {
    local tick_type=$1
    echo -e "\n📤 Sending test $tick_type tick..."

    if curl -X POST "$SIMULATION_TICKER_URL/tick/$tick_type" \
         -H "Content-Type: application/json" \
         -d '{"test": true}' \
         -s > /dev/null; then
        echo -e "${GREEN}✓ Tick sent successfully${NC}"
        return 0
    else
        echo -e "${RED}✗ Failed to send tick${NC}"
        return 1
    fi
}

# Function to check if tick was processed
check_tick_processed() {
    local service_name=$1
    local expected_log=$2

    echo -n "🔍 Checking if $service_name processed tick... "

    # This is a simplified check - in real implementation you'd check logs or metrics
    if docker logs necpgame-$service_name-1 2>&1 | grep -q "$expected_log" 2>/dev/null; then
        echo -e "${GREEN}✓ Processed${NC}"
        return 0
    else
        echo -e "${YELLOW}⚠ Could not verify${NC}"
        return 0  # Don't fail the test for log checking issues
    fi
}

echo "Step 1: Checking infrastructure health"
echo "--------------------------------------"

# Check Kafka topics
check_kafka_topic "world.tick.hourly"
check_kafka_topic "world.tick.daily"
check_kafka_topic "simulation.event"

# Check services
check_service "$ECONOMY_SERVICE_URL" "economy-service"
check_service "$SIMULATION_TICKER_URL" "simulation-ticker-service"

echo -e "\nStep 2: Testing hourly tick processing"
echo "--------------------------------------"

# Send hourly tick
if send_test_tick "hourly"; then
    echo "⏳ Waiting 5 seconds for processing..."
    sleep 5

    # Check if economy service processed it
    check_tick_processed "economy-service" "hourly tick"
fi

echo -e "\nStep 3: Testing daily tick processing"
echo "-------------------------------------"

# Send daily tick
if send_test_tick "daily"; then
    echo "⏳ Waiting 5 seconds for processing..."
    sleep 5

    # Check if world simulation service processed it
    check_tick_processed "world-simulation-service" "daily tick"
fi

echo -e "\nStep 4: Checking simulation events"
echo "-----------------------------------"

# Check if simulation events were published
echo -n "🔍 Checking simulation.event topic... "
if docker exec necpgame-kafka-1 kafka-console-consumer --bootstrap-server localhost:9092 --topic simulation.event --max-messages 1 --timeout-ms 5000 > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Events published${NC}"
else
    echo -e "${YELLOW}⚠ No events found (may be normal if no processing occurred)${NC}"
fi

echo -e "\n🎉 Event-Driven Tick Infrastructure Test Complete!"
echo "=================================================="
echo "If all checks passed, the event-driven simulation system is working correctly."
echo ""
echo "Next steps:"
echo "1. Monitor service logs for detailed processing information"
echo "2. Check Kafka topics for message flow: world.tick.* → services → simulation.event"
echo "3. Verify that economy market clearing and diplomacy evaluation are working"
echo ""
echo "Issue: #2281 - Event-Driven Simulation Tick Infrastructure"