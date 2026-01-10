# Database Agent: Apply Content Migration Command

## Command
```
/database-apply-content-migration
```

## Description
Applies content data migrations (quests, NPCs, dialogues) to the database.

## Usage
Execute this command when receiving content migrations from Backend agent for large-scale content imports.

## Migration Types

### Quest Migrations
- Location: `infrastructure/liquibase/migrations/data/quests/`
- Pattern: `V*__data_quest_*.sql`
- Content: Gameplay quest definitions

### NPC Migrations
- Location: `infrastructure/liquibase/migrations/data/npcs/`
- Pattern: `V*__data_npc_*.sql`
- Content: Narrative NPC definitions

### Dialogue Migrations
- Location: `infrastructure/liquibase/migrations/data/dialogues/`
- Pattern: `V*__data_dialogue_*.sql`
- Content: Social dialogue nodes

## Implementation
```bash
# Apply all content migrations
liquibase update --changelog-file=infrastructure/liquibase/changelog.yaml

# Or apply specific migration
liquibase update --changeSetAuthor={author} --changeSetId={id}

# Validate import
SELECT COUNT(*) FROM gameplay.quest_definitions;
SELECT COUNT(*) FROM narrative.npc_definitions;
SELECT COUNT(*) FROM narrative.dialogue_nodes;
```

## Response Format
```
[CONTENT MIGRATION] Applying content migrations...

📦 Quest Migrations:
- V20240101__data_quest_cyberpunk_city.sql (15 quests)
- V20240102__data_quest_underground_raid.sql (8 quests)

📦 NPC Migrations:
- V20240101__data_npc_fixers.sql (12 NPCs)
- V20240102__data_npc_nomads.sql (18 NPCs)

📦 Dialogue Migrations:
- V20240101__data_dialogue_city_events.sql (45 nodes)

[EXECUTING] Liquibase update...
✅ Quest migrations applied: 23 records
✅ NPC migrations applied: 30 records
✅ Dialogue migrations applied: 45 records

[VALIDATION] Post-migration checks...
✅ Foreign keys valid
✅ Data integrity verified
✅ API accessible
✅ Performance benchmarks pass

[RESULT] Content migration completed successfully
Total records imported: 98
Ready for QA testing
```

## Next Steps
- Validate data integrity
- Check API accessibility
- Run performance tests
- Handoff to QA for functional testing