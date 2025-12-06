# Import Quest to DB

Import content quest YAML to database.

## Single Quest Import (Updates)

1. Verify Status is `Backend - In Progress`
2. Check: labels `canon`, `lore`, `quest`, from Content Writer
3. Find YAML: `knowledge/canon/lore/timeline-author/quests/.../quest-*.yaml`
4. Import via API:
   - Windows: `scripts\import-quest.ps1 -QuestFile <path>`
   - Linux/Mac: `scripts/import-quest.sh <path>`
   - Or curl: `POST /api/v1/gameplay/quests/content/reload`
5. Verify: quest loaded, data correct, accessible via API
6. Handoff to QA: Update Status to `QA - Todo`

## Bulk Import (First Time)

For initial import of all content (quests, NPCs, dialogues):

1. **Check tables exist:**
   - Quests: `gameplay.quest_definitions` (migration `V1_46__quest_definitions_tables.sql`)
   - NPCs: `narrative.npc_definitions` (migration `V1_89__narrative_npc_dialogue_tables.sql`)
   - Dialogues: `narrative.dialogue_nodes` (migration `V1_89__narrative_npc_dialogue_tables.sql`)
   - If tables don't exist → handoff to `Database - Todo` first

2. **Generate SQL migrations:**
   - Windows: `.\scripts\generate-content-migrations.ps1`
   - Linux/Mac: `./scripts/generate-content-migrations.sh`
   - **Format:** 1 YAML file = 1 migration (with version from `metadata.version`)
   - **Output:**
     - Quests: `infrastructure/liquibase/migrations/data/quests/V*__data_quest_*.sql`
     - NPCs: `infrastructure/liquibase/migrations/data/npcs/V*__data_npc_*.sql`
     - Dialogues: `infrastructure/liquibase/migrations/data/dialogues/V*__data_dialogue_*.sql`

3. **Generate changelog:**
   - Windows: `.\scripts\db\generate-content-changelog.ps1`
   - Linux/Mac: `./scripts/db/generate-content-changelog.sh`
   - Creates `infrastructure/liquibase/changelog-content.yaml`

4. **Check changelog:** Ensure `changelog-content.yaml` is included in `changelog.yaml`

5. **Review generated migrations:** Check file count, versions, structure

6. **Handoff to Database:** Update Status to `Database - Todo`

7. **After migration applied:** Handoff to QA: Update Status to `QA - Todo`

**Full workflow:** See `scripts/CONTENT-MIGRATION-WORKFLOW.md`

**Update Status:**
```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516  // число,
    value: '{option_id}'  // id опции 'QA - Todo' или 'Database - Todo'
  }
});
```

**Important:** 
- Single quest updates: Use API import
- Bulk initial import: Generate SQL migration → Database → QA
- All content quests MUST be imported to DB before testing.
