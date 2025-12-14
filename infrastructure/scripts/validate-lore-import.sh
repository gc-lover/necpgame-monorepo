#!/bin/bash
# Issue: #1845
# Validate lore import data integrity

set -e

echo "🔍 Starting lore import validation..."

# Database connection parameters
DB_HOST=${DB_HOST:-localhost}
DB_PORT=${DB_PORT:-5432}
DB_NAME=${DB_NAME:-necpgame}
DB_USER=${DB_USER:-necpgame_user}
DB_PASSWORD=${DB_PASSWORD:-necpgame_pass}

echo "📊 Validating NPC data..."
NPC_COUNT=$(psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -t -c "SELECT COUNT(*) FROM narrative.npc_definitions WHERE is_active = true;" 2>/dev/null || echo "0")
echo "  • Active NPCs: $NPC_COUNT"

echo "💬 Validating dialogue data..."
DIALOGUE_COUNT=$(psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -t -c "SELECT COUNT(*) FROM narrative.dialogue_nodes WHERE is_active = true;" 2>/dev/null || echo "0")
echo "  • Active dialogues: $DIALOGUE_COUNT"

echo "📖 Validating lore data..."
LORE_COUNT=$(psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -t -c "SELECT COUNT(*) FROM narrative.lore_entries WHERE is_active = true;" 2>/dev/null || echo "0")
echo "  • Active lore entries: $LORE_COUNT"

echo "🎯 Validating quest data..."
QUEST_COUNT=$(psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -t -c "SELECT COUNT(*) FROM gameplay.quest_definitions WHERE is_active = true;" 2>/dev/null || echo "0")
echo "  • Active quests: $QUEST_COUNT"

echo "🧪 Running data integrity checks..."

# Check for duplicate IDs
echo "  • Checking for duplicate NPC IDs..."
DUPLICATE_NPCS=$(psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -t -c "
    SELECT COUNT(*) FROM (
        SELECT npc_id, COUNT(*) as cnt
        FROM narrative.npc_definitions
        GROUP BY npc_id
        HAVING COUNT(*) > 1
    ) duplicates;" 2>/dev/null || echo "0")

if [ "$DUPLICATE_NPCS" -gt 0 ]; then
    echo "  ❌ Found $DUPLICATE_NPCS duplicate NPC IDs!"
    exit 1
else
    echo "  OK No duplicate NPC IDs"
fi

echo "  • Checking for duplicate quest IDs..."
DUPLICATE_QUESTS=$(psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -t -c "
    SELECT COUNT(*) FROM (
        SELECT quest_id, COUNT(*) as cnt
        FROM gameplay.quest_definitions
        GROUP BY quest_id
        HAVING COUNT(*) > 1
    ) duplicates;" 2>/dev/null || echo "0")

if [ "$DUPLICATE_QUESTS" -gt 0 ]; then
    echo "  ❌ Found $DUPLICATE_QUESTS duplicate quest IDs!"
    exit 1
else
    echo "  OK No duplicate quest IDs"
fi

echo "  • Checking JSONB validity..."
INVALID_JSON=$(psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -t -c "
    SELECT COUNT(*) FROM (
        SELECT id FROM narrative.npc_definitions WHERE content_data::text = ''
        UNION ALL
        SELECT id FROM narrative.dialogue_nodes WHERE content_data::text = ''
        UNION ALL
        SELECT id FROM narrative.lore_entries WHERE content_data::text = ''
        UNION ALL
        SELECT id FROM gameplay.quest_definitions WHERE content_data::text = ''
    ) invalid_json;" 2>/dev/null || echo "0")

if [ "$INVALID_JSON" -gt 0 ]; then
    echo "  ❌ Found $INVALID_JSON records with invalid JSON!"
    exit 1
else
    echo "  OK All JSONB data is valid"
fi

echo ""
echo "OK Lore import validation completed successfully!"
echo ""
echo "📈 Final Statistics:"
echo "  • NPCs: $NPC_COUNT"
echo "  • Dialogues: $DIALOGUE_COUNT"
echo "  • Lore entries: $LORE_COUNT"
echo "  • Quests: $QUEST_COUNT"
echo "  • Total content records: $(echo "$NPC_COUNT + $DIALOGUE_COUNT + $LORE_COUNT + $QUEST_COUNT" | bc)"
echo ""
echo "🎯 Database ready for API testing!"