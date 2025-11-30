# Return Task

Return task if no UI concept.

## Steps

1. Update Status to `Idea Writer - Returned`
2. Add comment with reason

**Update Status:**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,  // внутренний ID для API (не номер Issue)
  updated_field: {
    id: 239690516  // число,
    value: '{option_id}'  // id опции 'Idea Writer - Returned' из list_project_fields
  }
});
```

**Важно:** 
- `item_id` используется только для API вызова (внутренний ID проекта)
- В комментариях всегда указывай номер Issue в формате `Issue: #123`, а не `item_id`
- Номер Issue берется из `content.number` результата `list_project_items`
