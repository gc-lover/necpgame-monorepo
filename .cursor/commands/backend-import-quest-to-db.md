# Import Quest to DB

Import content quest YAML to database.

## Steps

1. Verify Status is `Backend - In Progress`
2. Check: labels `canon`, `lore`, `quest`, from Content Writer
3. Find YAML: `knowledge/canon/lore/timeline-author/quests/.../quest-*.yaml`
4. Import: `POST /api/v1/gameplay/quests/content/reload` to `quest_definitions`
5. Verify: quest loaded, data correct, accessible via API
6. Handoff to QA: Update Status to `QA - Todo`

**Update Status:**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516  // число,
    value: '{option_id}'  // id опции 'QA - Todo' из list_project_fields
  }
});
```

**Important:** All content quests MUST be imported to DB before testing.
