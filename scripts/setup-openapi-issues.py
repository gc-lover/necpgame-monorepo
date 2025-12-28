#!/usr/bin/env python3
"""
Script to setup GitHub Project fields for OpenAPI refactoring issues.
"""

import requests
import time
import os

# GitHub configuration
GITHUB_TOKEN = os.getenv('GITHUB_TOKEN')
REPO_OWNER = 'gc-lover'
REPO_NAME = 'necpgame-monorepo'
PROJECT_NUMBER = 1
OWNER_TYPE = 'user'

# Field IDs
STATUS_FIELD_ID = 239690516
AGENT_FIELD_ID = 243899542
TYPE_FIELD_ID = 246469155
CHECK_FIELD_ID = 246468990

# Option IDs
STATUS_TODO = 'f75ad846'
AGENT_API = '6aa5d9af'
TYPE_API = '66f88b2c'
CHECK_NOT_CHECKED = '22932cc7'

def find_project_item_by_title(title_keyword):
    """Find project item by title keyword"""
    url = f"https://api.github.com/users/{REPO_OWNER}/projectsV2/{PROJECT_NUMBER}/items"

    headers = {
        'Authorization': f'token {GITHUB_TOKEN}',
        'Accept': 'application/vnd.github.v3+json'
    }

    params = {
        'per_page': 100
    }

    response = requests.get(url, headers=headers, params=params)

    if response.status_code == 200:
        items = response.json()
        for item in items:
            if 'fields' in item:
                for field in item['fields']:
                    if field.get('name') == 'Title':
                        title = field.get('value', {}).get('raw', '')
                        if title_keyword.lower() in title.lower():
                            return item['id']
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
        print(f"Failed to update field {field_id}: {response.status_code}")
        return False

def setup_issue_fields(title_keyword):
    """Setup all fields for an issue"""
    item_id = find_project_item_by_title(title_keyword)

    if not item_id:
        print(f"Could not find project item for: {title_keyword}")
        return False

    print(f"Setting up fields for item {item_id} ({title_keyword})")

    # Update Status
    update_project_field(item_id, STATUS_FIELD_ID, STATUS_TODO)

    # Update Agent
    update_project_field(item_id, AGENT_FIELD_ID, AGENT_API)

    # Update Type
    update_project_field(item_id, TYPE_FIELD_ID, TYPE_API)

    # Update Check
    update_project_field(item_id, CHECK_FIELD_ID, CHECK_NOT_CHECKED)

    print(f"Completed setup for: {title_keyword}")
    return True

def main():
    """Setup fields for all OpenAPI refactoring issues"""

    issues_to_setup = [
        "system-domain",
        "specialized-domain",
        "social-domain",
        "world-domain",
        "economy-domain",
        "cyberpunk-domain",
        "faction-domain"
    ]

    for issue_title in issues_to_setup:
        setup_issue_fields(issue_title)
        time.sleep(1)  # Rate limiting

if __name__ == "__main__":
    if not GITHUB_TOKEN:
        print("GITHUB_TOKEN environment variable required")
        exit(1)

    main()
