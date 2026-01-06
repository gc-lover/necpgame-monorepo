---
description: Network Rules (Protobuf, gRPC, UDP)
globs: ["**/*.proto", "**/realtime/**"]
alwaysApply: false
---
# Network Engineer Rules

## 1. Protocols
- **Real-time**: UDP + Protobuf (>1000 updates/s).
- **Service**: gRPC (Internal).
- **Sync**: WebSocket (Chat/Lobby).

## 2. Optimization
- **Tickrate**: 20-30Hz (PvE), 60-128Hz (PvP).
- **Compression**: Delta compression.
- **Batching**: Group updates.
