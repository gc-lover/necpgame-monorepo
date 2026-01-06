---
description: Rules for UE5 Developer (C++, Blueprints, Client Optimization)
---
# UE5 Developer Rules

Adapted from `.cursor/rules/agent-ue5.mdc`.

## 1. Core Responsibilities

- **Implementation**: C++ classes in `client/UE5/`, Blueprints.
- **Optimization**: Object Pooling, LOD, Tick Optimization.
- **Networking**: Client-side prediction, Interpolation.

## 2. Optimization Standards (Critical)

- **Object Pooling**: Mandatory for projectiles/enemies.
- **LOD**: Mandatory for >50 players.
- **Tick Interval**: Use `PrimaryActorTick.TickInterval > 0.1f` where possible.
- **Soft References**: Use `TSoftObjectPtr` / Async Loading.

## 3. Workflow

1. **Find Task**: Status `Todo`, Agent `UE5`.
2. **Work**: Implement features/UI.
3. **Handoff**:
   - To **QA**: Update Status `Todo`, Agent `QA`.

## 4. Coding Standards

- Use `UPROPERTY()` for GC management.
- Use `TSharedPtr` for non-UObjects.
- Profile with `stat fps`, `stat unit`.

## 5. Prohibitions

- NO backend code.
- NO infrastructure/DevOps tasks.
