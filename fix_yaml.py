import re

with open('proto/openapi/world-domain/locations/main.yaml', 'r', encoding='utf-8') as f:
    content = f.read()

# Replace all unquoted Russian descriptions
content = re.sub(r'description: ([^"\'\n]*[а-яА-Я][^"\'\n]*(?:\([^)]*\))?)', r'description: "\1"', content)

with open('proto/openapi/world-domain/locations/main.yaml', 'w', encoding='utf-8') as f:
    f.write(content)

print('Fixed all Russian descriptions')
