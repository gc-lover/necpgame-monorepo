import yaml

data = yaml.safe_load(open('knowledge/canon/interactive-objects/cyberspace-easter-eggs.yaml', 'r', encoding='utf-8'))
for egg in data['easter_eggs']:
    print(f'{egg["id"]}: {egg["category"]}')
