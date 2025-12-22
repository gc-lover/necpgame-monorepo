#!/usr/bin/env python3
import re

# Read the file
with open('proto/openapi/integration-domain/client/client-weapon-effects-service.yaml', 'r', encoding='utf-8') as f:
    content = f.read()

# Fix the broken YAML
content = re.sub(
    r"\$ref: \.\.\\\.\.\\common-schemas\.yaml#/components/schemas/Error'401':\s*\$ref: \.\.\\\.\.\\common-schemas\.yaml#/components/schemas/Error'500':\s*\$ref: \.\.\\\.\.\\common-schemas\.yaml#/components/schemas/Error'.*'",
    "\n        '401':\n          $ref: '#/components/schemas/Error'\n        '500':\n          $ref: '#/components/schemas/Error'",
    content
)

# Write back
with open('proto/openapi/integration-domain/client/client-weapon-effects-service.yaml', 'w', encoding='utf-8') as f:
    f.write(content)

print("Fixed YAML file")
