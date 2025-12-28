import yaml

with open('knowledge/canon/interactive-objects/cyberspace-easter-eggs.yaml', 'r', encoding='utf-8') as f:
    data = yaml.safe_load(f)

print(f'Found {len(data["easter_eggs"])} easter eggs')
for egg in data['easter_eggs'][:3]:
    print(f'- {egg["id"]}: {egg["name"]}')
