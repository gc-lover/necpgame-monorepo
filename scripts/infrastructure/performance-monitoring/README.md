# MMOFPS Performance Monitoring System

This package provides comprehensive performance monitoring, alerting, and analysis for MMOFPS game services with real-time metrics collection and automated issue detection.

## Features

### 🚀 **Real-Time Performance Monitoring**
- **Game Session Tracking**: Active sessions, duration, drop rates, concurrent players
- **Combat Performance**: Response times, weapon switches, damage calculations, tick rates
- **Network Analytics**: Latency, packet loss, jitter, WebSocket connections, UDP hole punching
- **Database Performance**: Query duration, connection pools, cache hit rates
- **Memory & GC**: Heap allocations, goroutine counts, GC pause times
- **Business Metrics**: Player retention, error rates, average session length

### 📊 **Advanced Metrics Collection**
- **Prometheus Integration**: 25+ custom metrics with proper labels and buckets
- **Performance Baselines**: P95/P99 response times, throughput measurements
- **Resource Monitoring**: CPU, memory, network usage tracking
- **Custom Histograms**: Latency distributions, request duration analysis
- **Counter Metrics**: Events, errors, connections with dimensional data

### 🚨 **Intelligent Alerting System**
- **Multi-Level Alerts**: Info, Warning, Error, Critical severity levels
- **Smart Rules Engine**: Configurable thresholds with cooldown periods
- **Multiple Notifiers**: Slack, Discord, Email integration
- **Alert Acknowledgment**: Manual and automatic alert management
- **Alert Correlation**: Grouping related alerts, reducing noise

### 📈 **Real-Time Performance Analysis**
- **Automated Reports**: Comprehensive performance reports with recommendations
- **Trend Analysis**: Historical performance trends and anomaly detection
- **Service Metrics**: Per-service, per-endpoint performance breakdown
- **Network Diagnostics**: Region-specific latency and packet loss analysis
- **Capacity Planning**: Resource utilization trends and scaling recommendations

## Core Components

### PerformanceMonitor
Main monitoring engine that collects metrics from all game systems:

```go
monitor := monitoring.NewPerformanceMonitor(logger, "combat-service")

// Record game session events
monitor.RecordGameSession(monitoring.GameSession{
    SessionID:    "session_123",
    PlayerID:     "player_456",
    StartTime:    time.Now(),
    Region:       "us-west",
    GameMode:     "ranked",
    Ping:         45,
    FramesPerSecond: 144,
})

// Record combat performance
monitor.RecordCombatEvent(monitoring.CombatEvent{
    EventID:      "combat_789",
    PlayerID:     "player_456",
    EventType:    "kill",
    ResponseTime: 45 * time.Millisecond,
})

// Record network stats
monitor.RecordNetworkStats(monitoring.NetworkStats{
    Region:        "us-west",
    AvgLatency:    50 * time.Millisecond,
    PacketLossRate: 0.005,
    ActiveConnections: 1250,
})
```

### AlertManager
Intelligent alerting system with multiple notification channels:

```go
alerts := monitoring.NewAlertManager(logger)

// Add notification channels
alerts.AddNotifier(&monitoring.SlackNotifier{
    WebhookURL: "https://hooks.slack.com/...",
    Channel:    "#alerts",
})

// Add custom alert rules
alerts.AddRule(monitoring.AlertRule{
    ID:          "high_latency",
    Name:        "High Network Latency",
    Metric:      "network_latency",
    Condition:   monitoring.AlertCondition{Operator: "gt", Value: 0.1},
    Level:       monitoring.AlertLevelError,
    Cooldown:    5 * time.Minute,
})

// Manual alert triggering
alerts.TriggerAlert(
    monitoring.AlertLevelCritical,
    "Server Down",
    "Game server us-west-1 is not responding",
    "game-server",
    "health_check",
    0, 1, // value, threshold
    map[string]interface{}{"server": "us-west-1"},
)
```

### PerformanceAnalyzer
Real-time analysis engine that generates comprehensive reports:

```go
analyzer := monitoring.NewPerformanceAnalyzer(monitor, alerts, logger)

// Record performance data
analyzer.RecordResponseTime(monitoring.ResponseTimeSample{
    Timestamp:  time.Now(),
    Service:    "combat-service",
    Endpoint:   "/api/v1/combat/action",
    Method:     "POST",
    Duration:   45 * time.Millisecond,
    StatusCode: 200,
})

// Generate performance report
report := analyzer.GenerateReport(monitoring.TimeRange{
    Start: time.Now().Add(-time.Hour),
    End:   time.Now(),
})

fmt.Printf("Avg Response Time: %v\\n", report.Summary.AvgResponseTime)
fmt.Printf("Error Rate: %.2f%%\\n", report.Summary.ErrorRate*100)
```

## Key Metrics

### Game Session Metrics
```
mmofps_active_sessions_total{region="us-west", game_mode="ranked"} 1250
mmofps_session_duration_seconds{region="us-west", game_mode="ranked"} histogram
mmofps_concurrent_players{region="us-west"} 15432
mmofps_session_drop_rate{region="us-west"} 0.023
```

### Combat Performance Metrics
```
mmofps_combat_response_time_seconds{action_type="kill", region="global"} histogram
mmofps_weapon_switch_time_seconds{from_weapon="ak47", to_weapon="shotgun"} histogram
mmofps_damage_calculation_time_seconds{damage_type="bullet", weapon_type="rifle"} histogram
mmofps_combat_tick_rate_hz{region="us-west"} histogram
```

### Network Metrics
```
mmofps_network_latency_seconds{region="us-west", connection_type="websocket"} histogram
mmofps_packet_loss_rate{region="us-west", connection_type="websocket"} 0.005
mmofps_websocket_connections_total{region="us-west", connection_type="game"} 1250
mmofps_udp_hole_punch_success_total{region="us-west"} 98
```

### Database Metrics
```
mmofps_db_query_duration_seconds{query_type="select", table="player_stats"} histogram
mmofps_db_connection_pool_size{pool_type="main"} 25
mmofps_cache_hit_rate{cache_type="player_data"} 0.945
```

## Alert Rules

### Default Alert Rules
1. **High Combat Response Time**: >100ms for 1 minute (Warning)
2. **High Network Latency**: >50ms for 1 minute (Error)
3. **High Packet Loss**: >2% for 30 seconds (Critical)
4. **Low Cache Hit Rate**: <85% for 5 minutes (Warning)
5. **High Session Drop Rate**: >3% for 1 minute (Error)
6. **High Error Rate**: >5% for 1 minute (Error)
7. **Slow DB Query**: >100ms for 30 seconds (Warning)
8. **High Memory Usage**: >1GB for 1 minute (Warning)

### Custom Alert Rules
```go
// Create custom alert for matchmaking
alerts.AddRule(monitoring.AlertRule{
    ID:          "matchmaking_timeout",
    Name:        "Matchmaking Queue Timeout",
    Metric:      "matchmaking_queue_time",
    Condition:   monitoring.AlertCondition{Operator: "gt", Value: 300}, // 5 minutes
    Level:       monitoring.AlertLevelCritical,
    Cooldown:    10 * time.Minute,
    Service:     "matchmaking-service",
})
```

## Performance Reports

### Automated Report Generation
```json
{
  "generated_at": "2024-12-28T12:00:00Z",
  "time_range": {
    "start": "2024-12-28T11:00:00Z",
    "end": "2024-12-28T12:00:00Z"
  },
  "summary": {
    "avg_response_time": "45ms",
    "p95_response_time": "120ms",
    "p99_response_time": "250ms",
    "error_rate": 0.023,
    "active_users": 15432,
    "session_drop_rate": 0.012,
    "avg_network_latency": "35ms",
    "cache_hit_rate": 0.945
  },
  "service_metrics": {
    "combat-service": {
      "service_name": "combat-service",
      "avg_response_time": "42ms",
      "error_rate": 0.018,
      "request_count": 125430,
      "endpoint_metrics": {
        "POST /api/v1/combat/action": {
          "endpoint": "/api/v1/combat/action",
          "method": "POST",
          "request_count": 45230,
          "avg_response_time": "38ms",
          "error_rate": 0.015,
          "p95_response_time": "95ms"
        }
      }
    }
  },
  "network_metrics": {
    "regions": {
      "us-west": {
        "region": "us-west",
        "avg_latency": "32ms",
        "packet_loss": 0.004,
        "active_players": 4321
      }
    },
    "global_avg_latency": "35ms",
    "global_packet_loss": 0.005,
    "total_connections": 15432
  },
  "alerts": [
    {
      "level": "warning",
      "count": 3,
      "top_alerts": ["High Combat Response Time (2)", "Low Cache Hit Rate (1)"],
      "last_alert": "2024-12-28T11:45:00Z"
    }
  ],
  "recommendations": [
    "Consider implementing Redis caching for frequently accessed data",
    "Optimize database queries with proper indexing",
    "Implement connection pooling for database connections"
  ]
}
```

## Integration Examples

### Service Integration
```go
// In your service main.go
monitor := monitoring.NewPerformanceMonitor(logger, "your-service-name")
alerts := monitoring.NewAlertManager(logger)
analyzer := monitoring.NewPerformanceAnalyzer(monitor, alerts, logger)

// Add Slack notifications
alerts.AddNotifier(&monitoring.SlackNotifier{
    WebhookURL: os.Getenv("SLACK_WEBHOOK"),
    Channel:    "#alerts",
})

// Start monitoring
go func() {
    ticker := time.NewTicker(time.Minute)
    for range ticker.C {
        report := analyzer.GenerateReport(monitoring.TimeRange{
            Start: time.Now().Add(-time.Hour),
            End:   time.Now(),
        })
        // Send report to dashboard or log
    }
}()
```

### HTTP Handler Integration
```go
func (h *CombatHandler) HandleAction(w http.ResponseWriter, r *http.Request) {
    start := time.Now()
    requestID := middleware.GetReqID(r.Context())

    // Your business logic here
    result, err := h.service.ProcessCombatAction(r.Context(), action)

    duration := time.Since(start)

    // Record performance metrics
    h.monitor.RecordResponseTime(monitoring.ResponseTimeSample{
        Timestamp:  time.Now(),
        Service:    "combat-service",
        Endpoint:   "/api/v1/combat/action",
        Method:     r.Method,
        Duration:   duration,
        StatusCode: 200,
        UserID:     getUserID(r),
        RequestID:  requestID,
    })

    if err != nil {
        h.monitor.RecordCombatEvent(monitoring.CombatEvent{
            EventID:      requestID,
            PlayerID:     getUserID(r),
            EventType:    "error",
            ResponseTime: duration,
        })
        // Handle error...
    }

    // Check for performance issues
    if duration > 100*time.Millisecond {
        h.alerts.CheckMetric("combat-service", "response_time", "us-west", duration.Seconds())
    }
}
```

## Configuration

### Environment Variables
```bash
# Prometheus metrics
PROMETHEUS_PORT=9090

# Alert notifications
SLACK_WEBHOOK_URL=https://hooks.slack.com/...
DISCORD_WEBHOOK_URL=https://discord.com/api/webhooks/...

# Alert thresholds
ALERT_HIGH_LATENCY_MS=50
ALERT_HIGH_ERROR_RATE=0.05
ALERT_LOW_CACHE_HIT_RATE=0.85

# Monitoring settings
MONITOR_MAX_SAMPLES=10000
MONITOR_REPORT_INTERVAL=1h
```

## Performance Benefits

### Real-Time Issue Detection
- **Immediate Alerts**: Detect performance issues within seconds
- **Root Cause Analysis**: Correlated metrics and logs for faster debugging
- **Automated Remediation**: Trigger scaling or failover procedures

### Capacity Planning
- **Usage Trends**: Historical analysis for resource planning
- **Bottleneck Identification**: Pinpoint performance constraints
- **Scaling Recommendations**: Data-driven scaling decisions

### Operational Excellence
- **SLA Monitoring**: Ensure service level agreements are met
- **Incident Response**: Faster mean time to resolution (MTTR)
- **Proactive Maintenance**: Predict and prevent issues

This monitoring system provides enterprise-grade observability for high-performance MMOFPS game services, ensuring optimal player experience and system reliability.
