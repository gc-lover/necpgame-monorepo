# Validate Result

Check UI/UX design readiness before handoff to UE5.

## Criteria

- [ ] Design doc created
- [ ] UX mechanics described
- [ ] Files in `knowledge/design/ui/`

**Result:**
- OK Ready → handoff to UE5
- ❌ Not ready → fix issues

**On handoff:** Update Status to `UE5 - Todo`

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
