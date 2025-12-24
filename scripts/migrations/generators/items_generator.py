"""
Items migration generator following SOLID principles.
"""

import json
from pathlib import Path
from typing import Dict, Any, List
from datetime import datetime

from ..base import BaseContentMigrationGenerator
from ..utils import JsonSerializer


class ItemsMigrationGenerator(BaseContentMigrationGenerator):
    """
    Concrete implementation for items migrations.
    Follows Single Responsibility Principle - handles only item processing.
    """

    def __init__(self):
        super().__init__(
            name="generate-items-migrations",
            description="Generate Liquibase YAML migrations for items from knowledge base",
            content_type="items",
            input_dirs=[
                "knowledge/content/items",
                "knowledge/mechanics/gear",
                "knowledge/mechanics/weapons"
            ],
            output_dir="infrastructure/liquibase/data/gameplay/items",
            table_name="gameplay.items"
        )

    def determine_category(self, yaml_file: Path, spec: dict) -> str:
        """Determine item category based on file path and content."""
        # Check path-based categories
        path_str = str(yaml_file).lower()
        if 'weapon' in path_str:
            return 'weapon'
        elif 'armor' in path_str:
            return 'armor'
        elif 'consumable' in path_str:
            return 'consumable'

        # Check content-based categories
        item_def = spec.get('item_definition', {})
        category = item_def.get('category', '').lower()
        if category in ['weapon', 'armor', 'consumable', 'material', 'currency', 'key_item']:
            return category

        return 'material'  # Default category

    def get_content_fields(self) -> List[str]:
        """Required fields for valid item content."""
        return ['item_definition']

    def extract_specific_data(self, spec: Dict[str, Any], yaml_file: Path) -> Dict[str, Any]:
        """Extract item-specific data."""
        metadata = spec.get('metadata', {})
        item_def = spec.get('item_definition', {})
        category = self.determine_category(yaml_file, spec)

        return {
            'item_id': metadata.get('id', f"item-{yaml_file.stem}"),
            'name': metadata.get('name', yaml_file.stem.replace('_', ' ').title()),
            'description': spec.get('description', ''),
            'category': category,
            'rarity': item_def.get('rarity', 'common'),
            'value': item_def.get('value', 0),
            'weight': item_def.get('weight', 0.0),
            'stackable': item_def.get('stackable', True),
            'max_stack': item_def.get('max_stack', 1),
            'properties': json.dumps(item_def.get('properties', {}), default=JsonSerializer.json_serializer, ensure_ascii=False),
            'requirements': json.dumps(item_def.get('requirements', {}), default=JsonSerializer.json_serializer, ensure_ascii=False),
            'effects': json.dumps(item_def.get('effects', {}), default=JsonSerializer.json_serializer, ensure_ascii=False)
        }
