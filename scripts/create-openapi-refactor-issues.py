#!/usr/bin/env python3
"""
Script to create GitHub issues for OpenAPI service refactoring.
Creates one issue per service that needs to be refactored from legacy directories.
"""

import requests
import json
import time
import os
from pathlib import Path

# GitHub configuration
GITHUB_TOKEN = os.getenv('GITHUB_TOKEN')
REPO_OWNER = 'gc-lover'
REPO_NAME = 'necpgame-monorepo'

# Project configuration
PROJECT_NUMBER = 1
OWNER_TYPE = 'user'

# Field IDs from config
STATUS_FIELD_ID = 239690516
AGENT_FIELD_ID = 243899542
TYPE_FIELD_ID = 246469155
CHECK_FIELD_ID = 246468990

# Option IDs
STATUS_TODO = 'f75ad846'
AGENT_API = '6aa5d9af'
TYPE_API = '66f88b2c'
CHECK_NOT_CHECKED = '22932cc7'

def create_github_issue(title, body=""):
    """Create a GitHub issue"""
    url = f"https://api.github.com/repos/{REPO_OWNER}/{REPO_NAME}/issues"

    headers = {
        'Authorization': f'token {GITHUB_TOKEN}',
        'Accept': 'application/vnd.github.v3+json'
    }

    data = {
        'title': title,
        'body': body,
        'labels': ['refactor', 'openapi', 'api-design']
    }

    response = requests.post(url, headers=headers, json=data)

    if response.status_code == 201:
        issue_data = response.json()
        print(f"Created issue #{issue_data['number']}: {title}")
        return issue_data['id'], issue_data['number']
    else:
        print(f"Failed to create issue: {response.status_code} - {response.text}")
        return None, None

def add_to_project(issue_id):
    """Add issue to GitHub Project"""
    url = f"https://api.github.com/users/{REPO_OWNER}/projectsV2/{PROJECT_NUMBER}/items"

    headers = {
        'Authorization': f'token {GITHUB_TOKEN}',
        'Accept': 'application/vnd.github.v3+json'
    }

    data = {
        'content_id': issue_id,
        'content_type': 'Issue'
    }

    response = requests.post(url, headers=headers, json=data)

    if response.status_code in [200, 201, 202]:
        item_data = response.json()
        print(f"Added to project: {item_data.get('id')}")
        return item_data.get('id')
    else:
        print(f"Failed to add to project: {response.status_code} - {response.text}")
        return None

def update_project_field(item_id, field_id, value):
    """Update a field in GitHub Project"""
    url = f"https://api.github.com/users/{REPO_OWNER}/projectsV2/{PROJECT_NUMBER}/items/{item_id}"

    headers = {
        'Authorization': f'token {GITHUB_TOKEN}',
        'Accept': 'application/vnd.github.v3+json'
    }

    data = {
        'field_id': str(field_id),
        'value': str(value)
    }

    response = requests.patch(url, headers=headers, json=data)

    if response.status_code == 200:
        return True
    else:
        print(f"Failed to update field {field_id}: {response.status_code} - {response.text}")
        return False

def get_file_count(directory):
    """Get count of YAML files in directory"""
    try:
        path = Path(f"proto/openapi/{directory}")
        if path.exists():
            return len(list(path.rglob("*.yaml")))
        return 0
    except:
        return 0

def get_subdirectories(directory):
    """Get list of subdirectories in a directory"""
    try:
        path = Path(f"proto/openapi/{directory}")
        if path.exists() and path.is_dir():
            return [d.name for d in path.iterdir() if d.is_dir()]
        return []
    except:
        return []

def main():
    """Main function to create refactoring issues"""

    # List of legacy directories to refactor
    legacy_directories = [
        ('system', 'AI, monitoring, networking, infrastructure services'),
        ('specialized', 'combat, crafting, effects, movement, matchmaking services'),
        ('social', 'communication, guilds, relationships, community services'),
        ('world', 'locations, events, environment, politics services'),
        ('economy', 'trading, auctions, currencies, marketplace services'),
        ('analysis', 'analytics dashboard services'),
        ('arena', 'arena and matchmaking services'),
        ('auth-expansion', 'extended authentication and session services'),
        ('companion', 'companion services'),
        ('cosmetic', 'cosmetic and appearance services'),
        ('cyberpunk', 'cyberware, hacking, cyberspace services'),
        ('cyberspace', 'cyberspace navigation services'),
        ('cyberspace-easter-eggs', 'cyberspace easter eggs services'),
        ('faction', 'faction and corporation services'),
        ('guild-system', 'guild system services'),
        ('integration', 'external integration services'),
        ('inventory-management', 'inventory management services'),
        ('misc', 'miscellaneous utility services'),
        ('ml-ai', 'machine learning and AI services'),
        ('notification-system', 'notification system services'),
        ('progression', 'character progression and leveling services'),
        ('referral', 'referral and affiliate services'),
        ('stock-analytics', 'stock analytics services'),
        ('webrtc', 'WebRTC communication services'),
        ('world-cities', 'world cities services'),
        ('world-regions', 'world regions services'),
    ]

    for directory, description in legacy_directories:
        file_count = get_file_count(directory)
        subdirs = get_subdirectories(directory)

        if file_count > 0:
            title = f"[REORG] Refactor {directory}-domain - {description} ({file_count} files)"

            # Create detailed body
            body = f"""## OpenAPI Service Refactoring Task

**Directory:** `proto/openapi/{directory}/`
**Files to refactor:** {file_count}
**Description:** {description}

### Subdirectories to convert to services:
"""

            if subdirs:
                for subdir in subdirs:
                    body += f"- `{subdir}/`\n"
            else:
                body += "- Direct YAML files in root directory\n"

            body += f"""
### Requirements:

1. **Analyze existing files** in `proto/openapi/{directory}/`
2. **Create service structure** following SOLID/DRY domain inheritance pattern
3. **Use common schemas** from `proto/openapi/common/`
4. **Implement enterprise-grade OpenAPI specs** with strict typing
5. **Validate with Redocly** and **generate Go code** with Ogen
6. **Add performance optimizations** (struct alignment, optimistic locking)

### Target Structure:
```
proto/openapi/{directory}-domain/
├── main.yaml              # Enterprise-grade API spec
├── README.md             # Service documentation
└── docs/
    └── index.html        # Generated documentation
```

### Validation Commands:
```bash
# Lint specification
npx @redocly/cli lint proto/openapi/{directory}-domain/main.yaml

# Generate Go code
ogen --target /tmp/codegen --package api --clean proto/openapi/{directory}-domain/main.yaml

# Build generated code
cd /tmp/codegen && go mod init test && go build .
```

**Priority:** High - Part of enterprise-grade OpenAPI reorganization.
"""

            # Create issue
            issue_id, issue_number = create_github_issue(title, body)

            if issue_id and issue_number:
                # Wait for issue to be added to project automatically
                time.sleep(3)

                # Find the project item
                # Note: In real implementation, you'd need to query the project API to find the item_id
                # For now, we'll assume the automation adds it and we can update fields

                print(f"Issue #{issue_number} created successfully")
                print(f"Title: {title}")
                print(f"Files: {file_count}")
                print("-" * 50)

                # In a real implementation, you'd need to:
                # 1. Query project items to find the item_id for this issue
                # 2. Update the fields (Status: Todo, Agent: API, Type: API, Check: 0)

        time.sleep(1)  # Rate limiting

if __name__ == "__main__":
    if not GITHUB_TOKEN:
        print("GITHUB_TOKEN environment variable required")
        exit(1)

    main()
