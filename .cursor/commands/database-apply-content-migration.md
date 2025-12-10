# Apply Content Migration

Apply generated SQL migrations for content (quests, NPCs, dialogues).

## Steps

1. Verify Agent = `DB`, Status = `In Progress`
2. **Check order:** Схемные миграции должны быть применены ПЕРЕД контентными:
   - `V1_46__quest_definitions_tables.sql` (для квестов)
   - `V1_89__narrative_npc_dialogue_tables.sql` (для NPC и диалогов)
3. **Check changelog:** Убедиться что `changelog-content.yaml` включен в `changelog.yaml`
4. **Validate migrations:**
   ```powershell
   python scripts/validate-all-migrations.py
   ```
5. **Apply migrations:**
   - Windows: `.\scripts\db\apply-migrations-direct.ps1`
   - Linux/Mac: `./scripts/db/apply-migrations-direct.sh`
   - Or via Liquibase: `liquibase update`
6. **Verify import:**
   ```sql
   -- Quests
   SELECT COUNT(*) FROM gameplay.quest_definitions;
   
   -- NPCs
   SELECT COUNT(*) FROM narrative.npc_definitions;
   
   -- Dialogues
   SELECT COUNT(*) FROM narrative.dialogue_nodes;
   ```
7. Handoff to QA: Status `Todo`, Agent `QA`

**Update fields:**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: [
    { id: 239690516, value: 'f75ad846' }, // Status: Todo
    { id: 243899542, value: '3352c488' }, // Agent: QA
  ]
});
```

**Comment:**
```markdown
OK Content quests migration applied. {count} quests imported.
Migration: V*__content_quests_initial_import.sql
Issue: #{number}
```

