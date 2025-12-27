#!/usr/bin/env python3
"""
Batch update Astana quest tasks to QA agent.
"""

import requests
import sys
from pathlib import Path

# Add scripts to path for imports
sys.path.insert(0, str(Path(__file__).parent))

from core.config import ConfigManager

class GitHubProjectUpdater:
    """Update GitHub project items via REST API"""

    def __init__(self):
        self.config = ConfigManager()
        self.token = self.config.get_github_token()
        self.headers = {
            'Authorization': f'Bearer {self.token}',
            'Accept': 'application/vnd.github+json',
            'X-GitHub-Api-Version': '2022-11-28'
        }

    def update_project_item(self, project_id, item_id, field_id, field_value):
        """Update a single field in a project item"""
        url = f'https://api.github.com/projects/{project_id}/items/{item_id}/fields/{field_id}'

        data = {"value": field_value}

        response = requests.patch(url, headers=self.headers, json=data)

        if response.status_code == 200:
            print(f"[OK] Updated item {item_id}, field {field_id}")
            return True
        else:
            print(f"[ERROR] Failed to update item {item_id}, field {field_id}: {response.status_code}")
            return False

def main():
    updater = GitHubProjectUpdater()

    # Project and field IDs
    project_id = "1"
    status_field_id = "239690516"
    agent_field_id = "243899542"

    # Status and agent values
    todo_status_value = "f75ad846"  # Todo
    qa_agent_value = "3352c488"    # QA

    # Astana task IDs
    astana_tasks = [
        140922215, 140922217, 140922220, 140922223,
        140922230, 140922231, 140922234, 140922235, 140922238
    ]

    print(f"[INFO] Updating {len(astana_tasks)} Astana tasks to Todo status and QA agent...")

    success_count = 0

    for task_id in astana_tasks:
        print(f"[INFO] Updating task {task_id}...")

        # Update status to Todo
        if updater.update_project_item(project_id, task_id, status_field_id, todo_status_value):
            success_count += 1
        else:
            continue

        # Update agent to QA
        if updater.update_project_item(project_id, task_id, agent_field_id, qa_agent_value):
            success_count += 1

    print(f"[SUMMARY] Successfully completed {success_count}/{len(astana_tasks) * 2} operations")

if __name__ == "__main__":
    main()
