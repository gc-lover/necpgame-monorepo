---
description: Official workflow for NECPGAME tasks (Find, Start, Work, Finish) following project rules.
---
# NECPGAME Task Workflow

This workflow enforces the rules from `.cursor/AGENT_SIMPLE_GUIDE.md` and `.cursor/rules/always.mdc`.

## 0. Context & Rules Setup

1. **Identify Role**: Determine which Agent role applies (e.g. Backend, Content, QA).
2. **Read Rules**:
   - `view_file c:\NECPGAME\.cursor\rules\always.mdc`
   - `view_file c:\NECPGAME\.cursor\rules\agent-{role}.mdc` (if role is specific e.g `agent-backend.mdc`)

## 1. Start Task ("Take")

If you have an Issue Number (e.g. #123) but not the Project Item ID:

1. **Find Item ID**:

   ```bash
   gh project item-list 1 --owner gc-lover --format json --limit 100 > project_items.json
   ```

   (Read the JSON to find the item where `content.number` matches the issue, get the `id`)

2. **Update Status to In Progress**:
   Define the arguments carefully based on the task type (API, BACKEND, etc.).
   // turbo

   ```bash
   python scripts/update-github-fields.py --item-id <ITEM_ID> --status in_progress --check 0 --agent <MY_AGENT_ROLE> --type <TYPE>
   ```

   Argument values:
   - status: `in_progress`
   - agent: see `update-github-fields.py --help` for list (e.g. `backend`, `content`)
   - type: `API`, `MIGRATION`, `DATA`, `BACKEND`, `UE5`
   - check: `0`

## 2. Execute Work

1. **Follow Rules**:
   - **No emoji** in code.
   - **File placement**: See `agent-file-placement.mdc`.
   - **Backend**: Optimizations required (see `agent-backend.mdc`).
2. **Validation**:
   - Run relevant validation scripts.
   - Global: `python scripts/validation/validate-emoji-ban.py .`

## 3. Finish Task ("Hand off")

1. **Mark Checked**:
   // turbo

   ```bash
   python scripts/update-github-fields.py --item-id <ITEM_ID> --check 1
   ```

2. **Update Status & Handoff**:
   - Determine next agent (see `AGENT_SIMPLE_GUIDE.md`: Idea -> Architect -> Backend -> QA -> Release, etc.)
   // turbo

   ```bash
   python scripts/update-github-fields.py --item-id <ITEM_ID> --status todo --agent <NEXT_AGENT_ROLE> --check 1
   ```

3. **Comment**:
   // turbo

   ```bash
   gh issue comment <ISSUE_NUM> --body "[OK] Ready. Handed off to <NEXT_AGENT_ROLE><br>Issue: #<ISSUE_NUM>"
   ```
