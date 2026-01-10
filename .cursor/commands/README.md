# Agent Commands Reference

This directory contains documentation for all agent commands available in the NECPGAME project.

## Command Categories

### Agent-Specific Commands
- `architect-*.md` - Architect agent commands
- `backend-*.md` - Backend agent commands
- `database-*.md` - Database agent commands
- `content-writer-*.md` - Content Writer agent commands
- `qa-*.md` - QA agent commands
- `performance-*.md` - Performance agent commands
- `network-*.md` - Network agent commands
- `security-*.md` - Security agent commands
- `devops-*.md` - DevOps agent commands
- `ui-ux-designer-*.md` - UI/UX Designer agent commands
- `ue5-*.md` - UE5 agent commands
- `game-balance-*.md` - Game Balance agent commands
- `release-*.md` - Release agent commands

### Common Commands
- `common-validation.md` - Shared validation commands
- `github-integration.md` - GitHub Project integration

## Command Syntax

All commands are executed via MCP in Cursor IDE:

```
/{agent}-{action} {parameters}
```

Example:
```
/backend-find-tasks
/backend-validate-optimizations #123
/database-refactor-schema players
```

## Implementation Status

| Command | Status | File |
|---------|--------|------|
| **Backend Commands** | | |
| backend-find-tasks | ✅ Available | backend-find-tasks.md |
| backend-validate-optimizations | ✅ Available | backend-validate-optimizations.md |
| backend-validate-result | ✅ Available | backend-validate-result.md |
| backend-import-quest-to-db | ✅ Available | backend-import-quest-to-db.md |
| **Database Commands** | | |
| database-find-tasks | ✅ Available | database-find-tasks.md |
| database-validate-result | ✅ Available | database-validate-result.md |
| database-refactor-schema | ✅ Available | database-refactor-schema.md |
| database-apply-content-migration | ✅ Available | database-apply-content-migration.md |
| **Architect Commands** | | |
| architect-find-tasks | ✅ Available | architect-find-tasks.md |
| **Content Writer Commands** | | |
| content-writer-validate-result | ✅ Available | content-writer-validate-result.md |
| **QA Commands** | | |
| qa-find-tasks | ✅ Available | qa-find-tasks.md |
| **Common Commands** | | |
| common-validation | ✅ Available | common-validation.md |
| github-integration | ✅ Available | github-integration.md |

**Total Commands Available: 13**

## Adding New Commands

1. Create new `.md` file in this directory
2. Follow the template structure
3. Update this README
4. Test the command via MCP