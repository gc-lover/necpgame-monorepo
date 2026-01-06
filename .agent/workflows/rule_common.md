---
description: Global Common Rules (Language, Safety, Autonomy) - ALWAYS ACTIVE
alwaysApply: true
---
# Global Agent Rules

## 1. COMMUNICATION: RUSSIAN LANGUAGE REQUIRED

- **Primary Language**: **RUSSIAN (Русский)**. ALWAYS respond in Russian.
- **Exceptions**: Technical terms (e.g., "struct alignment", "hot path", "context timeout") can remain in English for precision.
- **Style**: Concise, professional, direct. NO "summaries" or "reports".
- **Code First**: Reply with code/diffs. Text should be minimal.

## 2. GIT SAFETY (CRITICAL)

- **FORBIDDEN**: `reset`, `clean`, `checkout -f`, `branch -D`, `push -f`.
- **ALLOWED**: `add`, `commit`, `push`, `pull`, `checkout` (safe), `merge`.
- **Commit Format**: `[{agent}] {type}: {desc} (Issue #{n})`

## 3. FILE PLACEMENT STANDARDS

- **Go Code**: `services/{service}-go/`
- **Content**: `knowledge/canon/` (YAML)
- **OpenAPI**: `proto/openapi/`
- **UE5**: `client/UE5/`
- **Scripts**: `scripts/`
- **Infrastructure**: `infrastructure/`
- **Root**: ONLY config files, README, CHANGELOG.

## 4. LINTER: NO EMOJI

- **Forbidden**: 🚀, ✅, etc. in code.
- **Allowed**: Cyrillic (Russian) in comments/strings.
- **Validation**: `python scripts/validation/validate-emoji-ban.py`

## 5. TASK AUTONOMY

- **Workflow**: Find Task -> Start -> Work -> Validate -> Finish/Handoff.
- **Don't Ask**: Do not ask for permission to start standard tasks.
- **Handoff Comment**: `[OK] Ready. Handed off to {NextAgent}. Issue: #{n}`
