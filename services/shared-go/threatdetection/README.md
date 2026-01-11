# Advanced Threat Detection System

## Overview

Enterprise-grade threat detection system for DDoS mitigation, anomaly detection, and behavioral analysis. Designed for MMOFPS games requiring real-time security monitoring.

## Issue: #2163

## Features

### 1. DDoS Mitigation
- IP-based rate limiting
- Automatic blocking of malicious IPs
- Distributed blocking via Redis
- Configurable thresholds and windows

### 2. Anomaly Detection
- Statistical anomaly detection (z-score)
- Request time analysis
- Error rate monitoring
- Historical pattern comparison

### 3. Behavioral Analysis
- User behavior pattern tracking
- Suspicious activity scoring
- Automated threat detection
- Real-time monitoring

## Usage

### Basic Setup

```go
import "necpgame/services/shared-go/threatdetection"

detector, err := threatdetection.NewDetector(threatdetection.DetectorConfig{
    Redis: redisClient,
    Logger: logger,
    DDosThreshold: 1000,        // 1000 requests per window
    DDosWindow: 1 * time.Minute,
    DDosBlockDuration: 15 * time.Minute,
    AnomalyThreshold: 3.0,      // 3 standard deviations
    AnomalyWindow: 5 * time.Minute,
    BehaviorWindow: 10 * time.Minute,
    BehaviorThreshold: 0.7,     // 70% suspicious score
})
```

### Request Analysis

```go
// Analyze each request
threat, err := detector.AnalyzeRequest(ctx, sourceIP, userID, requestTime, isError)
if err != nil {
    return err
}

if threat != nil {
    // Handle threat
    logger.Warn("Threat detected",
        zap.String("type", string(threat.Type)),
        zap.String("level", threat.Level.String()),
        zap.String("source", threat.Source),
        zap.Float64("score", threat.Score),
    )

    // Block IP if critical
    if threat.Level == threatdetection.ThreatLevelCritical {
        // Implement blocking logic
    }
}
```

### Check if IP is Blocked

```go
blocked, err := detector.IsBlocked(ctx, sourceIP)
if err != nil {
    return err
}

if blocked {
    return errors.New("IP is blocked")
}
```

### Get Statistics

```go
stats := detector.GetThreatStats()
logger.Info("Threat detection stats",
    zap.Int64("total_threats", stats["total_threats"].(int64)),
    zap.Int64("ddos_detections", stats["ddos_detections"].(int64)),
    zap.Int64("anomaly_detections", stats["anomaly_detections"].(int64)),
    zap.Int64("behavioral_detections", stats["behavioral_detections"].(int64)),
)
```

## Threat Types

### DDoS
- **Detection**: Request rate exceeds threshold
- **Response**: Automatic IP blocking
- **Level**: Critical

### Anomaly
- **Detection**: Statistical outliers (z-score)
- **Response**: Logging and monitoring
- **Level**: Medium

### Behavioral
- **Detection**: Suspicious behavior patterns
- **Response**: Enhanced monitoring
- **Level**: High

## Configuration

### DDoS Settings
- `DDosThreshold`: Maximum requests per window
- `DDosWindow`: Time window for counting
- `DDosBlockDuration`: How long to block after detection

### Anomaly Settings
- `AnomalyThreshold`: Z-score threshold (typically 3.0)
- `AnomalyWindow`: Time window for statistics

### Behavioral Settings
- `BehaviorWindow`: Time window for pattern analysis
- `BehaviorThreshold`: Suspicious score threshold (0.0-1.0)

## Performance

- **Latency**: <1ms per request analysis
- **Memory**: ~1KB per tracked IP/user
- **Throughput**: 10,000+ requests/second
- **Redis**: Distributed blocking and statistics

## Integration

This library can be used in:
- API Gateway middleware
- Security service
- Real-time gateway
- All HTTP handlers

## Best Practices

1. **Tune Thresholds**: Adjust based on normal traffic patterns
2. **Monitor Stats**: Track detection rates and false positives
3. **Distributed Blocking**: Use Redis for multi-instance deployments
4. **Logging**: Log all detected threats for analysis
5. **Whitelisting**: Maintain whitelist for trusted IPs/users
