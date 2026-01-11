#!/usr/bin/env python3
"""
Script to get tasks from GitHub Project.
Shows tasks with Status: Todo and Status: In Progress for all agents.
"""

import requests
import json
import os
from pathlib import Path

# GitHub configuration
GITHUB_TOKEN = os.getenv('GITHUB_TOKEN')
REPO_OWNER = 'gc-lover'
REPO_NAME = 'necpgame-monorepo'
PROJECT_NUMBER = 1

# Field IDs from config
STATUS_FIELD_ID = 239690516
AGENT_FIELD_ID = 243899542
TYPE_FIELD_ID = 246469155
CHECK_FIELD_ID = 246468990

def get_project_items():
    """Get all items from GitHub Project"""
    url = f"https://api.github.com/graphql"

    headers = {
        'Authorization': f'Bearer {GITHUB_TOKEN}',
        'Content-Type': 'application/json'
    }

    # GraphQL query to get project items with field values
    query = """
    query($owner: String!, $number: Int!) {
      user(login: $owner) {
        projectV2(number: $number) {
          items(first: 100) {
            nodes {
              id
              content {
                ... on Issue {
                  number
                  title
                  body
                }
              }
              fieldValues(first: 10) {
                nodes {
                  ... on ProjectV2ItemFieldSingleSelectValue {
                    field {
                      ... on ProjectV2SingleSelectField {
                        id
                        name
                      }
                    }
                    optionId
                    name
                  }
                }
              }
            }
          }
        }
      }
    }
    """

    variables = {
        "owner": REPO_OWNER,
        "number": PROJECT_NUMBER
    }

    response = requests.post(url, headers=headers, json={"query": query, "variables": variables})

    if response.status_code == 200:
        return response.json()
    else:
        print(f"Failed to get project items: {response.status_code} - {response.text}")
        return None

def parse_project_data(data):
    """Parse GraphQL response into readable task list"""
    if not data or 'data' not in data:
        return []

    items = data['data']['user']['projectV2']['items']['nodes']
    tasks = []

    for item in items:
        if not item.get('content'):
            continue

        content = item['content']
        issue_number = content.get('number')
        title = content.get('title', '')

        # Parse field values
        status = None
        agent = None
        task_type = None
        check = None

        for field_value in item.get('fieldValues', {}).get('nodes', []):
            if field_value.get('field'):
                field_id = field_value['field']['id']
                field_name = field_value['field']['name']
                option_name = field_value.get('name', '')

                if field_id == str(STATUS_FIELD_ID):
                    status = option_name
                elif field_id == str(AGENT_FIELD_ID):
                    agent = option_name
                elif field_id == str(TYPE_FIELD_ID):
                    task_type = option_name
                elif field_id == str(CHECK_FIELD_ID):
                    check = option_name

        # Only include tasks that are Todo or In Progress
        if status in ['Todo', 'In Progress']:
            tasks.append({
                'item_id': item['id'],
                'issue_number': issue_number,
                'title': title,
                'status': status,
                'agent': agent,
                'type': task_type,
                'check': check
            })

    return tasks

def main():
    """Main function"""
    if not GITHUB_TOKEN:
        print("GITHUB_TOKEN environment variable required")
        print("Please set it with: export GITHUB_TOKEN=your_token_here")
        return 1

    print("Fetching tasks from GitHub Project...")
    data = get_project_items()

    if not data:
        return 1

    tasks = parse_project_data(data)

    if not tasks:
        print("No Todo/In Progress tasks found")
        return 0

    print(f"\nFound {len(tasks)} tasks:")
    print("=" * 80)

    for task in tasks:
        print(f"Item ID: {task['item_id']}")
        print(f"Issue: #{task['issue_number']}")
        print(f"Title: {task['title']}")
        print(f"Status: {task['status']}")
        print(f"Agent: {task['agent'] or 'Unassigned'}")
        print(f"Type: {task['type'] or 'Not set'}")
        print(f"Check: {task['check'] or 'Not checked'}")
        print("-" * 40)

    # Save to JSON file for use by other scripts
    output_file = Path("project_items_utf8.json")
    with open(output_file, 'w', encoding='utf-8') as f:
        json.dump(tasks, f, indent=2, ensure_ascii=False)

    print(f"\nSaved {len(tasks)} tasks to {output_file}")

    return 0

if __name__ == "__main__":
    exit(main())