# Validate Result

Check idea readiness and determine next agent.

## Criteria

- [ ] Idea described, lore developed
- [ ] Quest structured (if applicable)
- [ ] Game mechanics described

## Determine Next Agent

**System task (default):**
- OK Ready → handoff to Architect, Update Status to `Architect - Todo`
- ❌ Not ready → fix issues, don't handoff

**UI/UX task (labels `ui`, `ux`, `client`):**
- OK Ready → handoff to UI/UX Designer, Update Status to `UI/UX - Todo`
- ❌ Not ready → fix issues, don't handoff

**Content quest (labels `canon`, `lore`, `quest`):**
- OK Ready → handoff to Content Writer, Update Status to `Content Writer - Todo`
- ❌ Not ready → fix issues, don't handoff

**Result:** OK Ready → handoff to determined agent / ❌ Not ready → fix issues

**Update Status:**
**ВАЖНО: Используй константы из `.cursor/GITHUB_PROJECT_CONFIG.md`!**
```javascript
// Для системных задач: передать Architect
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,  // из результата list_project_items
  updated_field: {
    id: 239690516,  // STATUS_FIELD_ID (число, не строка!)
    value: '799d8a69'  // STATUS_OPTIONS['Architect - Todo'] из GITHUB_PROJECT_CONFIG.md
  }
});

// Для UI/UX задач: value: '49689997' (STATUS_OPTIONS['UI/UX - Todo'])
// Для контент-квестов: value: 'c62b60d3' (STATUS_OPTIONS['Content Writer - Todo'])
```

**ОБЯЗАТЕЛЬНО:** После обновления статуса добавь комментарий к Issue с номером Issue (например, `Issue: #123`), а не `item_id`.
