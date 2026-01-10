# GitHub Project Integration

## Overview
Commands for managing GitHub Project integration and task lifecycle.

## Core Commands

### Update Project Fields
```
/update-github-fields --item-id {id} --type {TYPE} --check {0|1}
```

**Purpose:** Updates GitHub Project fields for task management

**Parameters:**
- `item-id`: GitHub Project item ID
- `type`: Task type (API, MIGRATION, DATA, BACKEND, UE5)
- `check`: Validation status (0=unchecked, 1=checked)

**Implementation:**
```bash
python scripts/update-github-fields.py --item-id 123 --type BACKEND --check 1
```

### Status Transitions

| From Status | To Status | Trigger | Command |
|-------------|-----------|---------|---------|
| Todo | In Progress | Agent takes task | `/update-github-fields` |
| In Progress | Todo | Handoff to next agent | MCP update_project_item |
| In Progress | Review | Self-review | MCP update_project_item |
| Review | Todo | Ready for next agent | MCP update_project_item |
| Todo | Done | Final completion | MCP update_project_item |

## MCP Commands

### List Project Items
```javascript
mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: 'Agent:"{AgentName}" Status:"Todo"'
});
```

### Update Project Item
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: [
    {id: STATUS_FIELD_ID, value: STATUS_OPTIONS['In Progress']},
    {id: AGENT_FIELD_ID, value: AGENT_OPTIONS.Backend},
    {id: TYPE_FIELD_ID, value: TYPE_OPTIONS.BACKEND},
    {id: CHECK_FIELD_ID, value: CHECK_OPTIONS['1']}
  ]
});
```

### Add Issue Comment
```javascript
mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,
  body: '[OK] Work completed. Handed off to {NextAgent}'
});
```

## Field IDs Reference

See `.cursor/GITHUB_PROJECT_CONFIG.md` for complete field mappings.

## Workflow Integration

### Taking a Task
1. Find task with agent command (`/{agent}-find-tasks`)
2. Update status to `In Progress`
3. Update agent field
4. Set appropriate type
5. Set check to 0 (unchecked)

### Handoff Process
1. Validate work with agent command (`/{agent}-validate-result`)
2. Update status to `Todo`
3. Update agent to next agent
4. Update type if changed
5. Keep check as 1 (validated)
6. Add comment with handoff details

## Error Handling

### Common Issues
- **Item not found**: Check item_id is correct
- **Permission denied**: Verify GitHub token has project access
- **Field ID changed**: Update `.cursor/GITHUB_PROJECT_CONFIG.md`

### Recovery
- Use MCP to manually update fields
- Check GitHub Project directly
- Re-run failed commands after fixing issues