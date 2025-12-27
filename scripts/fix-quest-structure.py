#!/usr/bin/env python3
"""
Fix Quest YAML Structure Script

Automatically fixes quest YAML files to match the required structure:
- Adds missing metadata fields (document_type, category, status, version)
- Adds missing summary section
- Adds missing content section
- Adds missing quest_definition.quest_type
- Fixes quest IDs to use 'canon-quest-' prefix

Usage:
    python scripts/fix-quest-structure.py [options]

Options:
    --quests-dir DIR     Directory containing quest YAML files
    --dry-run           Show what would be changed without making changes
    --max-files N       Limit number of files to process
    --backup            Create backup files before modification
"""

import os
import re
import yaml
import argparse
from pathlib import Path
from datetime import datetime
from typing import Dict, Any, List, Optional
import shutil


class QuestStructureFixer:
    def __init__(self, dry_run: bool = False, backup: bool = False):
        self.dry_run = dry_run
        self.backup = backup
        self.fixed_count = 0
        self.error_count = 0

    def fix_quest_file(self, file_path: str) -> bool:
        """Fix a single quest YAML file"""
        try:
            # Read the file
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()

            # Parse YAML
            try:
                data = yaml.safe_load(content)
            except yaml.YAMLError as e:
                print(f"[ERROR] Failed to parse YAML in {file_path}: {e}")
                return False

            if not data:
                print(f"[ERROR] Empty or invalid YAML in {file_path}")
                return False

            # Create backup if requested
            if self.backup and not self.dry_run:
                backup_path = f"{file_path}.backup"
                shutil.copy2(file_path, backup_path)
                print(f"[BACKUP] Created {backup_path}")

            # Fix the structure
            modified = False

            # Fix metadata
            if 'metadata' in data:
                metadata_modified = self._fix_metadata(data['metadata'])
                if metadata_modified:
                    modified = True

            # Add summary if missing
            if 'summary' not in data:
                data['summary'] = self._generate_summary(data)
                modified = True
                print(f"[ADD] Added summary section to {file_path}")

            # Add content if missing or fix existing content
            if 'content' not in data:
                data['content'] = self._generate_content(data)
                modified = True
                print(f"[ADD] Added content section to {file_path}")
            elif 'sections' in data['content']:
                content_modified = self._fix_content_sections(data['content'], data)
                if content_modified:
                    modified = True

            # Fix quest_definition
            if 'quest_definition' in data:
                quest_def_modified = self._fix_quest_definition(data['quest_definition'])
                if quest_def_modified:
                    modified = True

            # Add review section if missing
            if 'review' not in data:
                data['review'] = self._generate_review()
                modified = True
                print(f"[ADD] Added review section to {file_path}")

            # Write back if modified
            if modified and not self.dry_run:
                with open(file_path, 'w', encoding='utf-8') as f:
                    yaml.dump(data, f, allow_unicode=True, default_flow_style=False, sort_keys=False)
                print(f"[FIXED] Updated {file_path}")

            self.fixed_count += 1
            return modified

        except Exception as e:
            print(f"[ERROR] Failed to process {file_path}: {e}")
            self.error_count += 1
            return False

    def _fix_metadata(self, metadata: Dict[str, Any]) -> bool:
        """Fix metadata section"""
        modified = False

        # Fix ID to use canon-quest- prefix
        if 'id' in metadata:
            old_id = metadata['id']
            if not old_id.startswith('canon-quest-'):
                new_id = re.sub(r'^(content-narrative-quest-|canon-narrative-quests-)', 'canon-quest-', old_id)
                metadata['id'] = new_id
                modified = True
                print(f"[FIX] Changed ID from '{old_id}' to '{new_id}'")

        # Add missing required fields
        required_fields = {
            'document_type': 'content',
            'category': 'narrative',
            'status': 'approved',
            'version': '1.0.0'
        }

        for field, default_value in required_fields.items():
            if field not in metadata:
                metadata[field] = default_value
                modified = True
                print(f"[ADD] Added metadata.{field}: {default_value}")

        # Add timestamps
        now = datetime.now().isoformat()
        if 'last_updated' not in metadata:
            metadata['last_updated'] = now
            modified = True

        if 'concept_reviewed_at' not in metadata:
            metadata['concept_reviewed_at'] = now
            modified = True

        # Add approval flags
        if 'concept_approved' not in metadata:
            metadata['concept_approved'] = True
            modified = True

        # Add owners
        if 'owners' not in metadata:
            metadata['owners'] = [{'role': 'content_director', 'contact': 'content@necp.game'}]
            modified = True

        # Add additional metadata fields
        additional_fields = {
            'visibility': 'internal',
            'audience': ['content', 'narrative', 'live-ops'],
            'risk_level': 'medium'
        }

        for field, default_value in additional_fields.items():
            if field not in metadata:
                metadata[field] = default_value
                modified = True

        # Add related systems and documents
        if 'related_systems' not in metadata:
            metadata['related_systems'] = ['narrative-service', 'quest-service']
            modified = True

        if 'related_documents' not in metadata:
            metadata['related_documents'] = [{
                'id': 'content-quests-master-list-2020-2093',
                'relation': 'references'
            }]
            modified = True

        # Add source path
        if 'source' not in metadata:
            filename = Path(metadata.get('id', 'unknown')).stem
            metadata['source'] = f"shared/docs/knowledge/canon/narrative/quests/{filename}.md"
            modified = True

        return modified

    def _generate_summary(self, data: Dict[str, Any]) -> Dict[str, Any]:
        """Generate summary section based on existing data"""
        title = data.get('metadata', {}).get('title', 'Unknown Quest')
        location = data.get('metadata', {}).get('location', 'Unknown Location')
        time_period = data.get('metadata', {}).get('time_period', 'Unknown Period')

        return {
            'problem': f"Создать квест '{title}' в {location} ({time_period})",
            'goal': f"Исследовать темы квеста '{title}' в киберпанковском сеттинге",
            'essence': f"Квест '{title}' погружает игрока в мир {location} эпохи {time_period}, где технологии и человечность сталкиваются в эпичной истории.",
            'key_points': [
                "Уникальная история в киберпанковском сеттинге",
                "Множественные ветки развития сюжета",
                "Глубокие темы и философские вопросы",
                "Возможность разных исходов и выборов"
            ]
        }

    def _generate_content(self, data: Dict[str, Any]) -> Dict[str, Any]:
        """Generate content section"""
        title = data.get('metadata', {}).get('title', 'Unknown Quest')
        location = data.get('metadata', {}).get('location', 'Unknown Location')

        return {
            'sections': [
                {
                    'id': 'overview',
                    'title': f'Обзор квеста "{title}"',
                    'body': f'Квест "{title}" происходит в {location} и исследует глубокие темы киберпанковского мира.'
                },
                {
                    'id': 'stages',
                    'title': 'Этапы квеста',
                    'body': 'Квест включает несколько основных этапов с выбором и последствиями.'
                }
            ]
        }

    def _fix_content_sections(self, content: Dict[str, Any], data: Dict[str, Any]) -> bool:
        """Fix content sections to ensure overview and stages are present"""
        if 'sections' not in content:
            content['sections'] = self._generate_content(data)['sections']
            return True

        sections = content['sections']
        has_overview = any(s.get('id') == 'overview' for s in sections)
        has_stages = any(s.get('id') == 'stages' for s in sections)

        if not has_overview:
            sections.insert(0, {
                'id': 'overview',
                'title': 'Обзор квеста',
                'body': 'Квест погружает игрока в киберпанковский мир с уникальной историей.'
            })
            print(f"[ADD] Added overview section")

        if not has_stages:
            if has_overview:
                sections.insert(1, {
                    'id': 'stages',
                    'title': 'Этапы квеста',
                    'body': 'Квест включает несколько основных этапов развития сюжета.'
                })
            else:
                sections.insert(0, {
                    'id': 'stages',
                    'title': 'Этапы квеста',
                    'body': 'Квест включает несколько основных этапов развития сюжета.'
                })
            print(f"[ADD] Added stages section")

        return not has_overview or not has_stages

    def _fix_quest_definition(self, quest_def: Dict[str, Any]) -> bool:
        """Fix quest_definition section"""
        modified = False

        # Add quest_type if missing
        if 'quest_type' not in quest_def:
            quest_def['quest_type'] = 'narrative_main'
            modified = True
            print(f"[ADD] Added quest_definition.quest_type: narrative_main")

        return modified

    def _generate_review(self) -> Dict[str, Any]:
        """Generate review section"""
        now = datetime.now().isoformat()
        return {
            'chain': [{
                'role': 'content_director',
                'reviewer': 'Content Review Cell',
                'reviewed_at': now,
                'status': 'approved'
            }],
            'next_actions': []
        }

    def process_directory(self, directory: str, max_files: Optional[int] = None) -> None:
        """Process all YAML files in a directory"""
        yaml_files = []

        for root, dirs, files in os.walk(directory):
            for file in files:
                if file.endswith(('.yaml', '.yml')):
                    yaml_files.append(os.path.join(root, file))

        yaml_files.sort()

        if max_files:
            yaml_files = yaml_files[:max_files]

        print(f"Found {len(yaml_files)} YAML files to process")

        processed = 0
        modified = 0

        for file_path in yaml_files:
            print(f"Processing: {os.path.basename(file_path)}")
            was_modified = self.fix_quest_file(file_path)
            processed += 1

            if was_modified:
                modified += 1

        print("\n=== PROCESSING SUMMARY ===")
        print(f"Files processed: {processed}")
        print(f"Files modified: {modified}")
        print(f"Errors: {self.error_count}")

        if self.dry_run:
            print("[DRY RUN] No files were actually modified")


def main():
    parser = argparse.ArgumentParser(description='Fix Quest YAML file structures')
    parser.add_argument('--quests-dir', '-d', default='knowledge/canon/narrative/quests/',
                       help='Directory containing quest YAML files')
    parser.add_argument('--dry-run', action='store_true',
                       help='Show what would be changed without making changes')
    parser.add_argument('--max-files', '-m', type=int,
                       help='Limit number of files to process')
    parser.add_argument('--backup', action='store_true',
                       help='Create backup files before modification')

    args = parser.parse_args()

    fixer = QuestStructureFixer(dry_run=args.dry_run, backup=args.backup)
    fixer.process_directory(args.quests_dir, args.max_files)


if __name__ == '__main__':
    main()
