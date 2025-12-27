#!/usr/bin/env python3
"""
Tokyo Part 1 Quests Import Script
Import Tokyo Part 1 quest definitions from YAML files to database.

Issue: #140893158
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


class TokyoPart1QuestImportScript(BaseScript):
    """
    Import Tokyo Part 1 quest definitions from YAML files to database.
    """

    def __init__(self):
        super().__init__(
            name="import-tokyo-part1-quests",
            description="Import Tokyo Part 1 quest YAML files to database"
        )
        self.args = None

    def add_script_args(self):
        """Add script-specific arguments"""
        self.parser.add_argument(
            '--source-dir', '-s',
            type=str,
            default='knowledge/canon/lore/timeline-author/quests/asia/tokyo/2020-2029',
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
            required_fields = ['metadata', 'quest_definition']
            for field in required_fields:
                if field not in data:
                    raise ValueError(f"Missing required field: {field}")

            metadata = data['metadata']
            required_metadata = ['id', 'title']
            for field in required_metadata:
                if field not in metadata:
                    raise ValueError(f"Missing required metadata field: {field}")

            # For this quest format, content is optional, but quest_definition is required
            if 'content' not in data and 'quest_definition' not in data:
                raise ValueError("Missing both content and quest_definition fields")

            return data

        except yaml.YAMLError as e:
            raise ValueError(f"Invalid YAML in {file_path}: {e}")
        except Exception as e:
            raise ValueError(f"Error reading {file_path}: {e}")

    def generate_migration_sql(self, quest_data: Dict[str, Any], file_path: Path) -> str:
        """
        Generate Liquibase migration SQL for quest insertion.

        Args:
            quest_data: Parsed quest data
            file_path: Source file path for tracking

        Returns:
            SQL migration content
        """
        metadata = quest_data['metadata']
        quest_definition = quest_data['quest_definition']
        content = quest_data.get('content', {})

        # Extract values from quest_definition
        quest_type = quest_definition.get('quest_type', 'side')
        level_min = quest_definition.get('level_min', 1)
        level_max = quest_definition.get('level_max')
        rewards = quest_definition.get('rewards', {})
        experience = rewards.get('experience', 0)
        money_data = rewards.get('money', {})
        money_min = money_data.get('min', 0) if isinstance(money_data, dict) else money_data if isinstance(money_data, (int, float)) else 0
        money_max = money_data.get('max', money_min) if isinstance(money_data, dict) else money_min

        # Generate unique IDs
        quest_id = str(uuid.uuid4())

        # Convert datetime objects to strings for JSON serialization
        def serialize_for_json(obj):
            if isinstance(obj, datetime):
                return obj.isoformat()
            return obj

        metadata_hash = hashlib.sha256(json.dumps(metadata, sort_keys=True, default=serialize_for_json).encode()).hexdigest()
        content_hash = hashlib.sha256(json.dumps(quest_definition, sort_keys=True, default=serialize_for_json).encode()).hexdigest()

        # Prepare data for insertion
        quest_definition_json = json.dumps(quest_definition, ensure_ascii=False, indent=2)
        narrative_context = json.dumps(content.get('sections', []), ensure_ascii=False, indent=2)
        gameplay_mechanics = json.dumps({}, ensure_ascii=False, indent=2)

        # Handle optional fields
        additional_npcs = json.dumps([], ensure_ascii=False, indent=2)
        environmental_challenges = json.dumps([], ensure_ascii=False, indent=2)
        visual_design = json.dumps({}, ensure_ascii=False, indent=2)
        cultural_elements = json.dumps({}, ensure_ascii=False, indent=2)

        rollback_sql = "--rollback DELETE FROM gameplay.quests WHERE metadata_id = '{}';".format(metadata['id'])

        sql = f"""--liquibase formatted sql

--changeset tokyo-part1-quests:{metadata['id']} runOnChange:true

INSERT INTO gameplay.quests (
    id,
    metadata_id,
    title,
    english_title,
    type,
    location,
    time_period,
    difficulty,
    estimated_duration,
    player_level_min,
    player_level_max,
    status,
    version,
    quest_definition,
    narrative_context,
    gameplay_mechanics,
    additional_npcs,
    environmental_challenges,
    visual_design,
    cultural_elements,
    metadata_hash,
    content_hash,
    created_at,
    updated_at,
    source_file
) VALUES (
    '{quest_id}',
    '{metadata['id']}',
    '{metadata['title']}',
    '{metadata.get('title', '')}',
    '{quest_type}',
    'Tokyo',
    '2020-2029',
    'easy',
    '30-60 минут',
    {level_min},
    {level_max if level_max else 'NULL'},
    'active',
    '1.0.0',
    '{quest_definition_json}',
    '{narrative_context}',
    '{gameplay_mechanics}',
    '{additional_npcs}',
    '{environmental_challenges}',
    '{visual_design}',
    '{cultural_elements}',
    '{metadata_hash}',
    '{content_hash}',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    '{str(file_path)}'
);

{rollback_sql}

"""

        return sql

    def process_quest_files(self, source_dir: Path, output_dir: Path, force: bool = False) -> List[str]:
        """
        Process all quest files in source directory and generate migrations.

        Args:
            source_dir: Directory containing quest YAML files
            output_dir: Directory to write migration files
            force: Whether to overwrite existing files

        Returns:
            List of generated migration file paths
        """
        output_dir.mkdir(parents=True, exist_ok=True)
        generated_files = []

        # Find all YAML files
        yaml_files = list(source_dir.glob('*.yaml'))
        yaml_files.sort()

        self.logger.info(f"Found {len(yaml_files)} quest files to process")

        for yaml_file in yaml_files:
            try:
                # Validate and parse quest file
                quest_data = self.validate_quest_file(yaml_file)
                metadata = quest_data['metadata']

                # Generate migration filename
                timestamp = datetime.now().strftime('%Y%m%d%H%M%S')
                migration_filename = f"{timestamp}__tokyo_part1_quest_{metadata['id']}.sql"
                migration_path = output_dir / migration_filename

                # Check if file exists
                if migration_path.exists() and not force:
                    self.logger.warning(f"Migration file already exists: {migration_path}")
                    continue

                # Generate SQL migration
                sql_content = self.generate_migration_sql(quest_data, yaml_file)

                # Write migration file
                with open(migration_path, 'w', encoding='utf-8') as f:
                    f.write(sql_content)

                generated_files.append(str(migration_path))
                self.logger.info(f"Generated migration: {migration_path}")

            except Exception as e:
                self.logger.error(f"Failed to process {yaml_file}: {e}")
                continue

        return generated_files

    def run(self):
        """Main execution method"""
        if not hasattr(self, 'args') or self.args is None:
            self.args = self.parse_args()
        args = self.args

        source_dir = Path(args.source_dir)
        output_dir = Path(args.output_dir)

        if not source_dir.exists():
            raise ValueError(f"Source directory does not exist: {source_dir}")

        self.logger.info(f"Importing quests from: {source_dir}")
        self.logger.info(f"Output directory: {output_dir}")

        # Process quest files
        generated_files = self.process_quest_files(source_dir, output_dir, args.force)

        self.logger.info(f"Successfully generated {len(generated_files)} migration files")

        if generated_files:
            self.logger.info("Generated files:")
            for file_path in generated_files:
                self.logger.info(f"  - {file_path}")


def main():
    """Entry point"""
    script = TokyoPart1QuestImportScript()
    script.main()


if __name__ == '__main__':
    main()
