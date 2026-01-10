#!/usr/bin/env python3

import requests
import json
import os
from pathlib import Path

# GitHub API configuration from docs
GITHUB_TOKEN = os.getenv('GITHUB_TOKEN')
OWNER = 'gc-lover'
PROJECT_NUMBER = 1

def get_project_items():
    """Get project items using GitHub REST API"""

    headers = {
        'Authorization': f'Bearer {GITHUB_TOKEN}',
        'Accept': 'application/vnd.github+json',
        'X-GitHub-Api-Version': '2022-11-28'
    }

    # First get project ID
    project_url = f'https://api.github.com/repos/{OWNER}/necpgame-monorepo/projects'
    try:
        response = requests.get(project_url, headers=headers)
        if response.status_code == 200:
            projects = response.json()
            if projects:
                project_id = projects[0]['id']  # Take first project
                print(f"Found project ID: {project_id}")

                # Get project items
                items_url = f'https://api.github.com/projects/{project_id}/columns'
                response = requests.get(items_url, headers=headers)

                if response.status_code == 200:
                    columns = response.json()
                    all_items = []
                    for column in columns:
                        col_items_url = column['cards_url']
                        items_response = requests.get(col_items_url, headers=headers)
                        if items_response.status_code == 200:
                            cards = items_response.json()
                            for card in cards:
                                if 'content_url' in card:
                                    # This is an issue
                                    issue_response = requests.get(card['content_url'], headers=headers)
                                    if issue_response.status_code == 200:
                                        issue = issue_response.json()
                                        all_items.append({
                                            'id': card['id'],
                                            'number': issue.get('number'),
                                            'title': issue.get('title'),
                                            'state': issue.get('state'),
                                            'column': column['name']
                                        })

                    # Save to file
                    with open('project_items.json', 'w', encoding='utf-8') as f:
                        json.dump({'items': all_items}, f, indent=2, ensure_ascii=False)

                    print(f"Saved {len(all_items)} items to project_items.json")

                    # Filter Backend TODO
                    backend_todo = [item for item in all_items if 'Backend' in item.get('title', '') and item.get('state') == 'open']
                    print(f"Found {len(backend_todo)} Backend TODO tasks:")
                    for item in backend_todo[:5]:
                        print(f"#{item['number']}: {item['title']}")

                else:
                    print(f"Error getting columns: {items_response.status_code}")
            else:
                print("No projects found")
        else:
            print(f"Error getting projects: {response.status_code} - {response.text}")
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    if not GITHUB_TOKEN:
        print("GITHUB_TOKEN environment variable not set")
    else:
        get_project_items()