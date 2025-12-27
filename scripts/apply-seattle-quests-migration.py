#!/usr/bin/env python3
"""
Apply Seattle Quests Migration Script
Apply the newly imported Seattle quest migrations to database.

Issue: #2227
"""

import os
import yaml
import psycopg2
from psycopg2.extras import Json
from pathlib import Path
from typing import Dict, Any, List
import json

from core.base_script import BaseScript


class ApplySeattleQuestsMigrationScript(BaseScript):
    """
    Apply Seattle quest migrations to database.
    """

    def __init__(self):
        super().__init__(
            name="apply-seattle-quests-migration",
            description="Apply Seattle quest migrations to database"
        )

    def add_script_args(self):
        """Add script-specific arguments"""
        pass  # No additional arguments needed

    def get_seattle_migration_files(self) -> List[Path]:
        """Get all Seattle quest migration files"""
        migrations_dir = Path("infrastructure/liquibase/data/gameplay/quests")
        seattle_files = []

        for file_path in migrations_dir.glob("data_quests_canon-quest-seattle*"):
            if "amazon-shadow-hackers" in file_path.name or \
               "underground-ripperdoc" in file_path.name or \
               "eco-protest-revolution" in file_path.name or \
               "virtual-reality-neural-dreams" in file_path.name or \
               "rain-city-street-racing" in file_path.name:
                seattle_files.append(file_path)

        return seattle_files

    def parse_yaml_migration(self, file_path: Path) -> Dict[str, Any]:
        """Parse YAML migration file"""
        with open(file_path, 'r', encoding='utf-8') as f:
            data = yaml.safe_load(f)

        return data

    def extract_insert_data(self, migration_data: Dict[str, Any]) -> Dict[str, Any]:
        """Extract INSERT data from migration"""
        changes = migration_data.get('databaseChangeLog', [{}])[0].get('changeSet', {}).get('changes', [])

        if not changes or not changes[0].get('insert'):
            return None

        insert_data = changes[0]['insert']
        columns = insert_data.get('columns', [])
        table_name = insert_data.get('tableName')

        # Extract column values
        row_data = {}
        for col in columns:
            col_name = col['column']['name']
            col_value = col['column']['value']

            # Handle JSON columns
            if col_name in ['metadata', 'rewards', 'objectives']:
                try:
                    # Try to parse as JSON if it's a string
                    if isinstance(col_value, str):
                        col_value = json.loads(col_value)
                    row_data[col_name] = col_value
                except (json.JSONDecodeError, TypeError):
                    row_data[col_name] = col_value
            else:
                row_data[col_name] = col_value

        return {
            'table': table_name,
            'data': row_data
        }

    def insert_quest_data(self, conn, table_name: str, data: Dict[str, Any]):
        """Insert quest data into database"""
        # Prepare columns and values
        columns = list(data.keys())
        values = [data[col] for col in columns]

        # Build query
        placeholders = ', '.join(['%s'] * len(columns))
        query = f"INSERT INTO {table_name} ({', '.join(columns)}) VALUES ({placeholders})"

        # Handle JSON columns
        for i, col in enumerate(columns):
            if col in ['metadata', 'rewards', 'objectives']:
                if isinstance(values[i], (dict, list)):
                    values[i] = Json(values[i])
                elif isinstance(values[i], str):
                    try:
                        parsed = json.loads(values[i])
                        values[i] = Json(parsed)
                    except json.JSONDecodeError:
                        values[i] = values[i]

        with conn.cursor() as cursor:
            cursor.execute(query, values)

    def run_script(self):
        """Main script execution"""
        self.logger.info("Starting apply-seattle-quests-migration")

        # Database connection parameters
        conn_params = {
            'host': 'localhost',
            'port': '5432',
            'database': 'necpgame',
            'user': 'postgres',
            'password': 'postgres'
        }
        conn = psycopg2.connect(**conn_params)

        try:
            # Get Seattle migration files
            migration_files = self.get_seattle_migration_files()
            self.logger.info(f"Found {len(migration_files)} Seattle quest migration files")

            applied_count = 0

            # Process each migration file
            for file_path in migration_files:
                self.logger.info(f"Processing migration file: {file_path.name}")

                try:
                    # Parse YAML
                    migration_data = self.parse_yaml_migration(file_path)

                    # Extract INSERT data
                    insert_info = self.extract_insert_data(migration_data)

                    if insert_info:
                        table_name = insert_info['table']
                        data = insert_info['data']

                        self.logger.info(f"Inserting quest: {data.get('quest_id', 'unknown')}")

                        # Insert data
                        self.insert_quest_data(conn, table_name, data)
                        applied_count += 1

                        self.logger.info(f"Successfully applied migration: {file_path.name}")
                    else:
                        self.logger.warning(f"No INSERT data found in {file_path.name}")

                except Exception as e:
                    self.logger.error(f"Failed to apply migration {file_path.name}: {e}")
                    # Continue with other migrations

            # Commit all changes
            conn.commit()
            self.logger.info(f"Successfully applied {applied_count} Seattle quest migrations")

        except Exception as e:
            self.logger.error(f"Script execution failed: {e}")
            conn.rollback()
            raise
        finally:
            conn.close()

        self.logger.info("Completed apply-seattle-quests-migration")


if __name__ == "__main__":
    script = ApplySeattleQuestsMigrationScript()
    script.run_script()
