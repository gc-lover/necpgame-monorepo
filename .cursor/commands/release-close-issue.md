# Close Issue

Close Issue after successful release.

## Steps

1. Verify: Release notes, GitHub Release, deployment, monitoring
2. Close Issue: `state: 'closed', state_reason: 'completed'`
3. Update Status to `Done`
4. Add comment: `OK Release completed` + details

**Update Status:**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516  // число,
    value: '{option_id}'  // id опции 'Done' из list_project_fields
  }
});
```
