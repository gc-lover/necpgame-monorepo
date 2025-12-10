# Validate Quest YAML

Validate quest YAML file before handoff.

## Validation

1. YAML syntax: `yamllint quest-*.yaml`
2. Structure: all required fields present
3. Data correctness: IDs unique, types valid
4. Size: file <=500 lines

**Result:**
- OK Valid → handoff to Backend: Status `Todo`, Agent `Backend`
- ❌ Invalid → fix errors, don't handoff

**Update fields:**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: [
    { id: 239690516, value: 'f75ad846' }, // Status: Todo
    { id: 243899542, value: '1fc13998' }, // Agent: Backend
  ]
});
```
