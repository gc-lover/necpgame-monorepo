# Return Task

Return task if not ready.

## Return Reasons

- No idea / idea insufficient → Idea Writer
- UI task → UI/UX Designer
- Content quest → Content Writer

## Steps

1. Update Status to `{CorrectAgent} - Returned`
2. Add comment with reason

**Update Status:**
```javascript
// Получить id опции через mcp_github_list_project_fields
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,  // внутренний ID для API (не номер Issue)
  updated_field: {
    id: 239690516,  // число
    value: '{option_id}'  // id опции '{option_id}' из list_project_fields  // id опции "{CorrectAgent} - Returned"
  }
});
```

**Важно:** 
- `item_id` используется только для API вызова (внутренний ID проекта)
- В комментариях всегда указывай номер Issue в формате `Issue: #123`, а не `item_id`
- Номер Issue берется из `content.number` результата `list_project_items`
