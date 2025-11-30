# Validate Quest YAML

Validate quest YAML file before handoff.

## Validation

1. YAML syntax: `yamllint quest-*.yaml`
2. Structure: all required fields present
3. Data correctness: IDs unique, types valid
4. Size: file <=500 lines

**Result:**
- OK Valid → handoff to Backend, Update Status to `Backend - Todo`
- ❌ Invalid → fix errors, don't handoff

**Update Status:**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516  // число,
    value: '{option_id}'  // id опции 'Backend - Todo' из list_project_fields
  }
});
```
