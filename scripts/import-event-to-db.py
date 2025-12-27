#!/usr/bin/env python3
"""
Event Import to Database Script
Import event YAML files to Liquibase migrations for database insertion.

Issue: Based on import-quest-to-db.py
"""

import hashlib
import json
import uuid
from datetime import datetime
from pathlib import Path
from typing import Dict, Any, Optional

import yaml

from core.base_script import BaseScript


class EventImportScript(BaseScript):
    """
    Import event definitions from YAML files to Liquibase migrations.
    """

    def __init__(self):
        super().__init__(
            name="import-event-to-db",
            description="Import event YAML files to database via Liquibase migrations"
        )

    def add_script_args(self):
        """Add script-specific arguments"""
        self.parser.add_argument(
            '--event-file', '-f',
            type=str,
            required=True,
            help='Path to event YAML file to import'
        )
        self.parser.add_argument(
            '--output-dir', '-o',
            type=str,
            default='infrastructure/liquibase/data/gameplay/events',
            help='Output directory for migration files'
        )
        self.parser.add_argument(
            '--force', '-F',
            action='store_true',
            help='Overwrite existing migration file'
        )

    def run(self):
        """Main import logic"""
        args = self.parse_args()

        event_file = Path(args.event_file)
        output_dir = Path(args.output_dir)

        # Validate input file
        if not event_file.exists():
            self.logger.error(f"Event file not found: {event_file}")
            return

        if not event_file.is_file():
            self.logger.error(f"Not a file: {event_file}")
            return

        # Ensure output directory exists
        output_dir.mkdir(parents=True, exist_ok=True)

        # Load and parse event YAML
        try:
            with open(event_file, 'r', encoding='utf-8') as f:
                event_data = yaml.safe_load(f)
        except Exception as e:
            self.logger.error(f"Failed to parse YAML file: {e}")
            return

        # Validate event structure
        if not self._validate_event_structure(event_data):
            return

        # Generate migration
        migration_data = self._generate_migration(event_data, event_file)

        # Write migration file
        migration_file = self._generate_migration_filename(event_data, output_dir)

        if migration_file.exists() and not args.force:
            self.logger.error(f"Migration file already exists: {migration_file}")
            self.logger.error("Use --force to overwrite")
            return

        try:
            with open(migration_file, 'w', encoding='utf-8') as f:
                yaml.safe_dump(migration_data, f, default_flow_style=False, allow_unicode=True, indent=2)

            self.logger.info(f"Generated migration file: {migration_file}")

            if not args.dry_run:
                self.logger.info("Migration file created successfully")

        except Exception as e:
            self.logger.error(f"Failed to write migration file: {e}")

    def _validate_event_structure(self, event_data: Dict[str, Any]) -> bool:
        """Validate event YAML structure"""
        if not isinstance(event_data, dict):
            self.logger.error("Event data must be a dictionary")
            return False

        metadata = event_data.get('metadata', {})
        if not metadata:
            self.logger.error("Missing metadata section")
            return False

        if 'id' not in metadata:
            self.logger.error("Missing id in metadata")
            return False

        return True

    def _generate_migration(self, event_data: Dict[str, Any], event_file: Path) -> Dict[str, Any]:
        """Generate Liquibase migration data"""
        metadata = event_data['metadata']

        # Create migration structure
        migration_data = {
            'databaseChangeLog': [{
                'changeSet': {
                    'id': f"event-{metadata['id']}-{datetime.now().strftime('%Y%m%d%H%M%S')}",
                    'author': 'backend-agent',
                    'changes': [{
                        'insert': {
                            'tableName': 'events',
                            'columns': [
                                {'column': {'name': 'id', 'value': str(uuid.uuid4())}},
                                {'column': {'name': 'event_id', 'value': metadata['id']}},
                                {'column': {'name': 'title', 'value': metadata['title']}},
                                {'column': {'name': 'content', 'value': yaml.safe_dump(event_data, default_flow_style=False, allow_unicode=True)}},
                                {'column': {'name': 'created_at', 'value': datetime.now().isoformat()}},
                                {'column': {'name': 'updated_at', 'value': datetime.now().isoformat()}}
                            ]
                        }
                    }]
                }
            }]
        }

        return migration_data

    def _generate_migration_filename(self, event_data: Dict[str, Any], output_dir: Path) -> Path:
        """Generate migration filename"""
        metadata = event_data['metadata']
        event_id = metadata['id']

        # Create hash for uniqueness
        hash_input = f"{event_id}-{datetime.now().isoformat()}"
        hash_obj = hashlib.md5(hash_input.encode())
        hash_suffix = hash_obj.hexdigest()[:8]

        timestamp = datetime.now().strftime('%Y%m%d%H%M%S')

        filename = f"data_events_{event_id.replace('-', '_')}_{hash_suffix}_{timestamp}.yaml"
        return output_dir / filename


if __name__ == "__main__":
    EventImportScript().run()
