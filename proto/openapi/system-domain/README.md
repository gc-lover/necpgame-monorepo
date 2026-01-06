# System Domain - Infrastructure Services

## Overview

The `system-domain` directory contains infrastructure services that provide core system functionality for the NECPGAME platform. These services are organized into three main categories:

## Directory Structure

```
system-domain/
├── ai/                    # Artificial Intelligence services
│   ├── ai-behavior-service/
│   ├── ai-companion-service/
│   └── machine-learning-service/
├── monitoring/           # Analytics and monitoring services
│   ├── analytics-service/
│   ├── behavioral-data-service/
│   ├── game-metrics-service/
│   ├── performance-monitoring-service/
│   └── player-analytics-service/
└── networking/           # Network and communication services
    ├── network-infrastructure-service/
    ├── neural-link-service/
    ├── notification-service/
    ├── push-notification-service/
    └── web-rtc-service/
```

## Service Categories

### AI Services (`ai/`)
- **AI Behavior Service**: NPC behavior patterns and decision making
- **AI Companion Service**: AI companions and assistants
- **Machine Learning Service**: ML model training and inference

### Monitoring Services (`monitoring/`)
- **Analytics Service**: Comprehensive game analytics (26+ files)
- **Behavioral Data Service**: Player behavior tracking
- **Game Metrics Service**: Real-time game performance metrics
- **Performance Monitoring Service**: System performance monitoring
- **Player Analytics Service**: Player engagement and progression analytics

### Networking Services (`networking/`)
- **Network Infrastructure Service**: Core networking infrastructure
- **Neural Link Service**: Neural interface connectivity
- **Notification Service**: In-game notifications
- **Push Notification Service**: External push notifications
- **WebRTC Service**: Real-time communication

## Migration Status

This directory structure was created as part of the system-domain refactor to organize infrastructure services by their functional domains. Services are gradually being migrated from the root `proto/openapi/` directory to this organized structure.

## Total Files
- **467 files** across all system-domain services
- Largest service: `analytics-service` (26 files)
- Most complex service: `relationship-service` (46 files) - still in root

## Next Steps
1. Complete migration of all services to appropriate subdirectories
2. Update all references in knowledge base and documentation
3. Update build scripts and CI/CD pipelines
4. Validate all API specifications after migration

## Performance Requirements
- All services must maintain <50ms P99 latency
- Monitoring services require <1GB memory per service
- AI services support concurrent ML model inference
- Networking services handle 1000+ concurrent connections
