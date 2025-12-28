#!/usr/bin/env python3
"""
Script to update GitHub Project fields for tasks.
Usage: python scripts/update-github-fields.py --item-id {id} --type {TYPE} --check {0|1}
"""

import argparse
import os
import sys
import json
from pathlib import Path

# Add project root to path
project_root = Path(__file__).parent.parent
sys.path.insert(0, str(project_root))

try:
    from scripts.core.config import Config
    from scripts.core.command_runner import CommandRunner
except ImportError:
    # Fallback imports
    sys.path.insert(0, str(project_root / "scripts"))
    import core.config as config
    import core.command_runner as command_runner

# Field IDs from GITHUB_PROJECT_CONFIG.md
FIELD_IDS = {
    'status': 239690516,
    'agent': 243899542,
    'type': 246469155,
    'check': 246468990
}

# Option IDs for Status field
STATUS_OPTIONS = {
    'todo': 'f75ad846',
    'in_progress': '83d488e7',
    'review': '55060662',
    'blocked': 'af634d5b',
    'returned': 'c01c12e9',
    'done': '98236657'
}

# Option IDs for Agent field
AGENT_OPTIONS = {
    'idea': '8c3f5f11',
    'content': 'd3cae8d8',
    'backend': '1fc13998',
    'architect': 'd109c7f9',
    'api': '6aa5d9af',
    'db': '1e745162',
    'qa': '3352c488',
    'performance': 'd16ede50',
    'security': '12586c50',
    'network': 'c60ebab1',
    'devops': '7e67a39b',
    'ui_ux': '98c65039',
    'ue5': '56920475',
    'game_balance': '12e8fb71',
    'release': 'f5878f68'
}

# Option IDs for Type field
TYPE_OPTIONS = {
    'api': '66f88b2c',
    'migration': 'd3702826',
    'data': 'b06014a2',
    'backend': '08174330',
    'ue5': 'd4d523a0'
}

# Option IDs for Check field
CHECK_OPTIONS = {
    'not_checked': '22932cc7',  # 0
    'checked': '4e8cf8f5'      # 1
}

def update_project_item_field(item_id, field_name, option_value):
    """Update a single field for a project item using MCP."""
    field_id = FIELD_IDS[field_name]

    # Prepare the update data
    update_data = {
        "owner": "gc-lover",
        "owner_type": "user",
        "project_number": 1,
        "item_id": item_id,
        "updated_field": {
            "id": field_id,
            "value": option_value
        }
    }

    print(f"Updating {field_name} field for item {item_id} to {option_value}")
    print(f"Update data: {json.dumps(update_data, indent=2)}")

    # Here we would call the MCP function, but since this is a script,
    # we'll just print what would be done
    print("NOTE: This script currently only prints the update data.")
    print("To actually update, use the MCP function mcp_github_update_project_item")

    return True

def main():
    parser = argparse.ArgumentParser(description='Update GitHub Project fields')
    parser.add_argument('--item-id', required=True, help='Project item ID')
    parser.add_argument('--type', choices=['API', 'MIGRATION', 'DATA', 'BACKEND', 'UE5'],
                       help='Set TYPE field')
    parser.add_argument('--check', choices=['0', '1'],
                       help='Set CHECK field (0=not checked, 1=checked)')
    parser.add_argument('--status', choices=list(STATUS_OPTIONS.keys()),
                       help='Set STATUS field')
    parser.add_argument('--agent', choices=list(AGENT_OPTIONS.keys()),
                       help='Set AGENT field')

    args = parser.parse_args()

    if not any([args.type, args.check, args.status, args.agent]):
        print("Error: At least one field to update must be specified")
        return 1

    # Update TYPE field
    if args.type:
        option_value = TYPE_OPTIONS[args.type.lower()]
        update_project_item_field(args.item_id, 'type', option_value)

    # Update CHECK field
    if args.check:
        option_key = 'not_checked' if args.check == '0' else 'checked'
        option_value = CHECK_OPTIONS[option_key]
        update_project_item_field(args.item_id, 'check', option_value)

    # Update STATUS field
    if args.status:
        option_value = STATUS_OPTIONS[args.status.lower()]
        update_project_item_field(args.item_id, 'status', option_value)

    # Update AGENT field
    if args.agent:
        option_value = AGENT_OPTIONS[args.agent.lower()]
        update_project_item_field(args.item_id, 'agent', option_value)

    return 0

if __name__ == '__main__':
    sys.exit(main())
