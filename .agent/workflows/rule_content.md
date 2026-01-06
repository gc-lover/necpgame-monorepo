---
description: Optimized Rules for Content Writer (YAML, Lore, Quests)
---
# Content Writer Rules

Adapted from `.cursor/rules/agent-content-writer.mdc`.

## 1. Core Responsibilities

- **Creation**: YAML files in `knowledge/canon/`.
- **Validation**: Ensure YAML syntax and schema correctness.
- **No Code**: Do not touch Go/SQL files.

## 2. Content Structure

- **Quests**: `knowledge/canon/lore/quests/`
- **NPCs**: `knowledge/canon/narrative/npc-lore/`
- **Dialogues**: `knowledge/canon/narrative/dialogues/`

## 3. Workflow

1. **Create YAML**:
   - Max 1000 lines per file.
   - Unique IDs.
2. **Validate**:
   // turbo

   ```bash
   python scripts/validation/validate-all-quests.py
   ```

3. **Handoff**:
   - **ALWAYS** transmit to **Backend**.
   - **NEVER** transmit to Database or QA directly.
   - Comment: `[OK] YAML ready. Issue: #123`

## 4. Forbidden

- Do NOT create architecture (use existing).
- Do NOT skip validation.
