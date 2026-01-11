# CPU Profiling and Flame Graphs for Critical Paths

## Overview

Enterprise-grade CPU profiling and flame graph generation for identifying performance bottlenecks in critical paths. Designed for MMOFPS game servers requiring sub-10ms response times.

## Issue: #1974

## Quick Start

### 1. Enable pprof in Service

```go
import (
    _ "net/http/pprof"
    "net/http"
)

// In main.go
go func() {
    http.ListenAndServe(":6060", nil)
}()
```

### 2. Collect CPU Profile

```bash
# 30-second CPU profile
curl http://localhost:6060/debug/pprof/profile?seconds=30 > cpu.prof

# 60-second CPU profile for critical paths
curl http://localhost:6060/debug/pprof/profile?seconds=60 > cpu-critical.prof
```

### 3. Generate Flame Graph

```bash
# Install go-torch (if not available, use pprof web UI)
go install github.com/uber/go-torch

# Generate flame graph
go tool pprof -http=:8080 cpu.prof
# Open http://localhost:8080 in browser, click "Flame Graph"

# Or use pprof directly
go tool pprof -http=:8080 -sample_index=cpu cpu.prof
```

## Critical Paths Profiling

### Identifying Critical Paths

**MMOFPS Critical Paths:**
1. **Game Tick Loop** - <8ms target (128 Hz)
2. **Player Update** - <100μs per player
3. **Combat Calculation** - <1ms per hit
4. **Database Query** - <10ms P95
5. **API Response** - <50ms P99

### Profiling Critical Paths

```bash
# Profile during high load (simulate 1000 concurrent users)
# Terminal 1: Start load test
k6 run --vus 1000 --duration 60s load-test.js

# Terminal 2: Collect CPU profile during load
curl http://localhost:6060/debug/pprof/profile?seconds=60 > cpu-load.prof

# Terminal 3: Analyze
go tool pprof -http=:8080 cpu-load.prof
```

### Automated Critical Path Profiling

```bash
# Use profiling script
./scripts/tools/profiling/profile-critical-paths.sh combat-service 60

# Script will:
# 1. Start load test
# 2. Collect CPU profile
# 3. Generate flame graph
# 4. Analyze top functions
# 5. Generate report
```

## Flame Graph Generation

### Method 1: Using pprof Web UI

```bash
# Start pprof web server
go tool pprof -http=:8080 cpu.prof

# Navigate to http://localhost:8080
# Click "Flame Graph" button
# Export as SVG or PNG
```

### Method 2: Using go-torch

```bash
# Install go-torch
go install github.com/uber/go-torch

# Generate flame graph
go-torch cpu.prof

# Output: torch.svg
```

### Method 3: Using pprof CLI

```bash
# Generate SVG flame graph
go tool pprof -svg cpu.prof > flame.svg

# Generate PNG flame graph
go tool pprof -png cpu.prof > flame.png
```

## Analyzing Flame Graphs

### Reading Flame Graphs

1. **Width** = Time spent in function
2. **Height** = Call stack depth
3. **Color** = Function type (hot = red, cold = blue)

### Identifying Bottlenecks

**Red Flags:**
- Wide bars at top level = main bottleneck
- Deep stacks = many function calls
- Wide bars in hot paths = optimization target

**Example Analysis:**
```
Flame Graph shows:
- 40% time in database.Query() → Optimize queries
- 30% time in json.Marshal() → Use faster JSON library
- 20% time in regex.Match() → Pre-compile regex
- 10% time in other functions
```

## Performance Targets

### MMOFPS Targets

| Path | Target | Measurement |
|------|--------|-------------|
| Game Tick | <8ms | P99 latency |
| Player Update | <100μs | Per player |
| Combat Calc | <1ms | Per hit |
| DB Query | <10ms | P95 latency |
| API Response | <50ms | P99 latency |

### Profiling Strategy

1. **Baseline**: Profile current performance
2. **Identify**: Find bottlenecks in flame graph
3. **Optimize**: Fix top 3 bottlenecks
4. **Verify**: Re-profile to confirm improvement
5. **Repeat**: Until targets met

## Continuous Profiling

### Production Profiling

```go
// In main.go
import (
    "net/http"
    _ "net/http/pprof"
    "necpgame/services/shared-go/profiling"
)

// Start pprof server
pprofServer, err := profiling.NewPprofServer(profiling.PprofConfig{
    Addr:   ":6060",
    Logger: logger,
})
if err != nil {
    log.Fatal(err)
}

go func() {
    if err := pprofServer.Start(ctx); err != nil {
        log.Fatal(err)
    }
}()
```

### Scheduled Profiling

```bash
# Cron job: Profile every hour
0 * * * * curl http://localhost:6060/debug/pprof/profile?seconds=30 > /tmp/profiles/cpu-$(date +%s).prof

# Weekly analysis
0 0 * * 0 ./scripts/tools/profiling/analyze-weekly-profiles.sh
```

## Scripts

### profile-critical-paths.sh

```bash
#!/bin/bash
# Profile critical paths for a service
# Usage: ./profile-critical-paths.sh <service-name> <duration-seconds>

SERVICE=$1
DURATION=${2:-60}
PPROF_PORT=6060

echo "Profiling critical paths for $SERVICE..."

# Collect CPU profile
curl -s "http://localhost:$PPROF_PORT/debug/pprof/profile?seconds=$DURATION" > cpu-$SERVICE.prof

# Generate flame graph
go tool pprof -svg cpu-$SERVICE.prof > flame-$SERVICE.svg

# Analyze top functions
go tool pprof -top cpu-$SERVICE.prof > top-$SERVICE.txt

echo "Profile saved: cpu-$SERVICE.prof"
echo "Flame graph: flame-$SERVICE.svg"
echo "Top functions: top-$SERVICE.txt"
```

### analyze-flame-graph.sh

```bash
#!/bin/bash
# Analyze flame graph and generate report
# Usage: ./analyze-flame-graph.sh <profile-file>

PROFILE=$1

echo "Analyzing flame graph: $PROFILE"

# Top 10 functions
go tool pprof -top -cum $PROFILE | head -20

# Generate flame graph
go tool pprof -http=:8080 $PROFILE

echo "Open http://localhost:8080 to view flame graph"
```

## Integration with CI/CD

### Pre-deployment Profiling

```yaml
# .github/workflows/performance-check.yml
- name: CPU Profiling
  run: |
    # Start service
    ./services/my-service-go/main &
    sleep 5
    
    # Collect profile
    curl http://localhost:6060/debug/pprof/profile?seconds=30 > cpu.prof
    
    # Check for regressions
    go tool pprof -top cpu.prof | grep -E "(database|json|regex)" > regressions.txt
    
    # Fail if regressions found
    if [ -s regressions.txt ]; then
      echo "Performance regression detected"
      exit 1
    fi
```

## Best Practices

1. **Profile in Production**: Use production-like load
2. **Profile Critical Paths**: Focus on hot paths
3. **Regular Profiling**: Weekly or monthly
4. **Compare Baselines**: Track improvements over time
5. **Automate Analysis**: Use scripts for consistency

## Troubleshooting

### High Profiling Overhead

- Reduce profiling duration
- Use sampling profiler (default)
- Profile only during specific time windows

### Inaccurate Results

- Profile for longer duration (60s+)
- Profile during realistic load
- Use multiple profiles for average

### Flame Graph Not Showing

- Ensure pprof server is running
- Check profile file is valid
- Use `go tool pprof -http=:8080` for web UI

## References

- Performance Agent Rules: `.cursor/rules/agent-performance.mdc`
- Profiling Library: `services/shared-go/profiling/`
- pprof Documentation: https://pkg.go.dev/net/http/pprof
