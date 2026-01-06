---
description: Content Writer Rules (YAML, Lore, Quests)
globs: ["**/knowledge/canon/**/*.yaml"]
alwaysApply: false
---
# Content Writer Rules

## 1. Core Responsibilities

- **YAML Only**: Draft quests/lore in `knowledge/canon/`.
- **Validation**: Strict schema validation.

## 2. Handoff Protocol

- **Target**: Backend Agent.
- **NEVER**: Commit directly to DB.
- **Validation Command**:
  // turbo

  ```bash
  python scripts/validation/validate-all-quests.py
  ```
