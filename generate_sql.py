import yaml
import json

# Load YAML data
with open('knowledge/canon/interactive-objects/cyberspace-easter-eggs.yaml', 'r', encoding='utf-8') as f:
    data = yaml.safe_load(f)

# Generate SQL inserts
sql = []
for egg in data['easter_eggs']:
    # Fix category values to match schema
    category = egg['category']
    if category == 'culture':
        category = 'cultural'
    elif category == 'technology':
        category = 'technological'  # correct according to DB constraint

    # Escape single quotes in text fields
    name = egg['name'].replace("'", "''")
    description = egg.get('description', '').replace("'", "''")
    content = egg.get('content', '').replace("'", "''")

    # Convert JSON fields to strings and escape for SQL
    location = json.dumps(egg['location'], ensure_ascii=False).replace("'", "''")
    discovery_method = json.dumps(egg['discovery_method'], ensure_ascii=False).replace("'", "''")
    rewards = json.dumps(egg.get('rewards', []), ensure_ascii=False).replace("'", "''")
    lore_connections = json.dumps(egg.get('lore_connections', []), ensure_ascii=False).replace("'", "''")

    sql.append(f"""INSERT INTO easter_eggs (id, name, category, difficulty, description, content, location, discovery_method, rewards, lore_connections, status, created_at, updated_at) VALUES ('{egg['id']}', '{name}', '{category}', '{egg['difficulty']}', '{description}', '{content}', '{location}', '{discovery_method}', '{rewards}', '{lore_connections}', 'active', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);""")

# Write to file with BOM for proper UTF-8 handling
with open('import_easter_eggs.sql', 'w', encoding='utf-8-sig') as f:
    f.write('\n'.join(sql))

print(f'Generated SQL for {len(sql)} easter eggs')
