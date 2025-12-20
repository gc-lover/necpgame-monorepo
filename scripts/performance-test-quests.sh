#!/bin/bash
# Performance test script for quest definitions
# Issue: #51 - Performance analysis

set -e

echo "🚀 Starting Quest Definitions Performance Test"

# Test 1: Basic queries
echo "📊 Test 1: Basic query performance"
docker exec necpgame-postgres-1 psql -U postgres -d necpgame -c "
\timing on
SELECT COUNT(*) FROM gameplay.quest_definitions;
SELECT * FROM gameplay.quest_definitions WHERE status = 'draft';
SELECT id, title FROM gameplay.quest_definitions WHERE level_min <= 10;
\timing off
"

# Test 2: JSONB operations
echo "🔍 Test 2: JSONB query performance"
docker exec necpgame-postgres-1 psql -U postgres -d necpgame -c "
\timing on
SELECT id, yaml_content->'metadata'->>'title' as title
FROM gameplay.quest_definitions
WHERE yaml_content->'metadata'->>'quest_type' = 'cultural';

SELECT id, yaml_content->'rewards'->>'experience' as exp
FROM gameplay.quest_definitions
WHERE cast(yaml_content->'metadata'->>'level_min' as integer) <= 10;
\timing off
"

# Test 3: Array operations
echo "🏷️ Test 3: Array query performance"
docker exec necpgame-postgres-1 psql -U postgres -d necpgame -c "
\timing on
SELECT id, title FROM gameplay.quest_definitions WHERE 'miami' = ANY(tags);
SELECT id, title FROM gameplay.quest_definitions WHERE tags && ARRAY['detroit','motor'];
SELECT id, title FROM gameplay.quest_definitions WHERE 'cyberpunk' = ANY(topics);
\timing off
"

# Test 4: Complex queries (typical game usage)
echo "🎮 Test 4: Complex game queries"
docker exec necpgame-postgres-1 psql -U postgres -d necpgame -c "
\timing on
-- Find suitable quests for level 5-15 player
SELECT id, title,
       yaml_content->'metadata'->>'quest_type' as type,
       yaml_content->'metadata'->>'level_min' as min_level,
       yaml_content->'metadata'->>'level_max' as max_level
FROM gameplay.quest_definitions
WHERE cast(yaml_content->'metadata'->>'level_min' as integer) <= 15
  AND cast(yaml_content->'metadata'->>'level_max' as integer) >= 5
  AND status = 'draft';

-- Find cultural quests with rewards
SELECT id, title,
       yaml_content->'rewards'->>'experience' as exp,
       yaml_content->'rewards'->>'currency' as currency
FROM gameplay.quest_definitions
WHERE yaml_content->'metadata'->>'quest_type' = 'cultural';
\timing off
"

echo "OK Performance tests completed!"
echo "📈 Results show current query performance with existing data volume"
echo "🔧 For production MMO scale, additional optimizations may be needed"
