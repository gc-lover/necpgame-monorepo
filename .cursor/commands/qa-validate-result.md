# Validate Result

Check QA completion before handoff to Release.

## Criteria

- [ ] All tests passed
- [ ] Test report created
- [ ] No critical bugs

**Result:**
- OK Ready → handoff to Release
- ❌ Not ready → fix bugs

**On handoff:** Update Status to `Release - Todo`

**Update Status:**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516  // число,
    value: '{option_id}'  // id опции 'Release - Todo' из list_project_fields
  }
});
```
