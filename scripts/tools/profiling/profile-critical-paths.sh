#!/bin/bash
# CPU Profiling and Flame Graph Generation for Critical Paths
# Issue: #1974
# Usage: ./profile-critical-paths.sh <service-name> [duration-seconds] [pprof-port]

set -e

SERVICE=${1:-"default"}
DURATION=${2:-60}
PPROF_PORT=${3:-6060}
OUTPUT_DIR="profiles"

# Create output directory
mkdir -p "$OUTPUT_DIR"

echo "=========================================="
echo "CPU Profiling for Critical Paths"
echo "Service: $SERVICE"
echo "Duration: ${DURATION}s"
echo "pprof Port: $PPROF_PORT"
echo "=========================================="

# Check if pprof endpoint is available
if ! curl -s "http://localhost:$PPROF_PORT/debug/pprof/" > /dev/null; then
    echo "ERROR: pprof endpoint not available at http://localhost:$PPROF_PORT"
    echo "Make sure service is running and pprof is enabled"
    exit 1
fi

TIMESTAMP=$(date +%Y%m%d_%H%M%S)
PROFILE_FILE="$OUTPUT_DIR/cpu_${SERVICE}_${TIMESTAMP}.prof"
FLAME_SVG="$OUTPUT_DIR/flame_${SERVICE}_${TIMESTAMP}.svg"
TOP_TXT="$OUTPUT_DIR/top_${SERVICE}_${TIMESTAMP}.txt"

echo ""
echo "Step 1: Collecting CPU profile..."
curl -s "http://localhost:$PPROF_PORT/debug/pprof/profile?seconds=$DURATION" > "$PROFILE_FILE"

if [ ! -s "$PROFILE_FILE" ]; then
    echo "ERROR: Failed to collect CPU profile"
    exit 1
fi

echo "  Profile saved: $PROFILE_FILE"

echo ""
echo "Step 2: Generating flame graph..."
go tool pprof -svg "$PROFILE_FILE" > "$FLAME_SVG" 2>/dev/null || {
    echo "  Warning: SVG generation failed, using web UI instead"
    echo "  Run: go tool pprof -http=:8080 $PROFILE_FILE"
}

if [ -f "$FLAME_SVG" ]; then
    echo "  Flame graph saved: $FLAME_SVG"
fi

echo ""
echo "Step 3: Analyzing top functions..."
go tool pprof -top -cum "$PROFILE_FILE" > "$TOP_TXT" 2>/dev/null || {
    echo "  Warning: Top functions analysis failed"
}

if [ -f "$TOP_TXT" ]; then
    echo "  Top functions saved: $TOP_TXT"
    echo ""
    echo "Top 10 functions by cumulative time:"
    head -15 "$TOP_TXT" | tail -10
fi

echo ""
echo "Step 4: Generating report..."
REPORT_FILE="$OUTPUT_DIR/report_${SERVICE}_${TIMESTAMP}.md"
cat > "$REPORT_FILE" << EOF
# CPU Profiling Report

**Service:** $SERVICE  
**Date:** $(date)  
**Duration:** ${DURATION}s  
**Profile:** $PROFILE_FILE

## Top Functions

\`\`\`
$(head -20 "$TOP_TXT" 2>/dev/null || echo "Analysis not available")
\`\`\`

## Flame Graph

Flame graph: \`$FLAME_SVG\`

To view interactively:
\`\`\`bash
go tool pprof -http=:8080 $PROFILE_FILE
\`\`\`

## Recommendations

1. Review top functions for optimization opportunities
2. Check flame graph for wide bars (bottlenecks)
3. Compare with previous profiles for regressions
4. Focus on critical paths: game tick, player update, combat calc

## Next Steps

- Optimize top 3 functions by cumulative time
- Re-profile after optimizations
- Track improvements over time
EOF

echo "  Report saved: $REPORT_FILE"

echo ""
echo "=========================================="
echo "Profiling Complete"
echo "=========================================="
echo "Profile: $PROFILE_FILE"
echo "Flame Graph: $FLAME_SVG"
echo "Top Functions: $TOP_TXT"
echo "Report: $REPORT_FILE"
echo ""
echo "To view interactively:"
echo "  go tool pprof -http=:8080 $PROFILE_FILE"
echo ""
