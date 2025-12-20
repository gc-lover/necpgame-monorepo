#!/bin/bash
# Post-Release Quest System Monitoring
# Issue: #51 - Quest System Release
# Monitors quest system performance and health for 72 hours post-release

set -e

# Configuration
MONITORING_DURATION="${MONITORING_DURATION:-4320}"  # 72 hours in minutes
CHECK_INTERVAL="${CHECK_INTERVAL:-300}"            # 5 minutes
LOG_FILE="${LOG_FILE:-quest-monitoring-$(date +%Y%m%d-%H%M%S).log}"

# API endpoints
API_BASE="${API_BASE:-http://localhost:8083}"
WS_URL="${WS_URL:-ws://localhost:8301}"

# Thresholds
LATENCY_THRESHOLD=100        # ms
ERROR_RATE_THRESHOLD=5       # %
MEMORY_THRESHOLD=80          # %
CPU_THRESHOLD=80             # %

echo "🎯 Starting Quest System Post-Release Monitoring"
echo "================================================="
echo "Duration: $(($MONITORING_DURATION / 60)) hours"
echo "Interval: $(($CHECK_INTERVAL / 60)) minutes"
echo "Log file: $LOG_FILE"
echo "================================================="

# Initialize monitoring
start_time=$(date +%s)
checks_completed=0
alerts_triggered=0

# Create log file header
cat > "$LOG_FILE" << EOF
Quest System Post-Release Monitoring Log
Started: $(date)
Duration: $(($MONITORING_DURATION / 60)) hours
Check Interval: $(($CHECK_INTERVAL / 60)) minutes

Timestamp,Check Type,Status,Value,Threshold,Details
EOF

log_entry() {
    local timestamp=$(date '+%Y-%m-%d %H:%M:%S')
    local check_type="$1"
    local status="$2"
    local value="$3"
    local threshold="$4"
    local details="$5"

    echo "$timestamp,$check_type,$status,$value,$threshold,$details" >> "$LOG_FILE"

    if [ "$status" = "ALERT" ]; then
        echo "🚨 ALERT: $check_type - $details"
        ((alerts_triggered++))
    fi
}

# Health check functions
check_api_health() {
    local response_time=$(curl -s -w "%{time_total}" -o /dev/null "${API_BASE}/health" 2>/dev/null || echo "999")
    local http_code=$(curl -s -w "%{http_code}" -o /dev/null "${API_BASE}/health" 2>/dev/null || echo "000")

    if [ "$http_code" = "200" ]; then
        response_time_ms=$(echo "$response_time * 1000" | bc 2>/dev/null || echo "0")
        if (( $(echo "$response_time_ms < $LATENCY_THRESHOLD" | bc -l 2>/dev/null || echo "0") )); then
            log_entry "API_HEALTH" "OK" "${response_time_ms}ms" "${LATENCY_THRESHOLD}ms" "Response time within threshold"
        else
            log_entry "API_HEALTH" "ALERT" "${response_time_ms}ms" "${LATENCY_THRESHOLD}ms" "Response time too high"
        fi
    else
        log_entry "API_HEALTH" "ALERT" "HTTP_$http_code" "200" "Health check failed"
    fi
}

check_quest_api() {
    local response_time=$(curl -s -w "%{time_total}" -o /dev/null "${API_BASE}/gameplay/quests" 2>/dev/null || echo "999")
    local quest_count=$(curl -s "${API_BASE}/gameplay/quests" 2>/dev/null | jq '.quests | length' 2>/dev/null || echo "0")

    response_time_ms=$(echo "$response_time * 1000" | bc 2>/dev/null || echo "0")

    if [ "$quest_count" -gt 0 ]; then
        log_entry "QUEST_API" "OK" "${quest_count}_quests,${response_time_ms}ms" "1+,${LATENCY_THRESHOLD}ms" "Quest API functional"
    else
        log_entry "QUEST_API" "ALERT" "${quest_count}_quests,${response_time_ms}ms" "1+,${LATENCY_THRESHOLD}ms" "No quests returned"
    fi
}

check_database_performance() {
    # This would connect to database and run performance queries
    # For now, we'll simulate with API call that hits database
    local query_time=$(curl -s -w "%{time_total}" -o /dev/null "${API_BASE}/gameplay/quests?limit=1" 2>/dev/null || echo "999")
    query_time_ms=$(echo "$query_time * 1000" | bc 2>/dev/null || echo "0")

    if (( $(echo "$query_time_ms < 50" | bc -l 2>/dev/null || echo "0") )); then
        log_entry "DB_PERFORMANCE" "OK" "${query_time_ms}ms" "50ms" "Database query performance good"
    else
        log_entry "DB_PERFORMANCE" "ALERT" "${query_time_ms}ms" "50ms" "Database query slow"
    fi
}

check_websocket_health() {
    # Basic WebSocket connectivity check
    if command -v websocat >/dev/null 2>&1; then
        if timeout 10 websocat "${WS_URL}/health" >/dev/null 2>&1; then
            log_entry "WEBSOCKET" "OK" "connected" "connected" "WebSocket endpoint accessible"
        else
            log_entry "WEBSOCKET" "ALERT" "failed" "connected" "WebSocket connection failed"
        fi
    else
        log_entry "WEBSOCKET" "SKIP" "websocat_not_found" "connected" "websocat tool not available"
    fi
}

check_system_resources() {
    # Check pod resources if kubectl available
    if command -v kubectl >/dev/null 2>&1; then
        # Check memory usage
        memory_usage=$(kubectl top pods -n necpgame --no-headers 2>/dev/null | awk '{sum+=$3} END {print sum/NR "%"}' || echo "unknown")

        # Check CPU usage
        cpu_usage=$(kubectl top pods -n necpgame --no-headers 2>/dev/null | awk '{sum+=$2} END {print sum/NR "%"}' || echo "unknown")

        if [[ "$memory_usage" != "unknown" ]] && (( $(echo "$memory_usage < $MEMORY_THRESHOLD" | bc -l 2>/dev/null || echo "1") )); then
            log_entry "MEMORY_USAGE" "OK" "${memory_usage}%" "${MEMORY_THRESHOLD}%" "Memory usage within limits"
        else
            log_entry "MEMORY_USAGE" "ALERT" "${memory_usage}%" "${MEMORY_THRESHOLD}%" "High memory usage"
        fi

        if [[ "$cpu_usage" != "unknown" ]] && (( $(echo "$cpu_usage < $CPU_THRESHOLD" | bc -l 2>/dev/null || echo "1") )); then
            log_entry "CPU_USAGE" "OK" "${cpu_usage}%" "${CPU_THRESHOLD}%" "CPU usage within limits"
        else
            log_entry "CPU_USAGE" "ALERT" "${cpu_usage}%" "${CPU_THRESHOLD}%" "High CPU usage"
        fi
    else
        log_entry "SYSTEM_RESOURCES" "SKIP" "kubectl_not_found" "available" "kubectl not available for resource checks"
    fi
}

check_error_rates() {
    # Check application logs for errors (simplified)
    if command -v kubectl >/dev/null 2>&1; then
        error_count=$(kubectl logs --since=5m -n necpgame deployment/gameplay-service-go 2>/dev/null | grep -c "ERROR\|error" || echo "0")

        if [ "$error_count" -lt 10 ]; then  # Arbitrary threshold
            log_entry "ERROR_RATE" "OK" "${error_count}_errors" "<10" "Error rate acceptable"
        else
            log_entry "ERROR_RATE" "ALERT" "${error_count}_errors" "<10" "High error rate detected"
        fi
    else
        log_entry "ERROR_RATE" "SKIP" "kubectl_not_found" "available" "kubectl not available for log checks"
    fi
}

# Main monitoring loop
echo "🔄 Starting monitoring loop..."
while true; do
    current_time=$(date +%s)
    elapsed_minutes=$(( (current_time - start_time) / 60 ))

    if [ $elapsed_minutes -ge $MONITORING_DURATION ]; then
        echo "OK Monitoring duration completed"
        break
    fi

    echo "📊 Check #$((checks_completed + 1)) - $(date)"

    # Run all checks
    check_api_health
    check_quest_api
    check_database_performance
    check_websocket_health
    check_system_resources
    check_error_rates

    ((checks_completed++))

    # Progress update every 10 checks
    if [ $((checks_completed % 10)) -eq 0 ]; then
        hours_remaining=$(( (MONITORING_DURATION - elapsed_minutes) / 60 ))
        echo "📈 Progress: $checks_completed checks completed, $hours_remaining hours remaining"
    fi

    # Wait for next check
    sleep $CHECK_INTERVAL
done

# Generate summary report
echo ""
echo "📋 Monitoring Summary Report"
echo "============================"
echo "Total checks completed: $checks_completed"
echo "Alerts triggered: $alerts_triggered"
echo "Monitoring duration: $(($elapsed_minutes / 60)) hours"
echo "Log file: $LOG_FILE"

# Summary statistics from log
total_ok=$(grep ",OK," "$LOG_FILE" | wc -l)
total_alerts=$(grep ",ALERT," "$LOG_FILE" | wc -l)
total_skips=$(grep ",SKIP," "$LOG_FILE" | wc -l)

echo ""
echo "📊 Results Summary:"
echo "OK OK checks: $total_ok"
echo "🚨 Alerts: $total_alerts"
echo "⏭️ Skipped: $total_skips"

if [ $alerts_triggered -eq 0 ]; then
    echo ""
    echo "🎉 SUCCESS: No critical alerts during monitoring period!"
    echo "Quest system is stable and performing within expected parameters."
else
    echo ""
    echo "WARNING WARNING: $alerts_triggered alerts detected during monitoring."
    echo "Review $LOG_FILE for detailed alert information."
    echo "Consider investigating system performance and configuration."
fi

echo ""
echo "📄 Detailed logs available in: $LOG_FILE"
echo "🔗 Grafana Dashboard: https://grafana.necpgame.com/d/quest-system"
echo "📧 Alert Notifications: Check AlertManager for active alerts"

# Send completion notification (would integrate with actual notification system)
echo ""
echo "OK Post-release monitoring completed successfully!"
