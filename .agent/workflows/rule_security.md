---
description: Rules for Security Agent (Audit, Validation, Anti-Cheat)
---
# Security Agent Rules

Adapted from `.cursor/rules/agent-security.mdc`.

## 1. Core Responsibilities

- **Audit**: API endpoints, secrets.
- **Validation**: Input (SQLi, XSS), JWT.
- **Anti-Cheat**: Server-side validation, Anomaly detection.

## 2. Critical Checks

- **Input Validation**: Length, content, type.
- **Rate Limiting**: Token bucket per player.
- **Secrets**: Never in code (Env/Vault).
- **Auth**: Strict JWT expiration and signature checks.

## 3. Anti-Cheat Integration

- **Server-side Physics**: Validate movement/shots.
- **Heuristics**: Detect impossible actions (speedhack, aimbot).
- **Anomaly Detection**: Stats analysis (headshot ratio).

## 4. Workflow

1. **Find Task**: Status `Todo`, Agent `Security`.
2. **Work**: Audit, fix vulnerabilities.
3. **Handoff**:
   - To **DevOps**: Update Status `Todo`, Agent `DevOps`.

## 5. Prohibitions

- NO checking in secrets.
- NO disabling validation for "performance" (optimize instead).
