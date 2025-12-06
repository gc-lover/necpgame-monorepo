#!/bin/bash
# Issue: #50
# Generate SQL migrations from YAML content files
# Scans knowledge/canon/ and generates Liquibase migrations

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
MIGRATIONS_DIR="$PROJECT_ROOT/infrastructure/liquibase/migrations"
CONTENT_DIR="$PROJECT_ROOT/knowledge/canon"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check Python
if ! command -v python3 &> /dev/null; then
    echo -e "${RED}Error: python3 is required${NC}"
    exit 1
fi

# Check if content directory exists
if [ ! -d "$CONTENT_DIR" ]; then
    echo -e "${RED}Error: Content directory not found: $CONTENT_DIR${NC}"
    exit 1
fi

# Get next migration number
get_next_migration_number() {
    local max_num=0
    for file in "$MIGRATIONS_DIR"/V*__*.sql; do
        if [ -f "$file" ]; then
            local num=$(basename "$file" | sed -E 's/V([0-9]+)__.*/\1/' | sed 's/^0*//')
            if [ -n "$num" ] && [ "$num" -gt "$max_num" ]; then
                max_num=$num
            fi
        fi
    done
    echo $((max_num + 1))
}

# Generate quests migration
generate_quests_migration() {
    local migration_num=$(get_next_migration_number)
    
    echo -e "${GREEN}Generating quests migrations...${NC}"
    
    python3 << EOF
import yaml
import json
import os
from pathlib import Path
from datetime import datetime, date
from collections import defaultdict

def json_serial(obj):
    """JSON serializer for objects not serializable by default json code"""
    if isinstance(obj, (datetime, date)):
        return obj.isoformat()
    raise TypeError(f"Type {type(obj)} not serializable")

def escape_sql(s):
    """Escape single quotes and backslashes in SQL strings"""
    # First escape backslashes, then single quotes
    return s.replace("\\", "\\\\").replace("'", "''")

def generate_migration_name(relative_path):
    """Generate migration name from path: america/chicago/2020-2029 -> america_chicago_2020_2029"""
    # Handle root directory (.)
    if str(relative_path) == '.' or str(relative_path) == '':
        return 'root'
    # Replace path separators and special chars with underscores
    # Normalize to forward slashes first, then replace all separators
    path_str = str(relative_path).replace('\\', '/')
    name = path_str.replace('/', '_').replace('-', '_')
    # Remove leading/trailing underscores and collapse multiple underscores
    name = '_'.join(filter(None, name.split('_')))
    return name

migrations_dir = Path("$MIGRATIONS_DIR")
content_dir = Path("$CONTENT_DIR")
quests_base = content_dir / "lore" / "_03-lore" / "timeline-author" / "quests"
migration_num = $migration_num

# Find all quest YAML files
quest_files = list(quests_base.rglob("quest-*.yaml"))

if not quest_files:
    print("No quest files found")
    exit(0)

# Filter out templates, READMEs, etc.
quest_files = [f for f in quest_files if not any(skip in f.name.lower() for skip in ['template', 'readme', 'index', 'list', 'tracker', 'spread', 'prioritization'])]

print(f"Found {len(quest_files)} quest files")

total_migrations = 0
total_quests = 0

for quest_file in sorted(quest_files):
    # Get relative path from quests_base and remove .yaml extension
    relative_path = quest_file.relative_to(quests_base)
    path_without_ext = str(relative_path).replace('.yaml', '').replace('.yml', '')
    migration_name = generate_migration_name(path_without_ext)
    data_dir = migrations_dir / "data" / "quests"
    data_dir.mkdir(parents=True, exist_ok=True)
    
    # Version will be added after reading metadata
    migration_file_template = data_dir / f"V{migration_num}__data_quest_{migration_name}"
    
    quest_count = 0
    try:
        try:
            with open(quest_file, 'r', encoding='utf-8') as f:
                data = yaml.safe_load(f)
            
            if not data or 'metadata' not in data:
                continue
            
        metadata = data.get('metadata', {})
        quest_def = data.get('quest_definition', {})  # Может отсутствовать
        summary = data.get('summary', {})
        
        quest_id = metadata.get('id', '')
        if not quest_id:
            continue
        
        # Get version from metadata for migration filename
        version = metadata.get('version', '1.0.0')
        version_suffix = version.replace('.', '_').replace('-', '_')
        
        # Set final migration file name with version
        migration_file = migration_file_template.parent / f"{migration_file_template.name}_v{version_suffix}.sql"
        
        sql_lines = [
            "-- Issue: #50",
            f"-- Import quest from: {relative_path}",
            f"-- Version: {version}",
            f"-- Generated: {datetime.now().isoformat()}",
            "",
            "BEGIN;",
            "",
        ]
        
        # Если quest_definition отсутствует, используем дефолты
        title = metadata.get('title', '') or summary.get('goal', '')
            description = summary.get('essence', '') or summary.get('goal', '') or summary.get('problem', '')
            quest_type = quest_def.get('quest_type', 'side') if quest_def else 'side'
            level_min = quest_def.get('level_min') if quest_def else None
            level_max = quest_def.get('level_max') if quest_def else None
            
            # Используем дефолты если quest_definition отсутствует
            requirements = json.dumps(quest_def.get('requirements', {}), ensure_ascii=False, default=json_serial) if quest_def else '{}'
            objectives = json.dumps(quest_def.get('objectives', []), ensure_ascii=False, default=json_serial) if quest_def else '[]'
            rewards = json.dumps(quest_def.get('rewards', {}), ensure_ascii=False, default=json_serial) if quest_def else '{}'
            branches = json.dumps(quest_def.get('branches', []), ensure_ascii=False, default=json_serial) if quest_def else '[]'
            content_data = json.dumps(data, ensure_ascii=False, default=json_serial)
            
            # Escape SQL strings
            title = escape_sql(title)
            description = escape_sql(description)
            requirements = escape_sql(requirements)
            objectives = escape_sql(objectives)
            rewards = escape_sql(rewards)
            branches = escape_sql(branches)
            content_data = escape_sql(content_data)
            
            level_min_sql = str(level_min) if level_min is not None else 'NULL'
            level_max_sql = str(level_max) if level_max is not None else 'NULL'
            
            sql_lines.append(f"-- Quest: {quest_id}")
            sql_lines.append(
                f"INSERT INTO gameplay.quest_definitions "
                f"(quest_id, title, description, quest_type, level_min, level_max, "
                f"requirements, objectives, rewards, branches, content_data, version, is_active) "
                f"VALUES ("
                f"'{quest_id}', "
                f"'{title}', "
                f"'{description}', "
                f"'{quest_type}', "
                f"{level_min_sql}, "
                f"{level_max_sql}, "
                f"'{requirements}'::jsonb, "
                f"'{objectives}'::jsonb, "
                f"'{rewards}'::jsonb, "
                f"'{branches}'::jsonb, "
                f"'{content_data}'::jsonb, "
                f"1, "
                f"true"
                f") "
                f"ON CONFLICT (quest_id) DO UPDATE SET "
                f"title = EXCLUDED.title, "
                f"description = EXCLUDED.description, "
                f"quest_type = EXCLUDED.quest_type, "
                f"level_min = EXCLUDED.level_min, "
                f"level_max = EXCLUDED.level_max, "
                f"requirements = EXCLUDED.requirements, "
                f"objectives = EXCLUDED.objectives, "
                f"rewards = EXCLUDED.rewards, "
                f"branches = EXCLUDED.branches, "
                f"content_data = EXCLUDED.content_data, "
                f"updated_at = CURRENT_TIMESTAMP;"
            )
            sql_lines.append("")
            quest_count = 1
            
        except Exception as e:
            print(f"Error processing {quest_file}: {e}")
            continue
    
    if quest_count == 0:
        continue  # Skip if no quest was processed
    
    sql_lines.append("COMMIT;")
    sql_lines.append("")
    sql_lines.append(f"-- Total imported: {quest_count} quest")
    
    # Write migration file
    migration_file.parent.mkdir(parents=True, exist_ok=True)
    with open(migration_file, 'w', encoding='utf-8') as f:
        f.write('\n'.join(sql_lines))
    
    print(f"Generated: {migration_file.name}")
    total_migrations += 1
    total_quests += quest_count
    migration_num += 1

print(f"\nTotal: {total_migrations} migrations, {total_quests} quests")
EOF

    if [ $? -eq 0 ]; then
        echo -e "${GREEN}OK Quests migrations generated${NC}"
    else
        echo -e "${RED}❌ Failed to generate quests migrations${NC}"
        exit 1
    fi
}

# Generate NPCs migration (requires npc_definitions table)
generate_npcs_migration() {
    local migration_num=$(get_next_migration_number)
    
    echo -e "${YELLOW}WARNING  NPC migrations require 'npc_definitions' table (not created yet)${NC}"
    echo -e "${GREEN}Generating NPCs migrations...${NC}"
    
    python3 << EOF
import yaml
import json
from pathlib import Path
from datetime import datetime, date
from collections import defaultdict

def json_serial(obj):
    if isinstance(obj, (datetime, date)):
        return obj.isoformat()
    raise TypeError(f"Type {type(obj)} not serializable")

def escape_sql(s):
    return s.replace("\\", "\\\\").replace("'", "''")

def generate_migration_name(relative_path):
    if str(relative_path) == '.' or str(relative_path) == '':
        return 'root'
    # Normalize to forward slashes first, then replace all separators
    path_str = str(relative_path).replace('\\', '/')
    name = path_str.replace('/', '_').replace('-', '_')
    name = '_'.join(filter(None, name.split('_')))
    return name

migrations_dir = Path("$MIGRATIONS_DIR")
content_dir = Path("$CONTENT_DIR")
npcs_base = content_dir / "narrative" / "npc-lore"
migration_num = $migration_num

# Find all NPC YAML files (exclude templates and README)
npc_files = [f for f in npcs_base.rglob("*.yaml") 
              if not f.name.startswith(('TEMPLATE', 'README', 'INDEX', 'LIST', 'TRACKER', 'SPREAD', 'PRIORITIZATION'))]

if not npc_files:
    print("No NPC files found")
    exit(0)

print(f"Found {len(npc_files)} NPC files")

total_migrations = 0
total_npcs = 0

for npc_file in sorted(npc_files):
    # Get relative path from npcs_base and remove .yaml extension
    relative_path = npc_file.relative_to(npcs_base)
    path_without_ext = str(relative_path).replace('.yaml', '').replace('.yml', '')
    migration_name = generate_migration_name(path_without_ext)
    data_dir = migrations_dir / "data" / "npcs"
    data_dir.mkdir(parents=True, exist_ok=True)
    
    # Version will be added after reading metadata
    migration_file_template = data_dir / f"V{migration_num}__data_npc_{migration_name}"
    
    npc_count = 0
    try:
        with open(npc_file, 'r', encoding='utf-8') as f:
            data = yaml.safe_load(f)
        
        if not data or 'metadata' not in data:
            continue
        
        metadata = data.get('metadata', {})
        npc_id = metadata.get('id', '')
        if not npc_id:
            continue
        
        # Get version from metadata for migration filename
        version = metadata.get('version', '1.0.0')
        version_suffix = version.replace('.', '_').replace('-', '_')
        
        # Set final migration file name with version
        migration_file = migration_file_template.parent / f"{migration_file_template.name}_v{version_suffix}.sql"
        
        sql_lines = [
            "-- Issue: #50",
            f"-- Import NPC from: {relative_path}",
            f"-- Version: {version}",
            f"-- Generated: {datetime.now().isoformat()}",
            "-- WARNING  WARNING: Requires 'npc_definitions' table (create via Database agent)",
            "",
            "BEGIN;",
            "",
        ]
        
        title = metadata.get('title', '')
        content_data = json.dumps(data, ensure_ascii=False, default=json_serial)
        
        title = escape_sql(title)
        content_data = escape_sql(content_data)
        
        sql_lines.append(f"-- NPC: {npc_id}")
        sql_lines.append(
            f"INSERT INTO narrative.npc_definitions "
            f"(npc_id, title, content_data, version, is_active) "
            f"VALUES ("
            f"'{npc_id}', "
            f"'{title}', "
            f"'{content_data}'::jsonb, "
            f"1, "
            f"true"
            f") "
            f"ON CONFLICT (npc_id) DO UPDATE SET "
            f"title = EXCLUDED.title, "
            f"content_data = EXCLUDED.content_data, "
            f"updated_at = CURRENT_TIMESTAMP;"
        )
        sql_lines.append("")
        npc_count = 1
        
    except Exception as e:
        print(f"Error processing {npc_file}: {e}")
        continue
    
    if npc_count == 0:
        continue  # Skip if no NPC was processed
    
    sql_lines.append("COMMIT;")
    sql_lines.append("")
    sql_lines.append(f"-- Total imported: {npc_count} NPC")
    
    migration_file.parent.mkdir(parents=True, exist_ok=True)
    with open(migration_file, 'w', encoding='utf-8') as f:
        f.write('\n'.join(sql_lines))
    
    print(f"Generated: {migration_file.name}")
    total_migrations += 1
    total_npcs += npc_count
    migration_num += 1

print(f"\nTotal: {total_migrations} migrations, {total_npcs} NPCs")
EOF

    if [ $? -eq 0 ]; then
        echo -e "${GREEN}OK NPCs migrations generated${NC}"
    else
        echo -e "${YELLOW}WARNING  NPCs migrations skipped (check errors above)${NC}"
    fi
}

# Generate dialogues migration (requires dialogue_nodes table)
generate_dialogues_migration() {
    local migration_num=$(get_next_migration_number)
    
    echo -e "${YELLOW}WARNING  Dialogue migrations require 'dialogue_nodes' table (not created yet)${NC}"
    echo -e "${GREEN}Generating dialogues migrations...${NC}"
    
    python3 << EOF
import yaml
import json
from pathlib import Path
from datetime import datetime, date
from collections import defaultdict

def json_serial(obj):
    if isinstance(obj, (datetime, date)):
        return obj.isoformat()
    raise TypeError(f"Type {type(obj)} not serializable")

def escape_sql(s):
    return s.replace("\\", "\\\\").replace("'", "''")

def generate_migration_name(relative_path):
    if str(relative_path) == '.' or str(relative_path) == '':
        return 'root'
    # Normalize to forward slashes first, then replace all separators
    path_str = str(relative_path).replace('\\', '/')
    name = path_str.replace('/', '_').replace('-', '_')
    name = '_'.join(filter(None, name.split('_')))
    return name

migrations_dir = Path("$MIGRATIONS_DIR")
content_dir = Path("$CONTENT_DIR")
dialogues_base = content_dir / "narrative" / "dialogues"
migration_num = $migration_num

# Find all dialogue YAML files (exclude templates and README)
dialogue_files = [f for f in dialogues_base.rglob("*.yaml") 
                   if not f.name.upper().startswith(('TEMPLATE', 'README', 'DIALOGUE-TEMPLATE'))]

if not dialogue_files:
    print("No dialogue files found")
    exit(0)

print(f"Found {len(dialogue_files)} dialogue files")

total_migrations = 0
total_dialogues = 0

for dialogue_file in sorted(dialogue_files):
    # Get relative path from dialogues_base and remove .yaml extension
    relative_path = dialogue_file.relative_to(dialogues_base)
    path_without_ext = str(relative_path).replace('.yaml', '').replace('.yml', '')
    migration_name = generate_migration_name(path_without_ext)
    data_dir = migrations_dir / "data" / "dialogues"
    data_dir.mkdir(parents=True, exist_ok=True)
    
    # Version will be added after reading metadata
    migration_file_template = data_dir / f"V{migration_num}__data_dialogue_{migration_name}"
    
    dialogue_count = 0
    try:
        with open(dialogue_file, 'r', encoding='utf-8') as f:
            data = yaml.safe_load(f)
        
        if not data or 'metadata' not in data:
            continue
        
        metadata = data.get('metadata', {})
        dialogue_id = metadata.get('id', '')
        if not dialogue_id:
            continue
        
        # Get version from metadata for migration filename
        version = metadata.get('version', '1.0.0')
        version_suffix = version.replace('.', '_').replace('-', '_')
        
        # Set final migration file name with version
        migration_file = migration_file_template.parent / f"{migration_file_template.name}_v{version_suffix}.sql"
        
        sql_lines = [
            "-- Issue: #50",
            f"-- Import dialogue from: {relative_path}",
            f"-- Version: {version}",
            f"-- Generated: {datetime.now().isoformat()}",
            "-- WARNING  WARNING: Requires 'dialogue_nodes' table (create via Database agent)",
            "",
            "BEGIN;",
            "",
        ]
        
        title = metadata.get('title', '')
        content_data = json.dumps(data, ensure_ascii=False, default=json_serial)
        
        title = escape_sql(title)
        content_data = escape_sql(content_data)
        
        sql_lines.append(f"-- Dialogue: {dialogue_id}")
        sql_lines.append(
            f"INSERT INTO narrative.dialogue_nodes "
            f"(dialogue_id, title, content_data, version, is_active) "
            f"VALUES ("
            f"'{dialogue_id}', "
            f"'{title}', "
            f"'{content_data}'::jsonb, "
            f"1, "
            f"true"
            f") "
            f"ON CONFLICT (dialogue_id) DO UPDATE SET "
            f"title = EXCLUDED.title, "
            f"content_data = EXCLUDED.content_data, "
            f"updated_at = CURRENT_TIMESTAMP;"
        )
        sql_lines.append("")
        dialogue_count = 1
        
    except Exception as e:
        print(f"Error processing {dialogue_file}: {e}")
        continue
    
    if dialogue_count == 0:
        continue  # Skip if no dialogue was processed
    
    sql_lines.append("COMMIT;")
    sql_lines.append("")
    sql_lines.append(f"-- Total imported: {dialogue_count} dialogue")
    
    migration_file.parent.mkdir(parents=True, exist_ok=True)
    with open(migration_file, 'w', encoding='utf-8') as f:
        f.write('\n'.join(sql_lines))
    
    print(f"Generated: {migration_file.name}")
    total_migrations += 1
    total_dialogues += dialogue_count
    migration_num += 1

print(f"\nTotal: {total_migrations} migrations, {total_dialogues} dialogues")
EOF

    if [ $? -eq 0 ]; then
        echo -e "${GREEN}OK Dialogues migrations generated${NC}"
    else
        echo -e "${YELLOW}WARNING  Dialogues migrations skipped (check errors above)${NC}"
    fi
}

# Main
echo -e "${YELLOW}=== Content Migration Generator ===${NC}"
echo ""

# Generate quests migration
generate_quests_migration

echo ""

# Generate NPCs migration (requires table - will generate but table must exist)
generate_npcs_migration

# Generate dialogues migration (requires table - will generate but table must exist)
generate_dialogues_migration

echo -e "${GREEN}OK All migrations generated successfully!${NC}"
echo ""
echo "Next steps:"
echo "1. Review generated migrations"
echo "2. Create missing tables (npc_definitions, dialogue_nodes) if needed"
echo "3. Apply migrations: liquibase update"
echo "4. Or use API batch import for updates"

