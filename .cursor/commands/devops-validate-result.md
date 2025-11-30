# Validate Result

Check infrastructure readiness before handoff to UE5.

## Criteria

- [ ] Docker images created, K8s manifests ready
- [ ] CI/CD configured, observability set up

**Result:**
- OK Ready → handoff to UE5, Update Status to `UE5 - Todo`
- ❌ Not ready → fix issues, don't handoff

**Update Status:**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516  // число,
    value: '{option_id}'  // id опции 'UE5 - Todo' из list_project_fields
  }
});
```
