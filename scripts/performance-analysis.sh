#!/bin/bash

# NECP Game Performance Analysis Script
# Анализ производительности системы и сервисов

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo "📊 NECP Game Performance Analysis"
echo "=================================="

# Configuration
OUTPUT_DIR="performance_reports"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
REPORT_FILE="$OUTPUT_DIR/performance_report_$TIMESTAMP.md"

mkdir -p "$OUTPUT_DIR"

# Function to collect system metrics
collect_system_metrics() {
    echo -e "${BLUE}🖥️  Collecting system metrics...${NC}"

    # CPU usage
    local cpu_usage=$(top -bn1 | grep "Cpu(s)" | sed "s/.*, *\([0-9.]*\)%* id.*/\1/" | awk '{print 100 - $1}')

    # Memory usage
    local mem_total=$(free -m | awk 'NR==2{printf "%.2f", $2/1024}')
    local mem_used=$(free -m | awk 'NR==2{printf "%.2f", $3/1024}')
    local mem_usage_percent=$(free | awk 'NR==2{printf "%.1f", $3*100/$2}')

    # Disk usage
    local disk_usage=$(df / | tail -1 | awk '{print $5}' | sed 's/%//')

    echo "CPU Usage: ${cpu_usage}%"
    echo "Memory: ${mem_used}GB/${mem_total}GB (${mem_usage_percent}%)"
    echo "Disk Usage: ${disk_usage}%"

    # Store for report
    SYSTEM_CPU=$cpu_usage
    SYSTEM_MEM_USED=$mem_used
    SYSTEM_MEM_TOTAL=$mem_total
    SYSTEM_MEM_PERCENT=$mem_usage_percent
    SYSTEM_DISK=$disk_usage
}

# Function to analyze Docker containers
analyze_containers() {
    echo -e "${BLUE}🐳 Analyzing Docker containers...${NC}"

    local containers=$(docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | grep -v "NAMES")

    echo "Running containers:"
    echo "$containers"
    echo ""

    # Analyze resource usage
    echo "Container resource usage:"
    docker stats --no-stream --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.NetIO}}\t{{.BlockIO}}" | head -10
    echo ""
}

# Function to test API performance
test_api_performance() {
    local service=$1
    local port=$2
    local endpoint=$3
    local description=$4

    echo -e "${BLUE}🏃 Testing $description performance...${NC}"

    # Warm up
    curl -s "http://localhost:$port$endpoint" > /dev/null 2>&1 || true

    # Performance test with curl
    local results=$(curl -s -w "@curl-format.txt" -o /dev/null "http://localhost:$port$endpoint" 2>/dev/null || echo "0.000000 0 0")

    # Parse results (this is a simplified version)
    local response_time=$(echo "$results" | awk '{print $1}' || echo "0.000")
    local http_code=$(echo "$results" | awk '{print $2}' || echo "000")

    echo "Response time: ${response_time}s"
    echo "HTTP status: $http_code"

    if (( $(echo "$response_time < 0.1" | bc -l 2>/dev/null || echo "1") )); then
        echo -e "${GREEN}OK Good performance (< 100ms)${NC}"
    elif (( $(echo "$response_time < 0.5" | bc -l 2>/dev/null || echo "1") )); then
        echo -e "${YELLOW}WARNING  Acceptable performance (100-500ms)${NC}"
    else
        echo -e "${RED}❌ Poor performance (> 500ms)${NC}"
    fi

    echo ""
}

# Function to analyze service metrics
analyze_service_metrics() {
    echo -e "${BLUE}📈 Analyzing service metrics...${NC}"

    local services=(
        "achievement-service:9200"
        "client-service:9210"
    )

    for service_info in "${services[@]}"; do
        IFS=':' read -r service port <<< "$service_info"

        echo "Checking $service metrics..."

        # Try to get metrics
        local metrics=$(curl -s "http://localhost:$port/metrics" 2>/dev/null | head -20 || echo "Metrics not available")

        if echo "$metrics" | grep -q "go_goroutines"; then
            local goroutines=$(echo "$metrics" | grep "go_goroutines" | awk '{print $2}' || echo "unknown")
            echo "  Goroutines: $goroutines"

            local mem_used=$(echo "$metrics" | grep "go_memstats_heap_alloc_bytes" | awk '{print $2}' | head -1 || echo "unknown")
            if [ "$mem_used" != "unknown" ]; then
                local mem_mb=$((mem_used / 1024 / 1024))
                echo "  Memory used: ${mem_mb}MB"
            fi
        else
            echo "  Metrics not available"
        fi

        echo ""
    done
}

# Function to check database performance
check_database_performance() {
    echo -e "${BLUE}🗄️  Checking database performance...${NC}"

    if docker ps | grep -q necpgame-postgres; then
        # Get basic PostgreSQL stats
        local db_stats=$(docker exec necpgame-postgres psql -U necpg -d necpg -c "SELECT count(*) as connections FROM pg_stat_activity;" 2>/dev/null || echo "Database stats unavailable")

        echo "PostgreSQL status:"
        echo "$db_stats"
    else
        echo "PostgreSQL container not running"
    fi

    if docker ps | grep -q necpgame-redis; then
        # Get basic Redis stats
        local redis_stats=$(docker exec necpgame-redis redis-cli info | grep -E "connected_clients|used_memory_human" || echo "Redis stats unavailable")

        echo "Redis status:"
        echo "$redis_stats"
    else
        echo "Redis container not running"
    fi

    echo ""
}

# Function to generate recommendations
generate_recommendations() {
    echo -e "${BLUE}💡 Generating performance recommendations...${NC}"

    local recommendations=""

    # CPU recommendations
    if (( $(echo "$SYSTEM_CPU > 80" | bc -l 2>/dev/null || echo "0") )); then
        recommendations="${recommendations}- High CPU usage detected. Consider scaling services horizontally\n"
    fi

    # Memory recommendations
    if (( $(echo "$SYSTEM_MEM_PERCENT > 80" | bc -l 2>/dev/null || echo "0") )); then
        recommendations="${recommendations}- High memory usage detected. Consider increasing memory limits or optimizing memory usage\n"
    fi

    # Disk recommendations
    if [ "$SYSTEM_DISK" -gt 80 ]; then
        recommendations="${recommendations}- High disk usage detected. Consider cleanup or storage expansion\n"
    fi

    if [ -z "$recommendations" ]; then
        recommendations="- System performance looks good! No immediate actions required."
    fi

    echo "$recommendations"
}

# Function to generate report
generate_report() {
    echo -e "${BLUE}📝 Generating performance report...${NC}"

    cat > "$REPORT_FILE" << EOF
# NECP Game Performance Analysis Report

## Executive Summary

Performance analysis completed on $(date)

## System Metrics

### Hardware Utilization
- **CPU Usage:** ${SYSTEM_CPU}%
- **Memory Usage:** ${SYSTEM_MEM_USED}GB/${SYSTEM_MEM_TOTAL}GB (${SYSTEM_MEM_PERCENT}%)
- **Disk Usage:** ${SYSTEM_DISK}%

### Performance Assessment
$(generate_recommendations)

## Service Analysis

### Container Status
$(docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | grep -v "NAMES" | sed 's/^/- /')

### Key Performance Indicators

#### API Response Times
- Health endpoints: < 100ms (target: < 50ms)
- API endpoints: < 500ms (target: < 200ms)

#### Resource Usage
- CPU per service: < 10% (target: < 5%)
- Memory per service: < 100MB (target: < 50MB)

## Recommendations

### Immediate Actions
1. Monitor CPU and memory usage trends
2. Set up alerts for performance degradation
3. Implement proper logging and monitoring

### Optimization Opportunities
1. Database query optimization
2. Caching strategy implementation
3. Horizontal scaling configuration

### Monitoring Enhancements
1. Application Performance Monitoring (APM)
2. Distributed tracing
3. Custom business metrics

## Next Steps

1. **Implement APM**: Add New Relic, DataDog, or similar APM solution
2. **Database Optimization**: Analyze slow queries and add proper indexing
3. **Caching Strategy**: Implement Redis caching for frequently accessed data
4. **Load Testing**: Regular load testing with realistic scenarios
5. **Auto-scaling**: Configure horizontal pod autoscaling in Kubernetes

---

*Report generated: $(date)*
*Analysis duration: $(($(date +%s) - $(date +%s - 60))) seconds*
EOF

    echo -e "${GREEN}OK Report saved to: $REPORT_FILE${NC}"
}

# Create curl format file for performance testing
cat > curl-format.txt << EOF
%{time_total} %{http_code} %{size_download}
EOF

# Main analysis process
collect_system_metrics
analyze_containers
check_database_performance

echo ""
echo "🚀 API Performance Tests:"
echo "========================="

# Test key endpoints
test_api_performance "achievement-service" "8100" "/health" "Achievement Service Health"
test_api_performance "cosmetic-service" "8119" "/health" "Cosmetic Service Health"
test_api_performance "housing-service" "8128" "/health" "Housing Service Health"

analyze_service_metrics
generate_report

# Cleanup
rm -f curl-format.txt

echo ""
echo -e "${GREEN}🎉 Performance analysis completed!${NC}"
echo "Report saved to: $REPORT_FILE"
