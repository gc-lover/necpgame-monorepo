#!/usr/bin/env python3
"""
Tokyo Part 3 Quests Import Script
Import Tokyo Part 3 quest definitions from YAML files to database.

Issue: #140893161
"""

import hashlib
import json
import os
import uuid
from datetime import datetime
from pathlib import Path
from typing import Dict, Any, List, Optional

import yaml

from core.base_script import BaseScript


class TokyoPart3QuestImportScript(BaseScript):
    """
    Import Tokyo Part 3 quest definitions from YAML files to database.
    """

    def __init__(self):
        super().__init__(
            name="import-tokyo-part3-quests",
            description="Import Tokyo Part 3 quest YAML files to database"
        )

    def add_script_args(self):
        """Add script-specific arguments"""
        self.parser.add_argument(
            '--source-dir', '-s',
            type=str,
            default='knowledge/canon/lore/timeline-author/quests/asia/tokyo/part3-2094-2100',
            help='Source directory containing quest YAML files'
        )
        self.parser.add_argument(
            '--output-dir', '-o',
            type=str,
            default='infrastructure/liquibase/data/gameplay/quests',
            help='Output directory for migration files'
        )
        self.parser.add_argument(
            '--force', '-F',
            action='store_true',
            help='Overwrite existing migration files'
        )

    def validate_quest_file(self, file_path: Path) -> Dict[str, Any]:
        """
        Validate and parse quest YAML file.

        Args:
            file_path: Path to quest YAML file

        Returns:
            Parsed quest data

        Raises:
            ValueError: If file is invalid
        """
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                data = yaml.safe_load(f)

            # Validate required fields
            required_fields = ['metadata', 'content']
            for field in required_fields:
                if field not in data:
                    raise ValueError(f"Missing required field: {field}")

            metadata = data['metadata']
            required_metadata = ['id', 'title', 'status', 'version']
            for field in required_metadata:
                if field not in metadata:
                    raise ValueError(f"Missing required metadata field: {field}")

            content = data['content']
            if 'sections' not in content:
                raise ValueError("Missing content.sections")

            return data

        except yaml.YAMLError as e:
            raise ValueError(f"YAML parsing error in {file_path}: {e}")
        except Exception as e:
            raise ValueError(f"Error reading {file_path}: {e}")

    def extract_quest_data(self, quest_data: Dict[str, Any]) -> Dict[str, Any]:
        """
        Extract quest data for database insertion.

        Args:
            quest_data: Parsed quest YAML data

        Returns:
            Structured quest data for database
        """
        metadata = quest_data['metadata']
        content = quest_data['content']

        # Extract basic quest info
        quest_info = {
            'quest_id': metadata['id'],
            'title': metadata['title'],
            'description': content.get('description', ''),
            'quest_type': 'main',  # Default to main quest
            'difficulty_level': 'medium',  # Default difficulty
            'era': '2094-2100',
            'location': 'Tokyo',
            'estimated_duration_minutes': 120,  # Default 2 hours
            'neural_requirement': 5,  # Default neural level
            'cyberware_tier_requirement': 'advanced',
            'is_active': True,
            'metadata': quest_data
        }

        # Extract objectives from content
        objectives = []
        if 'sections' in content:
            for section in content['sections']:
                if section.get('id') == 'objectives':
                    section_body = section.get('body', '')
                    # Parse objectives from text (this is a simple parser)
                    if isinstance(section_body, str):
                        lines = section_body.split('\n')
                        for i, line in enumerate(lines):
                            line = line.strip()
                            if line.startswith('- ') or line.startswith('• '):
                                objective_text = line[2:].strip()
                                objectives.append({
                                    'objective_id': f"{metadata['id']}-obj-{i+1:03d}",
                                    'title': objective_text[:100],  # Limit title length
                                    'description': objective_text,
                                    'objective_type': 'main',
                                    'is_required': True,
                                    'order_index': i + 1
                                })

        quest_info['objectives'] = objectives

        # Extract rewards if available
        rewards = []
        for section in content.get('sections', []):
            if section.get('id') == 'rewards':
                section_body = section.get('body', '')
                if isinstance(section_body, str):
                    lines = section_body.split('\n')
                    for line in lines:
                        line = line.strip()
                        if line.startswith('- ') or line.startswith('• '):
                            reward_text = line[2:].strip()
                            rewards.append({
                                'reward_type': 'item',  # Default type
                                'reward_id': reward_text.lower().replace(' ', '-'),
                                'quantity': 1,
                                'probability': 1.0,
                                'is_guaranteed': True
                            })

        quest_info['rewards'] = rewards

        return quest_info

    def generate_migration_sql(self, quests_data: List[Dict[str, Any]]) -> str:
        """
        Generate Liquibase migration SQL for Tokyo Part 3 quests.

        Args:
            quests_data: List of quest data dictionaries

        Returns:
            Migration SQL string
        """
        migration_header = f"""-- Issue: #140893161
-- liquibase formatted sql

--changeset backend:tokyo-part3-quests-data-import dbms:postgresql
--comment: Import Tokyo Part 3 quest data for 2094-2100 era

BEGIN;

"""

        quest_inserts = []
        objective_inserts = []
        reward_inserts = []
        prerequisite_inserts = []

        for quest_data in quests_data:
            # Quest definition insert
            metadata_json = json.dumps(quest_data['metadata'], ensure_ascii=False, indent=2)
            quest_insert = f"""-- {quest_data['title']}
INSERT INTO gameplay.quest_definitions (
    quest_id,
    title,
    description,
    quest_type,
    difficulty_level,
    era,
    location,
    estimated_duration_minutes,
    neural_requirement,
    cyberware_tier_requirement,
    is_active,
    created_at,
    updated_at,
    metadata
) VALUES (
    '{quest_data['quest_id']}',
    '{quest_data['title']}',
    '{quest_data['description']}',
    '{quest_data['quest_type']}',
    '{quest_data['difficulty_level']}',
    '{quest_data['era']}',
    '{quest_data['location']}',
    {quest_data['estimated_duration_minutes']},
    {quest_data['neural_requirement']},
    '{quest_data['cyberware_tier_requirement']}',
    {str(quest_data['is_active']).lower()},
    NOW(),
    NOW(),
    '{metadata_json}'::jsonb
);"""
            quest_inserts.append(quest_insert)

            # Objectives
            for obj in quest_data.get('objectives', []):
                obj_insert = f"""INSERT INTO gameplay.quest_objectives (
    quest_id,
    objective_id,
    title,
    description,
    objective_type,
    is_required,
    order_index,
    created_at,
    updated_at
) VALUES (
    '{quest_data['quest_id']}',
    '{obj['objective_id']}',
    '{obj['title']}',
    '{obj['description']}',
    '{obj['objective_type']}',
    {str(obj['is_required']).lower()},
    {obj['order_index']},
    NOW(),
    NOW()
);"""
                objective_inserts.append(obj_insert)

            # Rewards
            for reward in quest_data.get('rewards', []):
                reward_insert = f"""INSERT INTO gameplay.quest_rewards (
    quest_id,
    reward_type,
    reward_id,
    quantity,
    probability,
    is_guaranteed,
    created_at,
    updated_at
) VALUES (
    '{quest_data['quest_id']}',
    '{reward['reward_type']}',
    '{reward['reward_id']}',
    {reward['quantity']},
    {reward['probability']},
    {str(reward['is_guaranteed']).lower()},
    NOW(),
    NOW()
);"""
                reward_inserts.append(reward_insert)

        # Combine all inserts
        migration_body = "\n\n".join([
            "\n".join(quest_inserts),
            "\n".join(objective_inserts),
            "\n".join(reward_inserts)
        ])

        migration_footer = "\n\nCOMMIT;"

        return migration_header + migration_body + migration_footer

    def run_script(self):
        """Main script execution"""
        args = self.args

        source_dir = Path(args.source_dir)
        if not source_dir.exists():
            self.logger.error(f"Source directory does not exist: {source_dir}")
            return 1

        output_dir = Path(args.output_dir)
        output_dir.mkdir(parents=True, exist_ok=True)

        # Find all YAML files in source directory
        quest_files = list(source_dir.glob("*.yaml"))
        if not quest_files:
            self.logger.warning(f"No YAML files found in {source_dir}")
            return 0

        self.logger.info(f"Found {len(quest_files)} quest files to process")

        quests_data = []

        # Process each quest file
        for quest_file in quest_files:
            try:
                self.logger.info(f"Processing {quest_file.name}")

                # Validate and parse file
                quest_data = self.validate_quest_file(quest_file)

                # Extract structured data
                quest_info = self.extract_quest_data(quest_data)
                quests_data.append(quest_info)

                self.logger.info(f"Successfully processed {quest_file.name}")

            except Exception as e:
                self.logger.error(f"Failed to process {quest_file}: {e}")
                if not args.force:
                    return 1

        if not quests_data:
            self.logger.warning("No valid quest data to import")
            return 0

        # Generate migration SQL
        migration_sql = self.generate_migration_sql(quests_data)

        # Write migration file
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        migration_filename = f"V1_57__tokyo_part3_quests_data_import_{timestamp}.sql"
        migration_path = output_dir / migration_filename

        if migration_path.exists() and not args.force:
            self.logger.error(f"Migration file already exists: {migration_path}")
            self.logger.error("Use --force to overwrite")
            return 1

        with open(migration_path, 'w', encoding='utf-8') as f:
            f.write(migration_sql)

        self.logger.info(f"Generated migration file: {migration_path}")
        self.logger.info(f"Processed {len(quests_data)} quests")

        return 0


def main():
    """Script entry point"""
    script = TokyoPart3QuestImportScript()
    return script.run()


if __name__ == "__main__":
    exit(main())

