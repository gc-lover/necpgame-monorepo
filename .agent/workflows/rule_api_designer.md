---
description: Rules for API Designer (OpenAPI 3.0, Domain Separation, Ogen)
---
# API Designer Rules

Adapted from `.cursor/rules/agent-api-designer.mdc`.

## 1. Core Responsibilities

- **OpenAPI 3.0 Specs**: Create specs for REST APIs (ogen-compatible).
- **Domain Separation**: Use `proto/openapi/<domain>/` structure.
- **Strict Typing**: All fields must have types, constraints, and examples.

## 2. Enterprise-Grade Domain Architecture

**Root:** `proto/openapi/`

### Common Foundation (DRY)

- `common/schemas/game-entities.yaml`: Character, CombatAction, etc.
- `common/schemas/economy-entities.yaml`: Wallet, Transaction, etc.
- `common/schemas/social-entities.yaml`: Profile, Guild, etc.
- `common/schemas/infrastructure-entities.yaml`: Account, Session.

### Service Structure

```yaml
# proto/openapi/combat-service/main.yaml
components:
  schemas:
    CombatSession:
      allOf:
        - $ref: '../common/schemas/game-entities.yaml#/CombatSessionEntity' # Inheritance
        - type: object
          properties:
            ...
```

## 3. Critical Validation

Before handoff, run:
// turbo

```bash
python scripts/openapi/validate-domains-openapi.py
```

// turbo

```bash
python scripts/batch-optimize-openapi-struct-alignment.py proto/openapi/<DOMAIN>/main.yaml
```

## 4. Performance Optimization

- **Struct Alignment**: Order fields Large -> Small (String/Object -> Int64 -> Int32 -> Bool).
- **Optimistic Locking**: Use `version` field for concurrent updates.

## 5. Handoff

- **To Backend**: Status `Todo`, Agent `Backend`.
- **Constraint**: Spec must pass validation.
