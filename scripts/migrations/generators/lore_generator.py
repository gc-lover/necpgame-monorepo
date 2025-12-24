"""
Lore migration generator following SOLID principles.
"""

import json
from pathlib import Path
from typing import Dict, Any, List
from datetime import datetime

from ..base import BaseContentMigrationGenerator
from ..utils import JsonSerializer


class LoreMigrationGenerator(BaseContentMigrationGenerator):
    """
    Concrete implementation for lore migrations.
    Follows Single Responsibility Principle - handles only lore processing.
    """

    def __init__(self):
        super().__init__(
            name="generate-lore-migrations",
            description="Generate Liquibase YAML migrations for lore from knowledge base",
            content_type="lore",
            input_dirs=[
                "knowledge/canon/lore",
                "knowledge/canon/culture"
            ],
            output_dir="infrastructure/liquibase/data/knowledge/lore",
            table_name="knowledge.lore_entries"
        )

    def determine_category(self, yaml_file: Path, spec: dict) -> str:
        """Determine lore category based on file path and content."""
        # Check path-based categories
        path_parts = yaml_file.parts
        if 'characters' in path_parts:
            return 'characters'
        elif 'factions' in path_parts:
            return 'factions'
        elif 'locations' in path_parts:
            return 'locations'
        elif 'events' in path_parts:
            return 'events'
        elif 'technology' in path_parts:
            return 'technology'
        elif 'culture' in str(yaml_file):
            return 'culture'

        # Check content-based categories
        metadata = spec.get('metadata', {})
        content_category = metadata.get('category', '').lower()
        if content_category in ['characters', 'factions', 'locations', 'events', 'technology']:
            return content_category

        return 'general'  # Default category

    def get_content_fields(self) -> List[str]:
        """Required fields for valid lore content."""
        return ['content']

    def extract_specific_data(self, spec: Dict[str, Any], yaml_file: Path) -> Dict[str, Any]:
        """Extract lore-specific data."""
        metadata = spec.get('metadata', {})
        category = self.determine_category(yaml_file, spec)

        return {
            'title': metadata.get('title', yaml_file.stem.replace('_', ' ').title()),
            'content': spec.get('content', {}).get('text', ''),
            'category': category,
            'tags': json.dumps(spec.get('metadata', {}).get('tags', []), ensure_ascii=False),
            'related_entities': json.dumps([], ensure_ascii=False)  # Will be populated based on content analysis
        }
