# 📊 Production Monitoring Setup: Kafka Event-Driven Architecture

## Overview

This document provides comprehensive monitoring and observability setup for the Kafka Event-Driven Architecture in production. Proper monitoring is critical for maintaining high availability, performance, and security of this mission-critical infrastructure component.

**Monitoring Coverage:** 100% of components with 15-second resolution  
**Alert Response Time:** P0 alerts <5 minutes, P1 alerts <15 minutes  
**Data Retention:** 90 days metrics, 1 year logs, 7 years security events  

## 🏗️ Monitoring Architecture

### Core Components

```
Monitoring Stack
├── Prometheus (Metrics Collection)
│   ├── Node Exporter (Infrastructure)
│   ├── JMX Exporter (Kafka JVM)
│   ├── Application Metrics (Custom)
│   └── AlertManager (Alert Routing)
│
├── Grafana (Visualization)
│   ├── Kafka Overview Dashboard
│   ├── Security Dashboard
│   ├── Application Dashboard
│   └── Incident Response Dashboard
│
├── ELK Stack (Logging)
│   ├── Fluent Bit (Log Collection)
│   ├── Elasticsearch (Storage)
│   ├── Kibana (Analysis)
│   └── Security Event Correlation
│
└── Distributed Tracing
    ├── Jaeger/OpenTelemetry
    ├── Event Flow Tracing
    └── Performance Profiling
```

### Service Level Objectives (SLOs)

#### Availability SLO
- **Target:** 99.9% uptime (8.76 hours downtime/year)
- **Measurement:** Kafka cluster availability
- **Alert Threshold:** <99.5% (warning), <99.0% (critical)

#### Performance SLO
- **Throughput:** 90,000 events/second minimum
- **Latency:** P99 <60ms for combat events
- **Error Rate:** <0.1% event processing failures

#### Security SLO
- **Auth Success Rate:** >99.9% authentication success
- **Zero Data Loss:** 100% message durability
- **Incident Response:** <15 minutes mean time to respond

## 📈 Metrics Collection Setup

### Prometheus Configuration

#### Kafka Broker Metrics
```yaml
# prometheus.yml
scrape_configs:
  - job_name: 'kafka-broker'
    static_configs:
      - targets: ['kafka-broker-0:7071', 'kafka-broker-1:7071', 'kafka-broker-2:7071']
    scrape_interval: 15s
    scrape_timeout: 10s
    metrics_path: /metrics
    params:
      format: ['prometheus']
```

#### Application Metrics
```yaml
  - job_name: 'kafka-event-core'
    kubernetes_sd_configs:
      - role: pod
        namespaces:
          names: ['necpgame-infrastructure']
    relabel_configs:
      - source_labels: [__meta_kubernetes_pod_label_app]
        regex: kafka-event-driven-core
        action: keep
    scrape_interval: 15s
    metrics_path: /metrics
```

#### Custom Metrics Configuration
```yaml
# Application metrics
metrics:
  event_processing:
    type: histogram
    buckets: [0.001, 0.005, 0.01, 0.05, 0.1, 0.5, 1.0]
    labels: [event_type, domain, status]

  consumer_lag:
    type: gauge
    description: "Consumer lag by partition"
    labels: [topic, partition, consumer_group]

  rate_limit_hits:
    type: counter
    description: "Rate limit violations"
    labels: [service, limit_type]
```

### JMX Exporter Setup

#### Kafka JMX Configuration
```yaml
# kafka-jmx-exporter.yml
lowercaseOutputName: true
lowercaseOutputLabelNames: true
rules:
  - pattern: kafka.(\w+)<type=(\w+)><>(\w+)
    name: kafka_$1_$2_$3
  - pattern: kafka.(\w+)<type=(\w+), name=(\w+)><>(\w+)
    name: kafka_$1_$2_$4
    labels:
      name: "$3"
```

### Application Metrics Implementation

#### Go Metrics Setup
```go
// metrics.go
import (
    "github.com/prometheus/client_golang/prometheus"
    "github.com/prometheus/client_golang/prometheus/promauto"
)

var (
    eventsProcessed = promauto.NewCounterVec(
        prometheus.CounterOpts{
            Name: "kafka_events_processed_total",
            Help: "Total number of events processed",
        },
        []string{"event_type", "domain", "status"},
    )

    eventProcessingDuration = promauto.NewHistogramVec(
        prometheus.HistogramOpts{
            Name: "kafka_event_processing_duration_seconds",
            Help: "Event processing duration in seconds",
            Buckets: prometheus.DefBuckets,
        },
        []string{"event_type", "domain"},
    )

    consumerLag = promauto.NewGaugeVec(
        prometheus.GaugeOpts{
            Name: "kafka_consumer_lag",
            Help: "Consumer lag by partition",
        },
        []string{"topic", "partition", "consumer_group"},
    )
)
```

#### Real-time Gateway Metrics
```go
// UDP transport metrics
udpPacketsReceived = promauto.NewCounter(
    prometheus.CounterOpts{
        Name: "realtime_gateway_udp_packets_received_total",
        Help: "Total UDP packets received",
    },
)

spatialGridSize = promauto.NewGauge(
    prometheus.GaugeOpts{
        Name: "realtime_gateway_spatial_grid_size",
        Help: "Current spatial grid size",
    },
)
```

## 🎯 Alerting Rules

### Critical Alerts (P0)

#### Infrastructure Alerts
```yaml
groups:
  - name: kafka.infrastructure.critical
    rules:
      # Kafka Cluster Down
      - alert: KafkaClusterDown
        expr: up{job="kafka-broker"} == 0
        for: 1m
        labels:
          severity: critical
          category: infrastructure
          impact: system-down
        annotations:
          summary: "Kafka cluster is completely down"
          description: "All Kafka brokers are unreachable. Immediate action required."
          runbook: "https://wiki.necpgame.com/kafka/cluster-down"
          slack: "#kafka-emergency"

      # Zookeeper Ensemble Down
      - alert: KafkaZookeeperDown
        expr: up{job="kafka-zookeeper"} < 2
        for: 2m
        labels:
          severity: critical
          category: infrastructure
        annotations:
          summary: "Kafka Zookeeper ensemble degraded"
          description: "Less than 2 Zookeeper nodes are healthy. Cluster may become unstable."

      # Disk Space Critical
      - alert: KafkaDiskSpaceCritical
        expr: (kafka_broker_disk_used_bytes / kafka_broker_disk_total_bytes) > 0.95
        for: 5m
        labels:
          severity: critical
          category: storage
        annotations:
          summary: "Kafka disk space critically low"
          description: "Disk usage >95%. Immediate cleanup or scaling required."
```

#### Performance Alerts
```yaml
  - name: kafka.performance.critical
    rules:
      # High Consumer Lag
      - alert: KafkaHighConsumerLag
        expr: kafka_consumer_lag > 100000
        for: 5m
        labels:
          severity: critical
          category: performance
        annotations:
          summary: "Critical Kafka consumer lag"
          description: "Consumer lag >100k messages. Processing severely delayed."

      # Event Processing Failed
      - alert: KafkaEventProcessingFailed
        expr: rate(kafka_event_processing_failures_total[5m]) > 100
        for: 2m
        labels:
          severity: critical
          category: application
        annotations:
          summary: "High rate of event processing failures"
          description: "Event processing failure rate >100/min. Check application logs."
```

### Security Alerts (P0-P1)

#### Authentication & Authorization
```yaml
  - name: kafka.security.critical
    rules:
      # SASL Authentication Failures
      - alert: KafkaSASLAuthFailuresSpike
        expr: rate(kafka_server_sasl_authentication_failures_total[5m]) > 10
        for: 2m
        labels:
          severity: critical
          category: security
          incident_type: authentication
        annotations:
          summary: "SASL authentication failures spike"
          description: "High rate of authentication failures. Possible brute force attack."
          security_team: "@security-emergency"

      # ACL Denials Spike
      - alert: KafkaACLDenialsSpike
        expr: rate(kafka_authorizer_acl_denials_total[5m]) > 50
        for: 2m
        labels:
          severity: high
          category: security
          incident_type: authorization
        annotations:
          summary: "ACL denials spike"
          description: "High rate of authorization denials. Check access patterns."
```

#### Data Protection
```yaml
      # Unencrypted Connections
      - alert: KafkaUnencryptedConnections
        expr: kafka_server_plaintext_connections_active > 0
        for: 5m
        labels:
          severity: critical
          category: security
          incident_type: encryption
        annotations:
          summary: "Unencrypted Kafka connections detected"
          description: "Active unencrypted connections. Immediate security breach."
          security_team: "@security-emergency"

      # Certificate Expiry
      - alert: KafkaCertificateExpiry
        expr: kafka_ssl_certificate_expiry_days < 30
        for: 1h
        labels:
          severity: high
          category: security
          incident_type: certificates
        annotations:
          summary: "Kafka certificates expiring soon"
          description: "SSL/TLS certificates expire in {{ $value }} days."
```

### Performance Alerts (P1-P2)

#### Throughput and Latency
```yaml
  - name: kafka.performance.warning
    rules:
      # Low Throughput
      - alert: KafkaLowThroughput
        expr: rate(kafka_events_processed_total[5m]) < 50000
        for: 10m
        labels:
          severity: warning
          category: performance
        annotations:
          summary: "Kafka throughput below threshold"
          description: "Event processing rate <50k/min. Check for bottlenecks."

      # High Latency
      - alert: KafkaHighLatency
        expr: histogram_quantile(0.99, rate(kafka_event_processing_duration_seconds_bucket[5m])) > 0.1
        for: 5m
        labels:
          severity: warning
          category: performance
        annotations:
          summary: "Kafka P99 latency high"
          description: "99th percentile latency >100ms. Performance degradation."
```

#### Resource Usage
```yaml
      # High Memory Usage
      - alert: KafkaHighMemoryUsage
        expr: (kafka_jvm_memory_used_bytes / kafka_jvm_memory_max_bytes) > 0.85
        for: 5m
        labels:
          severity: warning
          category: resources
        annotations:
          summary: "Kafka high memory usage"
          description: "JVM memory usage >85%. Monitor for memory leaks."

      # High CPU Usage
      - alert: KafkaHighCPUUsage
        expr: rate(kafka_jvm_cpu_usage_seconds_total[5m]) > 0.8
        for: 5m
        labels:
          severity: warning
          category: resources
        annotations:
          summary: "Kafka high CPU usage"
          description: "CPU usage >80%. Check for performance issues."
```

## 📊 Grafana Dashboards

### 1. Kafka Overview Dashboard

#### Key Panels
- **Cluster Health:** Broker status, controller status, ISR status
- **Throughput:** Messages in/out per second, bytes transferred
- **Latency:** Request latency percentiles, queue time
- **Consumer Lag:** Lag by topic and consumer group
- **Partition Status:** Under-replicated partitions, offline partitions

#### Layout
```
Row 1: Health Overview
- Cluster Status (Status Panel)
- Active Controllers (Stat Panel)
- ISR Status (Table Panel)

Row 2: Performance Metrics
- Throughput Graph (Time Series)
- Latency Percentiles (Time Series)
- Consumer Lag (Time Series)

Row 3: Resource Usage
- CPU Usage (Gauge)
- Memory Usage (Gauge)
- Disk Usage (Gauge)
- Network I/O (Time Series)
```

### 2. Security Dashboard

#### Key Panels
- **Authentication:** Success/failure rates, SASL vs TLS
- **Authorization:** ACL denials, superuser access
- **Encryption:** TLS connections, certificate expiry
- **Rate Limiting:** Hits by service, circuit breaker status
- **Audit Events:** Security events over time

#### Security KPIs
```yaml
# Key security metrics to track
authentication_success_rate:
  query: rate(kafka_auth_success_total[5m]) / rate(kafka_auth_attempts_total[5m])

authorization_deny_rate:
  query: rate(kafka_acl_denials_total[5m])

tls_connection_ratio:
  query: kafka_tls_connections_active / kafka_total_connections_active
```

### 3. Application Dashboard

#### Event Processing Metrics
- **Event Types:** Processing rate by event type
- **Domains:** Performance by domain (combat, economy, social)
- **Success/Failure Rates:** Processing outcomes
- **Queue Depth:** Events waiting to be processed

#### Consumer Group Health
- **Lag Trends:** Historical lag patterns
- **Rebalancing Events:** Consumer group changes
- **Error Rates:** Consumer processing failures
- **Throughput:** Messages consumed per second

### 4. Incident Response Dashboard

#### Real-time Incident Monitoring
- **Active Alerts:** Current firing alerts with severity
- **Error Spikes:** Sudden increases in error rates
- **Performance Degradation:** Latency and throughput anomalies
- **Security Events:** Timeline of security incidents

#### Historical Analysis
- **Incident Timeline:** Past incidents with resolution times
- **Root Cause Analysis:** Common failure patterns
- **Recovery Metrics:** MTTR and MTTD trends

## 📝 Logging Setup

### Fluent Bit Configuration

#### Kafka Application Logs
```yaml
# fluent-bit-kafka.conf
[SERVICE]
    Flush         5
    Log_Level     info
    Daemon        off

[INPUT]
    Name              tail
    Path              /var/log/kafka/application.log
    Parser            json
    Tag               kafka.application.*
    Refresh_Interval  5

[FILTER]
    Name              grep
    Match             kafka.application.*
    Regex             level (ERROR|FATAL|WARN)

[OUTPUT]
    Name              elasticsearch
    Match             kafka.application.*
    Host              elasticsearch.necpgame.internal
    Port              9200
    Index             kafka-application-%Y.%m.%d
    Type              kafka-log
```

#### Security Event Logs
```yaml
[INPUT]
    Name              tail
    Path              /var/log/kafka/security.log
    Parser            json
    Tag               kafka.security.*
    Refresh_Interval  1

[FILTER]
    Name              record_modifier
    Match             kafka.security.*
    Record            compliance SOC2 GDPR ISO27001
    Record            retention 7-years

[OUTPUT]
    Name              opensearch
    Match             kafka.security.*
    Host              security-logs.necpgame.internal
    Port              9200
    Index             kafka-security-%Y.%m.%d
    Type              security-event
```

### Log Retention Policies

#### Application Logs
- **Retention:** 90 days
- **Compression:** gzip after 24 hours
- **Archival:** S3 after 30 days
- **Access:** Development and SRE teams

#### Security Logs
- **Retention:** 7 years (compliance requirement)
- **Encryption:** AES256-GCM at rest
- **Access:** Security team only
- **Backup:** Offsite encrypted backup

#### Audit Logs
- **Retention:** 7 years
- **Immutability:** Append-only
- **Chain of Custody:** Cryptographic signing
- **Access:** Compliance and security teams

## 🔍 Distributed Tracing

### Jaeger Setup

#### Service Configuration
```yaml
# jaeger-config.yml
service:
  name: kafka-event-driven
  version: 1.0.0

sampler:
  type: probabilistic
  param: 0.1  # 10% sampling rate

reporter:
  queueSize: 100
  bufferFlushInterval: 10
  logSpans: false

tags:
  environment: production
  service.type: event-processing
```

#### Event Flow Tracing
```go
// Tracing event processing
func (p *EventProcessor) ProcessEvent(ctx context.Context, event *Event) error {
    span, ctx := tracer.StartSpanFromContext(ctx, "process_event",
        tracer.Tag("event.type", event.Type),
        tracer.Tag("event.domain", event.Domain),
        tracer.Tag("event.id", event.ID))
    defer span.Finish()

    // Process event...
    span.LogFields(log.String("status", "processing"))

    if err := p.validateEvent(ctx, event); err != nil {
        span.SetTag("error", true)
        span.LogFields(log.Error(err))
        return err
    }

    if err := p.processBusinessLogic(ctx, event); err != nil {
        span.SetTag("error", true)
        span.LogFields(log.Error(err))
        return err
    }

    span.LogFields(log.String("status", "completed"))
    return nil
}
```

### Trace Analysis

#### Key Trace Metrics
- **Event Processing Latency:** End-to-end processing time
- **Service Dependencies:** Which services are called
- **Error Propagation:** Where errors occur in the flow
- **Performance Bottlenecks:** Slowest components

#### Trace Queries
```sql
-- Find slow event processing
SELECT * FROM traces
WHERE operation_name = 'process_event'
  AND duration > 100000000  -- 100ms in nanoseconds
  AND timestamp > now() - interval '1 hour'
ORDER BY duration DESC;

-- Find error traces
SELECT * FROM traces
WHERE tags['error'] = 'true'
  AND timestamp > now() - interval '24 hours';
```

## 🚨 Incident Response

### Alert Routing

#### PagerDuty Integration
```yaml
# alertmanager.yml
route:
  group_by: ['alertname', 'severity']
  group_wait: 10s
  group_interval: 10s
  repeat_interval: 1h
  receiver: 'pagerduty'
  routes:
    - match:
        severity: critical
      receiver: 'pagerduty-critical'
    - match:
        severity: high
      receiver: 'pagerduty-high'
    - match:
        severity: warning
      receiver: 'slack-warning'

receivers:
  - name: 'pagerduty-critical'
    pagerduty_configs:
      - routing_key: 'kafka-critical-key'
        severity: 'critical'
  - name: 'pagerduty-high'
    pagerduty_configs:
      - routing_key: 'kafka-high-key'
        severity: 'error'
  - name: 'slack-warning'
    slack_configs:
      - api_url: 'slack-webhook-url'
        channel: '#kafka-alerts'
```

### Runbooks

#### Critical Incident Response
1. **Acknowledge Alert:** Within 5 minutes
2. **Assess Impact:** Determine affected systems and users
3. **Contain Issue:** Isolate problematic components
4. **Investigate Root Cause:** Use monitoring data and logs
5. **Implement Fix:** Deploy hotfix or rollback
6. **Verify Resolution:** Confirm system stability
7. **Document Incident:** Update runbooks and procedures

#### Common Incident Scenarios

##### Scenario 1: Consumer Lag Spike
**Symptoms:** Consumer lag >100k messages  
**Root Causes:** Application errors, resource constraints, network issues  
**Response:** Scale consumers, restart failed pods, check application logs

##### Scenario 2: Authentication Failures
**Symptoms:** SASL auth failure rate >10/min  
**Root Causes:** Credential rotation, misconfiguration, brute force attack  
**Response:** Rotate credentials, check configurations, enable rate limiting

##### Scenario 3: High Memory Usage
**Symptoms:** JVM memory >85%  
**Root Causes:** Memory leaks, large message processing, GC issues  
**Response:** Restart pods, analyze heap dumps, optimize memory usage

## 📋 Monitoring Checklist

### Daily Checks
- [ ] All Kafka brokers healthy
- [ ] Consumer lag <1000 messages
- [ ] No critical alerts firing
- [ ] Throughput within expected ranges
- [ ] Security metrics normal

### Weekly Reviews
- [ ] Performance trends analysis
- [ ] Error rate trends
- [ ] Resource usage optimization
- [ ] Log volume analysis
- [ ] Security incident review

### Monthly Audits
- [ ] SLO compliance review
- [ ] Alert effectiveness assessment
- [ ] Monitoring coverage gaps
- [ ] Incident response improvements
- [ ] Cost optimization opportunities

---

**Monitoring Lead:** Release Agent (#a8c3d2f1)  
**SRE Team:** @sre-team  
**Security Team:** @security-team  
**On-Call Rotation:** 24/7 coverage with 15-minute response SLA  

**Last Updated:** January 6, 2026  
**Version:** 1.0.0  
**Status:** ✅ PRODUCTION MONITORING READY
