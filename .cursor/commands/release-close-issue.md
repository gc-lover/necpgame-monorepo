# Close Issue

Close Issue after successful release.

## Steps

1. Verify: Release notes, GitHub Release, deployment, monitoring
2. Close Issue: `state: 'closed', state_reason: 'completed'`
3. Update fields: Status `Done`, Agent `Release`
4. Add comment: `OK Release completed` + details

**Update Status:**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: [
    { id: 239690516, value: '98236657' }, // Status: Done
    { id: 243899542, value: 'f5878f68' }, // Agent: Release
  ]
});
```
