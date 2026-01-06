---
description: Rules for Game Balance (Economy, Weapons, Rewards)
---
# Game Balance Agent Rules

Adapted from `.cursor/rules/agent-game-balance.mdc`.

## 1. Core Responsibilities

- **Balancing**: Weapons, Armor, Skills.
- **Economy**: Prices, Rewards, Drop rates.
- **Config**: YAML/JSON files in `balance/` or `config/`.

## 2. Methodology

- **Data-Driven**: Basing on metrics (TTK, Win Rates).
- **Config-Based**: Externalize values (Damage, RPM, Range).

## 3. Workflow

1. **Find Task**: Status `Todo`, Agent `GameBalance`.
2. **Work**: Edit configs, analyze data.
3. **Handoff**:
   - To **Release**: Update Status `Todo`, Agent `Release`.

## 4. Input/Output

- **Input**: Metrics, Player feedback, Requirements.
- **Output**: Balanced Configs, Formulas.
