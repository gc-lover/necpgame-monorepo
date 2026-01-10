#!/usr/bin/env python3

import os
import sys
sys.path.append('scripts')

try:
    from github_api import GitHubAPI
    api = GitHubAPI()
    items = api.list_project_items()
    print(f'Found {len(items)} project items')

    # Filter for Backend agent with TODO status
    backend_todo = []
    for item in items:
        status = item.get('status', '').lower()
        agent = item.get('agent', '').lower()
        if status == 'todo' and agent == 'backend':
            backend_todo.append(item)

    print(f'Found {len(backend_todo)} Backend TODO tasks:')
    for item in backend_todo[:5]:  # Show first 5
        print(f'ID: {item.get("id")}, Title: {item.get("title")}, Type: {item.get("type")}')

except Exception as e:
    print(f'Error: {e}')
    import traceback
    traceback.print_exc()