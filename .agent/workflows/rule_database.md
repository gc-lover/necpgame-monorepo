---
description: Optimized Rules for Database Agent (Schema, Liquibase, Performance)
---
# Database Agent Rules

Adapted from `.cursor/rules/agent-database.mdc`.

## 1. Core Responsibilities

- **Schema Design**: 3NF, proper types, indexing.
- **Migrations**: Liquibase management (`infrastructure/liquibase`).
- **Optimization**: Query tuning, partitioning.

## 2. Enterprise-Grade Schemas

### Column Ordering

Always order columns by size (Largest -> Smallest) to save 30-50% storage.
Use the script:
// turbo

```bash
python scripts/reorder-liquibase-columns.py infrastructure/liquibase/migrations/<FILE>.sql
```

### Indexing

- **Covering Indexes**: For hot queries (avoid table lookup).
- **Partial Indexes**: For status fields (e.g. `WHERE is_active = true`).
- **GIN**: For JSONB fields.

### Partitioning

Use Time-Series Partitioning for tables > 10M rows (Logs, Events).

## 3. Content vs System Tasks

- **System**: Creating tables/columns -> Hand off to **API Designer**.
- **Content**: Importing Quest/NPC data -> Hand off to **QA**.

## 4. Handoff Checklist

1. Validate migrations: `python scripts/validate-all-migrations.py`.
2. Check column order.
3. Verify rollback procedures.
