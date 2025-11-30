# Import Quest to DB

Import content quest YAML to database.

## Steps

1. Verify Status is `Backend - In Progress`
2. Check: labels `canon`, `lore`, `quest`, from Content Writer
3. Find YAML: `knowledge/canon/lore/timeline-author/quests/.../quest-*.yaml`
4. Import: `POST /api/v1/gameplay/quests/content/reload` to `quest_definitions`
5. Verify: quest loaded, data correct, accessible via API
6. Handoff to QA: Update Status to `QA - Todo`

**Important:** All content quests MUST be imported to DB before testing.
