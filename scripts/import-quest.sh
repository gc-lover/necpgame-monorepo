#!/bin/bash
# Issue: #688
# Script to import quest YAML to database via API

set -e

QUEST_FILE="${1:-knowledge/canon/lore/timeline-author/quests/europe/brussels/2020-2029/quest-006-waffles.yaml}"
API_URL="${2:-http://localhost:8083/api/v1/gameplay/quests/content/reload}"

if [ ! -f "$QUEST_FILE" ]; then
    echo "Error: Quest file not found: $QUEST_FILE"
    exit 1
fi

# Convert YAML to JSON using Python
JSON_CONTENT=$(python3 -c "
import yaml
import json
import sys

with open('$QUEST_FILE', 'r', encoding='utf-8') as f:
    data = yaml.safe_load(f)
    print(json.dumps(data, ensure_ascii=False))
")

# Extract quest_id from metadata
QUEST_ID=$(python3 -c "
import yaml
import sys

with open('$QUEST_FILE', 'r', encoding='utf-8') as f:
    data = yaml.safe_load(f)
    print(data['metadata']['id'])
")

# Create request payload
REQUEST_BODY=$(python3 -c "
import json
import sys

yaml_content = json.loads('''$JSON_CONTENT''')
quest_id = '''$QUEST_ID'''

payload = {
    'quest_id': quest_id,
    'yaml_content': yaml_content
}

print(json.dumps(payload, ensure_ascii=False))
")

# Send request
echo "Importing quest: $QUEST_ID"
echo "API URL: $API_URL"

RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$API_URL" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer ${AUTH_TOKEN:-}" \
    -d "$REQUEST_BODY")

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | sed '$d')

if [ "$HTTP_CODE" -eq 200 ]; then
    echo "OK Quest imported successfully!"
    echo "$BODY" | python3 -m json.tool
else
    echo "❌ Failed to import quest (HTTP $HTTP_CODE)"
    echo "$BODY"
    exit 1
fi


