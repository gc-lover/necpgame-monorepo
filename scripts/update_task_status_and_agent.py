#!/usr/bin/env python3
"""
NECPGAME - Update GitHub Project Task Status and Agent
Script to automatically update task status and agent assignment
"""

import requests
import json
import os
from datetime import datetime

# GitHub API configuration
GITHUB_TOKEN = os.getenv('GITHUB_TOKEN')
REPO_OWNER = 'gc-lover'
REPO_NAME = 'necpgame-monorepo'

# Project field IDs from config
STATUS_FIELD_ID = 239690516
AGENT_FIELD_ID = 243899542

# Status option IDs
STATUS_OPTIONS = {
    'Returned': 'c01c12e9',
    'Todo': 'f75ad846',
    'In Progress': '83d488e7',
    'Review': '55060662',
    'Blocked': 'af634d5b',
    'Done': '98236657',
}

# Agent option IDs
AGENT_OPTIONS = {
    'Idea': '8c3f5f11',
    'Content': 'd3cae8d8',
    'Backend': '1fc13998',
    'Architect': 'd109c7f9',
    'API': '6aa5d9af',
    'DB': '1e745162',
    'QA': '3352c488',
    'Performance': 'd16ede50',
    'Security': '12586c50',
    'Network': 'c60ebab1',
    'DevOps': '7e67a39b',
    'UI/UX': '98c65039',
    'UE5': '56920475',
    'GameBalance': '12e8fb71',
    'Release': 'f5878f68',
}

def update_project_item(item_id, status=None, agent=None):
    """Update GitHub Project item status and/or agent"""
    url = f'https://api.github.com/graphql'
    headers = {
        'Authorization': f'token {GITHUB_TOKEN}',
        'Accept': 'application/vnd.github.v3+json'
    }

    mutations = []

    if status:
        status_value = STATUS_OPTIONS.get(status)
        if status_value:
            mutations.append(f"""
            updateStatus: updateProjectV2ItemFieldValue(
                input: {{
                    projectId: "PVT_kwHODCWAw84BIyie"
                    itemId: "{item_id}"
                    fieldId: "{STATUS_FIELD_ID}"
                    value: {{
                        singleSelectOptionId: "{status_value}"
                    }}
                }}
            ) {{
                projectV2Item {{
                    id
                }}
            }}
            """)

    if agent:
        agent_value = AGENT_OPTIONS.get(agent)
        if agent_value:
            mutations.append(f"""
            updateAgent: updateProjectV2ItemFieldValue(
                input: {{
                    projectId: "PVT_kwHODCWAw84BIyie"
                    itemId: "{item_id}"
                    fieldId: "{AGENT_FIELD_ID}"
                    value: {{
                        singleSelectOptionId: "{agent_value}"
                    }}
                }}
            ) {{
                projectV2Item {{
                    id
                }}
            }}
            """)

    if mutations:
        mutation = f"""
        mutation {{
            {' '.join(mutations)}
        }}
        """

        response = requests.post(url, headers=headers, json={'query': mutation})
        if response.status_code == 200:
            print(f"✅ Successfully updated item {item_id}: status={status}, agent={agent}")
            return True
        else:
            print(f"❌ Failed to update item {item_id}: {response.status_code} - {response.text}")
            return False

    return False

def main():
    """Main function to demonstrate status and agent updates"""
    if not GITHUB_TOKEN:
        print("[ERROR] GITHUB_TOKEN environment variable not set")
        return

    # Example: Update task to Done status with Backend agent
    item_id = "PVTI_lAHODCWAw84BIyiezghllnU"  # Example item ID

    print("🎯 Updating task status and agent assignment...")
    print(f"Item ID: {item_id}")
    print("New Status: Done")
    print("New Agent: Backend")
    print()

    success = update_project_item(item_id, status="Done", agent="Backend")

    if success:
        print("\n✅ Task successfully updated!")
        print("Status: Done")
        print("Agent: Backend")
    else:
        print("\n❌ Failed to update task")

if __name__ == '__main__':
    main()
