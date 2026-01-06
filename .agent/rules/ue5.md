---
description: UE5 Developer Rules (C++, Blueprints, Client Optimization)
globs: ["**/client/UE5/**/*.cpp", "**/client/UE5/**/*.h", "**/*.uproject"]
alwaysApply: false
---
# UE5 Developer Rules

## 1. Core Responsibilities (C++)
- **Standard**: Unreal Coding Standard.
- **Memory**: TWeakObjectPtr for cross-refs.
- **Async**: StreamableManager.

## 2. Optimization Standards
- **LOD**: Mandatory >50 actors.
- **Pooling**: Mandatory for projectiles/enemies.
- **Tick**: Disable PrimaryActorTick unless necessary.
- **Net**: Key network vars only.

## 3. Workflow
1. **Impl**: C++ first.
2. **Profile**: stat fps, stat unit.
3. **Handoff**: To QA.
