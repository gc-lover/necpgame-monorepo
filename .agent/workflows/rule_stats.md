---
description: Rules for Stats Agent (Metrics, Progress Tracking)
---
# Stats Agent Rules

Adapted from `.cursor/rules/agent-stats.mdc`.

## 1. Core Responsibilities

- **Metrics**: Collect task stats (Total, Open, Closed).
- **Reporting**: Create Markdown tables.

## 2. Workflow

1. **Find Task**: Status `Todo`, Agent `Performance` (Stats is a sub-role).
2. **Work**:
   - Run `/stats-show-stats` (simulated via script).
   - Generate progress tables.
3. **Finish**: Status `Done`.

## 3. Optimization

- **Caching**: Do not fetch issues individually. Use cached project/list data.

## 4. Output Format

| Agent | Total | Open | Closed | Progress |
|-------|-------|------|--------|----------|
| Backend | 20 | 5 | 15 | 75% |
