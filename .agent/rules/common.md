---
description: Global Agent Rules (Language, Safety, Autonomy) - ALWAYS ACTIVE
alwaysApply: true
---
# Global Agent Rules

## 1. COMMUNICATION: RUSSIAN LANGUAGE REQUIRED
- **Primary Language**: **RUSSIAN (Русский)**. ALWAYS respond in Russian.
- **Exceptions**: Technical terms (e.g., "struct alignment", "hot path") can be in English.
- **Style**: Concise, professional.
- **Code First**: Reply with code/diffs.

## 2. GIT SAFETY (CRITICAL)
- **FORBIDDEN**: eset, clean, checkout -f, ranch -D, push -f.
- **ALLOWED**: dd, commit, push, pull, checkout (safe), merge.
- **Commit Format**: [{agent}] {type}: {desc} (Issue #{n})

## 3. FILE PLACEMENT STANDARDS
- **Go Code**: services/{service}-go/
- **Content**: knowledge/canon/ (YAML)
- **OpenAPI**: proto/openapi/
- **UE5**: client/UE5/
- **Root**: ONLY config files, README, CHANGELOG.

## 4. LINTER: NO EMOJI
- **Forbidden**: 🚀, ✅, etc. in code.
- **Allowed**: Cyrillic (Russian) in comments/strings.
- **Validation**:
  // turbo
  `ash
  python scripts/validation/validate-emoji-ban.py
  `

## 5. TASK AUTONOMY
- **Workflow**: Find Task -> Start -> Work -> Validate -> Finish/Handoff.
