#!/usr/bin/env python3
"""
Achievement Data Import Script
Imports achievement data from YAML files into the achievement system database.

Usage:
    python import-achievements.py [--dry-run] [--verbose]

Options:
    --dry-run   Show what would be imported without actually importing
    --verbose   Show detailed import progress
"""

import argparse
import os
import sys
import yaml
import psycopg2
from psycopg2.extras import execute_values
import logging
from typing import Dict, List, Any
from pathlib import Path

# Add project root to path for imports
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from scripts.core.config import DatabaseConfig

class AchievementImporter:
    def __init__(self, db_config: DatabaseConfig, dry_run: bool = False, verbose: bool = False):
        self.db_config = db_config
        self.dry_run = dry_run
        self.verbose = verbose
        self.conn = None
        self.cursor = None

        # Setup logging
        level = logging.DEBUG if verbose else logging.INFO
        logging.basicConfig(level=level, format='%(asctime)s - %(levelname)s - %(message)s')
        self.logger = logging.getLogger(__name__)

    def connect_db(self):
        """Connect to the database"""
        try:
            self.conn = psycopg2.connect(
                host=self.db_config.host,
                port=self.db_config.port,
                database=self.db_config.database,
                user=self.db_config.user,
                password=self.db_config.password
            )
            self.cursor = self.conn.cursor()
            self.logger.info("Connected to database successfully")
        except Exception as e:
            self.logger.error(f"Failed to connect to database: {e}")
            raise

    def disconnect_db(self):
        """Disconnect from the database"""
        if self.cursor:
            self.cursor.close()
        if self.conn:
            self.conn.close()
            self.logger.info("Disconnected from database")

    def find_yaml_files(self) -> List[Path]:
        """Find all achievement YAML files"""
        knowledge_dir = Path("knowledge")
        yaml_files = []

        # Search in canon/achievements
        canon_achievements = knowledge_dir / "canon" / "achievements"
        if canon_achievements.exists():
            yaml_files.extend(canon_achievements.glob("*.yaml"))

        # Search in content for quest achievements
        content_quests = knowledge_dir / "content" / "quests"
        if content_quests.exists():
            for quest_file in content_quests.glob("*.yaml"):
                yaml_files.append(quest_file)

        self.logger.info(f"Found {len(yaml_files)} YAML files to process")
        return yaml_files

    def parse_achievement_yaml(self, file_path: Path) -> List[Dict[str, Any]]:
        """Parse achievements from a YAML file"""
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                data = yaml.safe_load(f)

            achievements = []

            # Handle different YAML structures
            if 'achievements' in data:
                # Multiple achievements in one file
                for achievement_data in data['achievements']:
                    achievement = self.transform_achievement_data(achievement_data)
                    if achievement:
                        achievements.append(achievement)
            elif 'id' in data and 'name' in data:
                # Single achievement in file
                achievement = self.transform_achievement_data(data)
                if achievement:
                    achievements.append(achievement)
            else:
                self.logger.warning(f"No achievements found in {file_path}")

            return achievements

        except Exception as e:
            self.logger.error(f"Failed to parse {file_path}: {e}")
            return []

    def transform_achievement_data(self, data: Dict[str, Any]) -> Dict[str, Any]:
        """Transform YAML achievement data to database format"""
        try:
            achievement = {
                'id': data.get('id'),
                'name': data.get('name', ''),
                'description': data.get('description', ''),
                'category': data.get('category', 'general'),
                'icon_url': data.get('icon', ''),
                'points': data.get('points', 0),
                'rarity': data.get('rarity', 'common'),
                'is_hidden': data.get('is_hidden', False),
                'is_active': True,
                'requirements': data.get('requirements', []),
                'rewards': data.get('rewards', [])
            }

            # Validate required fields
            if not achievement['id'] or not achievement['name']:
                self.logger.warning(f"Skipping achievement with missing id or name: {achievement}")
                return None

            return achievement

        except Exception as e:
            self.logger.error(f"Failed to transform achievement data: {e}")
            return None

    def import_achievements(self, achievements: List[Dict[str, Any]]):
        """Import achievements into database"""
        if not achievements:
            return

        if self.dry_run:
            self.logger.info(f"[DRY RUN] Would import {len(achievements)} achievements")
            for ach in achievements[:5]:  # Show first 5
                self.logger.info(f"  - {ach['name']} ({ach['id']})")
            if len(achievements) > 5:
                self.logger.info(f"  ... and {len(achievements) - 5} more")
            return

        try:
            # Prepare data for bulk insert
            achievement_data = []
            for ach in achievements:
                achievement_data.append((
                    ach['id'],
                    ach['name'],
                    ach['description'],
                    ach['category'],
                    ach['icon_url'],
                    ach['points'],
                    ach['rarity'],
                    ach['is_hidden'],
                    ach['is_active']
                ))

            # Bulk insert achievements
            query = """
                INSERT INTO achievements (id, name, description, category, icon_url, points, rarity, is_hidden, is_active, created_at, updated_at)
                VALUES %s
                ON CONFLICT (id) DO UPDATE SET
                    name = EXCLUDED.name,
                    description = EXCLUDED.description,
                    category = EXCLUDED.category,
                    icon_url = EXCLUDED.icon_url,
                    points = EXCLUDED.points,
                    rarity = EXCLUDED.rarity,
                    is_hidden = EXCLUDED.is_hidden,
                    is_active = EXCLUDED.is_active,
                    updated_at = CURRENT_TIMESTAMP
            """

            execute_values(self.cursor, query, achievement_data)
            self.conn.commit()

            self.logger.info(f"Successfully imported {len(achievements)} achievements")

        except Exception as e:
            self.logger.error(f"Failed to import achievements: {e}")
            self.conn.rollback()
            raise

    def run_import(self):
        """Run the complete import process"""
        try:
            self.connect_db()

            # Find and process YAML files
            yaml_files = self.find_yaml_files()
            total_achievements = 0

            for file_path in yaml_files:
                self.logger.info(f"Processing {file_path}")
                achievements = self.parse_achievement_yaml(file_path)

                if achievements:
                    self.import_achievements(achievements)
                    total_achievements += len(achievements)

            self.logger.info(f"Import completed. Total achievements processed: {total_achievements}")

        except Exception as e:
            self.logger.error(f"Import failed: {e}")
            raise
        finally:
            self.disconnect_db()

def main():
    parser = argparse.ArgumentParser(description="Import achievement data from YAML files")
    parser.add_argument("--dry-run", action="store_true", help="Show what would be imported without actually importing")
    parser.add_argument("--verbose", "-v", action="store_true", help="Show detailed import progress")
    parser.add_argument("--config", default="scripts/core/config.py", help="Path to database config file")

    args = parser.parse_args()

    # Load database configuration
    db_config = DatabaseConfig()

    # Run importer
    importer = AchievementImporter(db_config, dry_run=args.dry_run, verbose=args.verbose)
    importer.run_import()

if __name__ == "__main__":
    main()
