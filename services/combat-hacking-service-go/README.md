# Combat Hacking Service Go

Enterprise-Grade Combat Hacking Service for NECPGAME MMOFPS RPG.

**Domain:** specialized-domain
**Module:** combat-hacking-service-go

## Features

- **Real-time hacking operations** (1000+ RPS)
- **Network infiltration** and ICE bypass
- **Cyber combat effects** and counter-hacking
- **Anti-cheat validation** for hacking attempts
- **Device and infrastructure hacking**
- **Implant manipulation** and cyberware control

## Skills Implemented

### 1. Screen Hack Blind (#143875347)
- **Endpoint:** `POST /hacking/screen-hack/blind`
- **Description:** Hack urban AR screens to create AoE blind zone
- **Parameters:** Player ID, screen position, skill level
- **Effects:** Blind zone with configurable radius and duration

### 2. Glitch Doubles (#143875814)
- **Endpoint:** `POST /hacking/glitch-doubles/activate`
- **Description:** Create phantom copies of player actions
- **Parameters:** Player ID, skill level, enemy count
- **Effects:** Multiple phantom entities confusing enemies

## Performance Targets

- **P99 Latency:** <10ms for hacking operations
- **Memory:** Zero allocations in hot path
- **Concurrent users:** 10,000+ simultaneous connections

## API Endpoints

- `GET /health` - Service health check
- `POST /hacking/screen-hack/blind` - Activate screen hack blind skill
- `POST /hacking/glitch-doubles/activate` - Activate glitch doubles skill

## Building and Running

```bash
# Build
make build

# Run
make run

# Run tests
make test

# Docker build
make docker-build

# Docker run
make docker-run
```

## Architecture

- **Handlers:** HTTP request/response handling
- **Models:** Data structures and DTOs
- **Repository:** In-memory storage with cleanup (production: use Redis/PostgreSQL)
- **Server:** HTTP server with graceful shutdown

## Issues

- **#143875347:** Screen Hack Blind skill implementation
- **#143875814:** Glitch Doubles skill implementation

## BACKEND OPTIMIZATION NOTES

- Struct field alignment optimized (large → small types)
- Memory layout optimized for cache efficiency
- Expected memory savings: 30-50% for hacking-related structs
