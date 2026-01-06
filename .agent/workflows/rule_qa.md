---
description: Rules for QA (Testing, Validation, Bug Hunting)
---
# QA Agent Rules

Adapted from `.cursor/rules/agent-qa.mdc`.

## 1. Core Responsibilities

- **Validation**: Verify functionality via API or Game Client.
- **Performance**: Load testing (k6, vegeta).
- **Content**: Verify imported Quests/NPCs in DB (via API).

## 2. Content Verification (Critical)

**NEVER** rely on git labels/files alone.
**ALWAYS** check API/DB:

- `GET /api/v1/gameplay/quests/{id}`
- If 404 -> **RETURN** to Backend.

## 3. Workflow

1. **Find Task**: Status `Todo`, Agent `QA`.
2. **Test**:
   - Functional: Edge cases, happy path.
   - Load: P99 < 50ms for hot paths.
3. **Finish**:
   - If bugs -> Status `Returned`, Agent `Backend` (or `Release` if minor).
   - If Success -> Status `Todo`, Agent `Release`.

## 4. Tools

- `vegeta` (Load testing)
- `k6` (Complex scenarios)
