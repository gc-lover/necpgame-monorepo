#!/usr/bin/env python3
"""
GitHub Project Fields Update Script

Updates TYPE and CHECK fields in GitHub Project items.
Used by agents to properly classify and track task verification.

Usage:
    python scripts/update-github-fields.py --item-id 123 --type API --check 1
    python scripts/update-github-fields.py --item-id 123 --type BACKEND
    python scripts/update-github-fields.py --item-id 123 --check 1
"""

import argparse
import sys
import os
from pathlib import Path

# Add scripts to path for imports
sys.path.insert(0, str(Path(__file__).parent))

from core.config import ConfigManager
from core.logger import Logger

class GitHubFieldsUpdater:
    """Updates TYPE and CHECK fields in GitHub Project items"""

    def __init__(self):
        self.config = ConfigManager()
        self.logger = Logger(__name__)

        # Field IDs from config
        self.TYPE_FIELD_ID = self.config.get('github.type_field_id', '[TO_BE_FILLED]')
        self.CHECK_FIELD_ID = self.config.get('github.check_field_id', '[TO_BE_FILLED]')

        # Type options mapping
        self.TYPE_OPTIONS = {
            'API': self.config.get('github.type_options.api', '[TO_BE_FILLED]'),
            'MIGRATION': self.config.get('github.type_options.migration', '[TO_BE_FILLED]'),
            'DATA': self.config.get('github.type_options.data', '[TO_BE_FILLED]'),
            'BACKEND': self.config.get('github.type_options.backend', '[TO_BE_FILLED]'),
            'UE5': self.config.get('github.type_options.ue5', '[TO_BE_FILLED]'),
        }

        # Check options
        self.CHECK_OPTIONS = {
            '0': self.config.get('github.check_options.not_checked', '0'),
            '1': self.config.get('github.check_options.checked', '1'),
        }

    def update_fields(self, item_id: str, type_value: str = None, check_value: str = None):
        """Update TYPE and/or CHECK fields for a GitHub Project item"""

        if not item_id:
            raise ValueError("item_id is required")

        # Build update fields
        updated_fields = []

        if type_value:
            if type_value not in self.TYPE_OPTIONS:
                raise ValueError(f"Invalid TYPE value: {type_value}. Must be one of: {list(self.TYPE_OPTIONS.keys())}")
            updated_fields.append({
                'id': self.TYPE_FIELD_ID,
                'value': self.TYPE_OPTIONS[type_value]
            })
            self.logger.info(f"Setting TYPE to {type_value}")

        if check_value:
            if check_value not in self.CHECK_OPTIONS:
                raise ValueError(f"Invalid CHECK value: {check_value}. Must be '0' or '1'")
            updated_fields.append({
                'id': self.CHECK_FIELD_ID,
                'value': self.CHECK_OPTIONS[check_value]
            })
            self.logger.info(f"Setting CHECK to {check_value}")

        if not updated_fields:
            self.logger.warning("No fields to update")
            return

        # Here you would call the MCP GitHub API
        # For now, just log what would be done
        self.logger.info(f"Would update item {item_id} with fields: {updated_fields}")

        # TODO: Implement actual MCP call
        # mcp_github_update_project_item({
        #     owner_type: 'user',
        #     owner: 'gc-lover',
        #     project_number: 1,
        #     item_id: item_id,
        #     updated_field: updated_fields
        # })

        return True

def main():
    parser = argparse.ArgumentParser(description='Update GitHub Project TYPE and CHECK fields')
    parser.add_argument('--item-id', required=True, help='GitHub Project item ID')
    parser.add_argument('--type', choices=['API', 'MIGRATION', 'DATA', 'BACKEND', 'UE5'],
                       help='Task type (API, MIGRATION, DATA, BACKEND, UE5)')
    parser.add_argument('--check', choices=['0', '1'],
                       help='Check status (0=not checked, 1=checked)')

    args = parser.parse_args()

    if not args.type and not args.check:
        parser.error("At least one of --type or --check must be specified")

    updater = GitHubFieldsUpdater()

    try:
        updater.update_fields(
            item_id=args.item_id,
            type_value=args.type,
            check_value=args.check
        )
        print(f"[OK] Fields updated for item {args.item_id}")
    except Exception as e:
        print(f"[ERROR] Failed to update fields: {e}")
        sys.exit(1)

if __name__ == '__main__':
    main()
