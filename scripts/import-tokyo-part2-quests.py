#!/usr/bin/env python3
"""
Import Tokyo Part 2 quest YAML files to database via Liquibase migrations
"""

import sys
from pathlib import Path
from core.base_script import BaseScript
import json
from datetime import datetime

class TokyoPart2QuestImportScript(BaseScript):
    def __init__(self):
        super().__init__(
            name="import-tokyo-part2-quests",
            description="Import Tokyo Part 2 quest YAML files to database"
        )

    def add_script_args(self):
        self.parser.add_argument(
            '--source-dir', '-s',
            type=str,
            default='knowledge/canon/lore/timeline-author/quests/asia/tokyo/2040-2060',
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

    def validate_quest_file(self, file_path: Path) -> dict:
        """Validate and parse quest YAML file"""
        data = self.load_yaml_file(file_path)

        # Required content fields
        required_fields = ['quest_definition', 'narrative_context']
        for field in required_fields:
            if field not in data:
                raise ValueError(f"Missing required content field: {field}")

        content = data
        metadata = content.get('metadata', {})

        # Convert datetime objects to ISO format strings
        if 'last_updated' in metadata and isinstance(metadata['last_updated'], datetime):
            metadata['last_updated'] = metadata['last_updated'].isoformat()
        if 'processed_at' in metadata and isinstance(metadata['processed_at'], datetime):
            metadata['processed_at'] = metadata['processed_at'].isoformat()

        return content

    def generate_migration_sql(self, quest_data: dict, file_path: Path) -> str:
        """Generate Liquibase SQL migration for quest data"""
        metadata = quest_data.get('metadata', {})
        quest_definition = quest_data.get('quest_definition', {})
        narrative_context = quest_data.get('narrative_context', {})
        gameplay_mechanics = quest_data.get('gameplay_mechanics', {})
        additional_npcs = quest_data.get('additional_npcs', {})
        environmental_challenges = quest_data.get('environmental_challenges', {})
        visual_design = quest_data.get('visual_design', {})
        cultural_elements = quest_data.get('cultural_elements', {})

        quest_id = f"quest_{metadata.get('id', 'unknown')}"
        content_hash = self.calculate_content_hash(quest_data)
        metadata_hash = self.calculate_metadata_hash(metadata)

        # Convert to JSON strings
        quest_definition_json = json.dumps(quest_definition, ensure_ascii=False, indent=2)
        narrative_context_json = json.dumps(narrative_context, ensure_ascii=False, indent=2)
        gameplay_mechanics_json = json.dumps(gameplay_mechanics, ensure_ascii=False, indent=2)
        additional_npcs_json = json.dumps(additional_npcs, ensure_ascii=False, indent=2)
        environmental_challenges_json = json.dumps(environmental_challenges, ensure_ascii=False, indent=2)
        visual_design_json = json.dumps(visual_design, ensure_ascii=False, indent=2)
        cultural_elements_json = json.dumps(cultural_elements, ensure_ascii=False, indent=2)

        sql = f"""--liquibase formatted sql

--changeset tokyo-part2-quests:{metadata['id']} runOnChange:true

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
    '{metadata.get('english_title', metadata['title'])}',
    '{metadata.get('type', 'urban_exploration')}',
    '{metadata.get('location', 'Tokyo')}',
    '{metadata.get('time_period', '2040-2060')}',
    '{metadata.get('difficulty', 'medium')}',
    '{metadata.get('estimated_duration', '45-90 минут')}',
    {metadata.get('player_level_min', 10)},
    {metadata.get('player_level_max', 40)},
    '{quest_definition.get('status', 'active')}',
    '{metadata.get('version', '1.0')}',
    '{quest_definition_json}',
    '{narrative_context_json}',
    '{gameplay_mechanics_json}',
    '{additional_npcs_json}',
    '{environmental_challenges_json}',
    '{visual_design_json}',
    '{cultural_elements_json}',
    '{metadata_hash}',
    '{content_hash}',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    '{str(file_path)}'
);

--rollback DELETE FROM gameplay.quests WHERE metadata_id = '{metadata['id']}';

"""
        return sql

    def run(self):
        """Main script execution"""
        self.log_info("Starting Tokyo Part 2 quest import")

        source_dir = Path(self.args.source_dir)
        output_dir = Path(self.args.output_dir)

        if not source_dir.exists():
            raise FileNotFoundError(f"Source directory not found: {source_dir}")

        output_dir.mkdir(parents=True, exist_ok=True)

        yaml_files = list(source_dir.glob("*.yaml"))
        if not yaml_files:
            self.log_warning(f"No YAML files found in {source_dir}")
            return

        self.log_info(f"Found {len(yaml_files)} YAML files to process")

        processed_count = 0
        for yaml_file in sorted(yaml_files):
            try:
                self.log_info(f"Processing {yaml_file.name}")

                # Validate and parse
                quest_data = self.validate_quest_file(yaml_file)

                # Generate migration SQL
                migration_sql = self.generate_migration_sql(quest_data, yaml_file)

                # Write migration file
                timestamp = self.get_timestamp()
                metadata_id = quest_data.get('metadata', {}).get('id', 'unknown')
                migration_filename = f"{timestamp}__tokyo_part2_quest_{metadata_id}.sql"
                migration_path = output_dir / migration_filename

                if migration_path.exists() and not self.args.force:
                    self.log_warning(f"Migration file already exists: {migration_path}")
                    continue

                with open(migration_path, 'w', encoding='utf-8') as f:
                    f.write(migration_sql)

                self.log_info(f"Generated migration: {migration_path}")
                processed_count += 1

            except Exception as e:
                self.log_error(f"Failed to process {yaml_file.name}: {e}")
                continue

        self.log_info(f"Successfully processed {processed_count}/{len(yaml_files)} quest files")

if __name__ == "__main__":
    script = TokyoPart2QuestImportScript()
    script.main()
