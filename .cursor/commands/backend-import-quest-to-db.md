# Backend Agent: Import Quest to DB Command

## Command
```
/backend-import-quest-to-db
```

## Description
Handles importing quest content from YAML files into the database after Content Writer creates the YAML files.

## Usage
Execute this command when Backend receives quest YAML files from Content Writer to import them into the database.

## Decision Logic

### Scenario A: Tables Exist + Small Import (1-10 quests)
```
Content Writer → Backend → API Import → QA
```

**Actions:**
1. Use `POST /api/v1/gameplay/quests/content/reload` endpoint
2. Validate import with `python scripts/migrations/validate-quest-imports.py`
3. Handoff to QA

### Scenario B: Tables Exist + Large Import (>10 quests)
```
Content Writer → Backend → SQL Migrations → Database → QA
```

**Actions:**
1. Generate SQL migrations with `python scripts/migrations/run_generator.py --type quests`
2. Validate migrations with `python scripts/migrations/validate-quest-migrations.py`
3. Handoff to Database agent for application

### Scenario C: Tables Don't Exist
```
Content Writer → Backend → Create Issue → Database → Backend → Continue
```

**Actions:**
1. Create Issue for Database: "Missing tables: gameplay.quest_definitions"
2. Set current task to `Blocked`
3. Wait for Database to create tables
4. Resume import process

## Implementation
```bash
# Check table existence
python scripts/migrations/check-quest-table.py

# For small imports
curl -X POST http://localhost:8080/api/v1/gameplay/quests/content/reload \
  -H "Content-Type: application/json" \
  -d '{"source": "yaml", "validate": true}'

# For large imports
python scripts/migrations/run_generator.py --type quests --output-dir infrastructure/liquibase/data/quests/
```

## Response Format
```
[QUEST IMPORT] Analyzing quest files...

Found 5 quest YAML files
Tables exist: ✅ gameplay.quest_definitions
Recommended: API Import (small batch)

[EXECUTING] API Import...
✅ Quest 1 imported successfully
✅ Quest 2 imported successfully
✅ Quest 3 imported successfully
✅ Quest 4 imported successfully
✅ Quest 5 imported successfully

[VALIDATION] Running post-import checks...
✅ All quests accessible via API
✅ Data integrity verified
✅ Foreign keys valid

[RESULT] Quest import completed successfully
Ready for QA testing
```

## Next Steps
- Small import: Handoff to QA
- Large import: Handoff to Database for migration application
- Missing tables: Create Database Issue and block current task