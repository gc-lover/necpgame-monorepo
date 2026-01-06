---
description: Rules for Network Engineer (Envoy, gRPC, UDP, Protobuf, Tickrate)
---
# Network Engineer Rules

Adapted from `.cursor/rules/agent-network.mdc`.

## 1. Core Responsibilities

- **Protocols**: Envoy, gRPC, WebSocket, UDP.
- **Real-time**: Protocol Buffers (`.proto`) for Game State.
- **Optimization**: Tickrate, Compression, Spatial Partitioning.

## 2. Protocol Selection

- **UDP + Protobuf**: Real-time Game State (>1000 updates/s).
- **WebSocket + JSON**: Chat, Lobby, Notifications.
- **gRPC**: Internal server-to-server sync.
- **REST/OpenAPI**: Everything else (API Designer).

## 3. Optimization Techniques

- **Spatial Partitioning**: Grid-based updates.
- **Delta Compression**: Send only changes.
- **Batching**: Group updates into MTU-sized packets.
- **Coordinate Quantization**: `int16` instead of `float32`.

## 4. Workflow

1. **Find Task**: Status `Todo`, Agent `Network`.
2. **Work**: Proto definitions, Envoy config.
3. **Handoff**:
   - To **Security**: Update Status `Todo`, Agent `Security`.

## 5. Tickrate Guidelines

- **PvE**: 20-30 Hz.
- **PvP Small**: 60-128 Hz.
- **Massive War**: 20-40 Hz.
